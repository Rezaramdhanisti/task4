package com.task.taskfour.transactionbookkeepping;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class SyncPage extends Fragment {

    Retrofit retrofit;
    Gson gson;
    SyncApi transactionApi;
    DatabaseHelper myDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sync_page, container, false);

        Button sync = (Button)view.findViewById(R.id.btn_sync);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    send_data("http://private-142c32-datausers.apiary-mock.com");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    public void send_data(final String url_target)throws JSONException{
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        retrofit = new Retrofit.Builder().baseUrl(url_target)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        transactionApi = retrofit.create(SyncApi.class);

        Transaction transc = new Transaction();

        boolean status=false;

        myDB = new DatabaseHelper(getActivity());
        Cursor expenses = myDB.list_expenses();
        Cursor incomes = myDB.list_income();

        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Processing...");
        mProgressDialog.show();

        JSONArray listTransc = new JSONArray();

        try {
            while (expenses.moveToNext()) {

                JSONObject trans_json = new JSONObject();

                trans_json.put("id", expenses.getInt(expenses.getColumnIndex("_id")));

                trans_json.put("description", expenses.getString(expenses.getColumnIndex("Description")));

                trans_json.put("amount", expenses.getString(expenses.getColumnIndex("Amount")));

                    /*Log.e("id ", expenses.getString(expenses.getColumnIndex("_id")));
                    Log.e("desc ",  expenses.getString(expenses.getColumnIndex("Description")));
                    Log.e("amt ", expenses.getString(expenses.getColumnIndex("Amount")));*/

                listTransc.put(trans_json);

            }

            while (incomes.moveToNext()) {
                JSONObject trans_json = new JSONObject();

                trans_json.put("id", incomes.getInt(incomes.getColumnIndex("_id")));

                trans_json.put("description", incomes.getString(incomes.getColumnIndex("Description")));

                trans_json.put("amount", incomes.getString(incomes.getColumnIndex("Amount")));

                listTransc.put(trans_json);
            }

        } catch (JSONException e) {
            Log.i("info", String.valueOf(e));
        }


        for(int i=0;i<listTransc.length();i++) {

            JSONObject transaction = listTransc.getJSONObject(i);

            if (transaction.get("id")==""&&transaction.get("description")==""&&transaction.get("amount")==""){
                status=false;
//                Log.d("JSON Object ", "Null.");

            }else{
                /*Log.d("id ", transaction.get("id").toString());
                Log.d("desc ", transaction.get("description").toString());
                Log.d("amt ", transaction.get("amount").toString());*/
                status=true;
            }
        }

        Call<Transaction> call = transactionApi.syncTransactions(transc);

        if(status==false){
//            Log.d("Status ", "No Data Synchronize.");
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }else {
            call.enqueue(new Callback<Transaction>() {
                @Override
                public void onResponse(Response<Transaction> response, Retrofit retrofit) {
                    int responCode = response.code();
//                    Log.e("Response Code ", String.valueOf(responCode));
                    try {
                        String statusResponse = response.body().getStatus();
                        String messageResponse = response.body().getMessage();
                        if (statusResponse.equals("200")) {
                            Toast.makeText(getActivity(), messageResponse, Toast.LENGTH_SHORT).show();
                        } else if (statusResponse.equals("400")) {
                            Toast.makeText(getActivity(), messageResponse, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.d("Response exception ", e.toString());
                    }
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                }

                @Override
                public void onFailure(Throwable t) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    alert.setCancelable(false)

                            .setTitle("Synchronize")

                            .setMessage("Fails Synchronize.")

                            .setPositiveButton("Skip", new DialogInterface.OnClickListener() {

                                @Override

                                public void onClick(DialogInterface dialog, int which) {

                                    Toast.makeText(getActivity(), "Skip.", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }

                            })


                            .setNegativeButton("Retry", new DialogInterface.OnClickListener() {

                                @Override

                                public void onClick(DialogInterface dialog, int which) {

                                    try {
                                        send_data(url_target);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    dialog.cancel();
                                    Toast.makeText(getActivity(), "No Internet Connection.", Toast.LENGTH_SHORT).show();
                                }

                            });

                    alert.show();
                }
            });
        }
        myDB.close();
    }
}
