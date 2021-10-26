package com.example.ibook;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MasukActivity extends AppCompatActivity {
    private DBHelper databaseHelper;
    private Cashflow cashflow;

    EditText tgl, nominal, ket;
    ImageView kalendar;
    Button simpan;

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masuk);

        kalendar = findViewById(R.id.logo_masuk);
        tgl = findViewById(R.id.tgl_masuk);
        nominal = findViewById(R.id.nominal_masuk);
        ket = findViewById(R.id.keterangan_masuk);
        simpan = findViewById(R.id.penyimpanan_masuk);

        databaseHelper = new DBHelper(getApplicationContext());
        cashflow = new Cashflow();

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel();
            }
        };

        kalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MasukActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tgl.getText().toString().isEmpty() && !nominal.getText().toString().isEmpty() && !ket.getText().toString().isEmpty()){
                    cashflow.setJenis("in");
                    cashflow.setTgl(tgl.getText().toString().trim());
                    cashflow.setNominal(nominal.getText().toString().trim());
                    cashflow.setKeterangan(ket.getText().toString().trim());
                    databaseHelper.addCashflow(cashflow, getIntent().getStringExtra("username"));

                    emptyInputEditText();
                    Toast.makeText(getApplicationContext(), "Data berhasil disimpan!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Tidak boleh ada yang kosong!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tgl.setText(sdf.format(myCalendar.getTime()));
    }

    public void toHome(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
        finish();
    }

    private void emptyInputEditText() {
        tgl.setText(null);
        nominal.setText(null);
        ket.setText(null);
    }
}
