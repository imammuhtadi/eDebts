package com.tugas.adi.edebts;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.SnackBar;


public class LunasActivity extends ActionBarActivity {
    Toolbar toolbar;
    MySQLHelper dbHelper;
    protected Cursor cursor;
    protected ListAdapter adapter;
    protected ListView numberList;
    private int id=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunas);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        numberList = (ListView) findViewById(R.id.ListLunas);
        dbHelper = new MySQLHelper(this);
        numberList.setSelected(true);
        numberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                cursor = db.rawQuery("SELECT * FROM data WHERE status='paid' ORDER BY _id DESC",null);
                cursor.moveToPosition(arg2);
                final String tempNama = cursor.getString(1);
                final String tempJenis = cursor.getString(2);
                final String tempNominal = cursor.getString(3);
                final String tempDesk = cursor.getString(4);
                Dialog dialog = new Dialog(LunasActivity.this, "History " + tempNama, "Nama \t: " + tempNama + "\nJumlah \t: " + tempNominal + "\nKet \t: " + tempDesk);

                dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                        String sqlupdate = "UPDATE data SET status='unpaid' WHERE _id='" + id + "';";
                        db.execSQL(sqlupdate);
                        view();
                        Toast.makeText(LunasActivity.this, "Data Telah Diupdate", Toast.LENGTH_SHORT).show();
                        new SnackBar(LunasActivity.this,"Dikembalikan ke daftar", "UNDO", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                SQLiteDatabase db = dbHelper.getReadableDatabase();
                                String sqlundo = "UPDATE data SET status='paid' WHERE _id='" + id + "';";
                                db.execSQL(sqlundo);
                                Toast.makeText(LunasActivity.this, "Data Telah Dikembalikan", Toast.LENGTH_SHORT).show();
                                view();
                            }
                        }).show();
                    }
                });
                dialog.addCancelButton("HAPUS");
                dialog.setOnCancelButtonClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                        String sqlhapus = "DELETE FROM data WHERE _id='" + id + "';";
                        db.execSQL(sqlhapus);
                        view();
                        Toast.makeText(LunasActivity.this, "Data Telah Dihapus", Toast.LENGTH_SHORT).show();

                        new SnackBar(LunasActivity.this,
                                "Data Telah Dihapus",
                                "UNDO", new View.OnClickListener(){

                            @Override
                            public void onClick(View v) {
                                SQLiteDatabase db = dbHelper.getReadableDatabase();
                                String sqlundo = "INSERT INTO data values('" + id + "','" + tempNama + "','" + tempJenis + "'," + tempNominal + ",'" + tempDesk + "','paid')";
                                db.execSQL(sqlundo);
                                view();
                                Toast.makeText(LunasActivity.this, "Data Telah Dikembalikan", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                        //*/
                    }
                });
                dialog.show();
                ButtonFlat acceptButton = dialog.getButtonAccept();
                acceptButton.setText("LUNAS");
                id=cursor.getInt(0);
            }
        });
        view();
    }
    private void view(){
        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM data WHERE status='paid' ORDER BY _id DESC;",null);
            adapter = new SimpleCursorAdapter(this,R.layout.list_single,cursor,
                    new String[] {"nama","nominal","deskripsi"},
                    new int[] {R.id.nama,R.id.nominal,R.id.deskripsi});
            numberList.setAdapter(adapter);}
        catch(Exception e){
            //Your Code Here
        }
    }
}
