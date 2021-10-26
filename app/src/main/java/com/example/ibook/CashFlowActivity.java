package com.example.ibook;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CashFlowActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CashFlowAdapter cashflowRecyclerAdapter;
    private DBHelper databaseHelper;
    private List<Cashflow> listCashflow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_flow);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCashflow);

        listCashflow = new ArrayList<>();
        cashflowRecyclerAdapter = new CashFlowAdapter(listCashflow);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(cashflowRecyclerAdapter);
        databaseHelper = new DBHelper(getApplicationContext());

        getDataFromSQLite();
    }

    private void getDataFromSQLite() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listCashflow.clear();
                listCashflow.addAll(databaseHelper.getAllCashflow(getIntent().getStringExtra("username")));
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                cashflowRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    public void toHomeee(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
        finish();
    }
}
