package com.task.taskfour.transactionbookkeepping;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HomePage extends Fragment {
        DatabaseHelper myDB;
        Fragment fragment = null;
        FragmentManager fm;
        FragmentTransaction ft;
        Cursor expenses,incomes;
        AlertDialog.Builder alert;
        int clicked;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        myDB = new DatabaseHelper(getActivity());
        expenses = myDB.list_expenses();
        incomes = myDB.list_income();
        ListView listExpenses = (ListView)view.findViewById(R.id.listViewExp);
        ListView listIncomes = (ListView)view.findViewById(R.id.listViewInc);
        TextView expTotal = (TextView)view.findViewById(R.id.expensesTotal);
        TextView incTotal = (TextView)view.findViewById(R.id.incomeTotal);
        TextView balTotal = (TextView)view.findViewById(R.id.balanceTotal);

        final EditText descIn = new EditText(getActivity());
        final EditText amtIn = new EditText(getActivity());
        final Button update = new Button(getActivity());

        clicked =0;
        descIn.setInputType(InputType.TYPE_CLASS_TEXT);
        descIn.setHint("Description");
        descIn.setTextColor(Color.parseColor("#c0c0c0"));
        descIn.setHintTextColor(Color.parseColor("#ffffff"));
        descIn.setEnabled(false);
        descIn.setGravity(Gravity.CENTER);
        amtIn.setInputType(InputType.TYPE_CLASS_NUMBER);
        amtIn.setHint("Amount");
        amtIn.setTextColor(Color.parseColor("#c0c0c0"));
        amtIn.setHintTextColor(Color.parseColor("#ffffff"));
        amtIn.setGravity(Gravity.CENTER);
        amtIn.setEnabled(false);
        update.setText("Edit");
        update.setTextColor(Color.parseColor("#1E824C"));
        update.setGravity(Gravity.CENTER);
        update.setBackgroundColor(Color.parseColor("#66CC99"));

        SimpleCursorAdapter adapterExpense = new SimpleCursorAdapter(view.getContext(),
                R.layout.custom_data_list,
                expenses,
                new String[] {"Description","Amount"},
                new int[]{ R.id.data_list_desc, R.id.data_list_amount},0);

        listExpenses.setAdapter(adapterExpense);

        listExpenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                expenses.moveToPosition(pos);
                final int rowId = expenses.getInt(expenses.getColumnIndexOrThrow("_id"));
                final String descTemp = expenses.getString(expenses.getColumnIndex("Description"));
                final String amtTemp = expenses.getString(expenses.getColumnIndex("Amount"));
                alert = new AlertDialog.Builder(getActivity());
                descIn.setText(descTemp);
                amtIn.setText(amtTemp);

                LinearLayout layoutD =new LinearLayout(getActivity());
                layoutD.setOrientation(LinearLayout.VERTICAL);
                layoutD.setPadding(5, 5, 5, 5);
                layoutD.setBackgroundColor(Color.parseColor("#E9D460"));
                layoutD.addView(descIn);
                layoutD.addView(amtIn);
                layoutD.addView(update);
                alert.setView(layoutD);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(clicked==0){
                            Toast.makeText(getActivity(), "Edit Data.", Toast.LENGTH_SHORT).show();
                            descIn.setEnabled(true);
                            amtIn.setEnabled(true);
                            descIn.setTextColor(Color.parseColor("#E74C3C"));
                            amtIn.setTextColor(Color.parseColor("#E74C3C"));
                            clicked=1;
                        }else if (clicked==1){
                            Toast.makeText(getActivity(), "Edit Success.", Toast.LENGTH_SHORT).show();
                            descIn.setEnabled(false);
                            amtIn.setEnabled(false);
                            descIn.setTextColor(Color.parseColor("#c0c0c0"));
                            amtIn.setTextColor(Color.parseColor("#c0c0c0"));//
                            clicked=0;
                        }
                    }
                });

                alert.setCancelable(false)

                        .setTitle("Data Expenses")

                        .setMessage("Please Input Data For Update / Click Delete For Remove :")

                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override

                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(getActivity(), "Canceled.", Toast.LENGTH_SHORT).show();

                                refreshFragment();

                                dialog.cancel();
                            }

                        })


                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {

                        int id = myDB.delete_expense(String.valueOf(rowId));
                        Toast.makeText(getActivity(), "Data Deleted.",
                                Toast.LENGTH_SHORT).show();
                        refreshFragment();
                        dialog.dismiss();

                    }

                })

                .setNeutralButton("Update", new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Update Data.", Toast.LENGTH_SHORT).show();
                        myDB.update_expense(String.valueOf(rowId), descIn.getText().toString(), amtIn.getText().toString());
                        refreshFragment();
                        dialog.dismiss();
                    }

                });
                alert.show();

            }

        });

        SimpleCursorAdapter adapterIncome = new SimpleCursorAdapter(getActivity(),
                R.layout.custom_data_list,
                incomes,
                new String[] {"Description","Amount"},
                new int[]{ R.id.data_list_desc, R.id.data_list_amount} ,0);

        listIncomes.setAdapter(adapterIncome);

        listIncomes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                expenses.moveToPosition(pos);
                final int rowId = incomes.getInt(incomes.getColumnIndexOrThrow("_id"));
                final String descTemp = incomes.getString(incomes.getColumnIndex("Description"));
                final String amtTemp = incomes.getString(incomes.getColumnIndex("Amount"));
                alert = new AlertDialog.Builder(getActivity());

                descIn.setText(descTemp);
                amtIn.setText(amtTemp);

                LinearLayout layoutD = new LinearLayout(getActivity());
                layoutD.setOrientation(LinearLayout.VERTICAL);
                layoutD.setPadding(5, 5, 5, 5);
                layoutD.setBackgroundColor(Color.parseColor("#E9D460"));
                layoutD.addView(descIn);
                layoutD.addView(amtIn);
                layoutD.addView(update);
                alert.setView(layoutD);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(clicked==0) {
                            Toast.makeText(getActivity(), "Edit Data.", Toast.LENGTH_SHORT).show();
                            descIn.setEnabled(true);
                            amtIn.setEnabled(true);
                            descIn.setTextColor(Color.parseColor("#E74C3C"));
                            amtIn.setTextColor(Color.parseColor("#E74C3C"));
                            clicked=1;
                        }else if (clicked==1){
                            Toast.makeText(getActivity(), "Edit Success.", Toast.LENGTH_SHORT).show();
                            descIn.setEnabled(false);
                            amtIn.setEnabled(false);
                            descIn.setTextColor(Color.parseColor("#c0c0c0"));
                            amtIn.setTextColor(Color.parseColor("#c0c0c0"));//
                            clicked=0;
                        }
                    }
                });


                alert.setCancelable(false)

                        .setTitle("Data Incomes")

                        .setMessage("Please Input Data For Update / Click Delete For Remove :")

                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override

                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(getActivity(), "Canceled.", Toast.LENGTH_SHORT).show();

                                refreshFragment();

                                dialog.cancel();
                            }

                        })

                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {

                            @Override

                            public void onClick(DialogInterface dialog, int which) {

                                int id = myDB.delete_Income(String.valueOf(rowId));
                                Toast.makeText(getActivity(), "Data Deleted.",
                                        Toast.LENGTH_SHORT).show();
                                refreshFragment();
                                dialog.cancel();

                            }

                        })
                        .setNeutralButton("Update", new DialogInterface.OnClickListener() {

                            @Override

                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "Update Data.", Toast.LENGTH_SHORT).show();
                                myDB.update_income(String.valueOf(rowId), descIn.getText().toString(), amtIn.getText().toString());
                                refreshFragment();
                                dialog.dismiss();
                            }

                        });

                alert.show();
            }

        });

        int sumExp = 0;

        while (expenses.moveToNext()) {
            sumExp += expenses.getInt(expenses.getColumnIndex("Amount"));

        }
        expTotal.setText("$ "+String.valueOf(sumExp));

        int sumInc = 0;

        while (incomes.moveToNext()) {
            sumInc += incomes.getInt(incomes.getColumnIndex("Amount"));
        }

        incTotal.setText("$ "+String.valueOf(sumInc));

        balTotal.setText("$ "+String.valueOf(sumInc-sumExp));

        myDB.close();

        return view;
    }

    public void refreshFragment(){
        fragment = new HomePage();
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.fragment_place, fragment);
        ft.commit();
    }

}