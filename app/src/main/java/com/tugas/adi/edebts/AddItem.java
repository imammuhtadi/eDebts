package com.tugas.adi.edebts;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;


public class AddItem extends ActionBarActivity {

    Toolbar toolbar;
    MySQLHelper dbHelper;
    String nama, nominal, jenis, deskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        dbHelper = new MySQLHelper(this);
    }

    public void onJenisClicked(View view){
        boolean checked = ((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.rbHutang:
                jenis = "hutang";
                break;
            case R.id.rbPiutang:
                jenis = "piutang";
                break;
        }
    }

    public void onAddClick(View v){
        EditText etNama = (EditText) findViewById(R.id.nama);
        EditText etNominal = (EditText) findViewById(R.id.nominal);
        EditText etDeskripsi = (EditText) findViewById(R.id.desc);
        nama = etNama.getText().toString();
        nominal = etNominal.getText().toString();
        deskripsi = etDeskripsi.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try{
            db.execSQL("insert into "+ MySQLHelper.TABLE + " values(null,'"+nama+"','"+jenis+"',"+nominal+",'"+deskripsi+"','unpaid')");
            etNama.setText("");
            etNominal.setText("");
            etDeskripsi.setText("");
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }catch(Exception e) {
            //Toast
        }
    }
}
