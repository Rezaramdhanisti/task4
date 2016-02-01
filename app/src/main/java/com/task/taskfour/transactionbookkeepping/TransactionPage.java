package com.task.taskfour.transactionbookkeepping;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class TransactionPage extends Fragment {

    DatabaseHelper myDB;
    Button add_expense,add_income;
    EditText descExpense,amountExpense,descIncome,amountIncome;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_page, container, false);
        myDB = new DatabaseHelper(getActivity());
        descExpense = (EditText)view.findViewById(R.id.inputDescExpense);
        descIncome = (EditText)view.findViewById(R.id.inputDescIncome);
        amountExpense = (EditText)view.findViewById(R.id.inputAmtExpense);
        amountIncome = (EditText)view.findViewById(R.id.inputAmtIncome);
        add_expense = (Button)view.findViewById(R.id.btn_add_exp);
        add_income = (Button)view.findViewById(R.id.btn_add_inc);

        add_expense.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String desc = descExpense.getText().toString();
                String amt = amountExpense.getText().toString();
                myDB.save_expense(desc,amt);
            }
        });

        add_income.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String desc = descIncome.getText().toString();
                String amt = amountIncome.getText().toString();
                myDB.save_income(desc, amt);
            }
        });

        return view;
    }
}
