package com.tugas.adi.edebts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.SnackBar;

/**
 * Created by hp1 on 21-01-2015.
 */
public class Piutang extends Fragment {
    MySQLHelper dbHelper;
    protected Cursor cursor;
    protected ListAdapter adapter;
    protected ListView numberList;
    private int id=-1;

    /** Called when the activity is first created. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_piutang, container, false);

        numberList = (ListView) v.findViewById(R.id.ListPiutang);
        dbHelper = new MySQLHelper(getActivity());
        numberList.setSelected(true);
        numberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                cursor = db.rawQuery("SELECT * FROM data WHERE (jenis='piutang' AND status='unpaid') ORDER BY _id DESC",null);
                cursor.moveToPosition(arg2);
                final String tempNama = cursor.getString(1);
                final String tempNominal = cursor.getString(3);
                final String tempDesk = cursor.getString(4);
                Dialog dialog = new Dialog(getActivity(), "Piutang " + tempNama, "Nama \t: " + tempNama + "\nJumlah \t: " + tempNominal + "\nKet \t: " + tempDesk);

                dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                        String sqlupdate = "UPDATE data SET status='paid' WHERE _id='" + id + "';";
                        db.execSQL(sqlupdate);
                        view();
                        Toast.makeText(getActivity(), "Data Telah Diupdate", Toast.LENGTH_SHORT).show();
                        new SnackBar(getActivity(),"Ditandai Telah Lunas", "UNDO", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                SQLiteDatabase db = dbHelper.getReadableDatabase();
                                String sqlundo = "UPDATE data SET status='unpaid' WHERE _id='" + id + "';";
                                db.execSQL(sqlundo);
                                Toast.makeText(getActivity(), "Data Telah Dikembalikan", Toast.LENGTH_SHORT).show();
                                view();
                            }
                        }).show();
                    }
                });
                dialog.addCancelButton("HAPUS");
                dialog.setOnCancelButtonClickListener(new View.OnClickListener()

                {

                    @Override
                    public void onClick(View v) {
                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                        String sqlhapus = "DELETE FROM data WHERE _id='" + id + "';";
                        db.execSQL(sqlhapus);
                        view();
                        Toast.makeText(getActivity(), "Data Telah Dihapus", Toast.LENGTH_SHORT).show();

                        new SnackBar(getActivity(),
                                "Data Telah Dihapus",
                                "UNDO", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                SQLiteDatabase db = dbHelper.getReadableDatabase();
                                String sqlundo = "INSERT INTO data values('" + id + "','" + tempNama + "','piutang'," + tempNominal + ",'" + tempDesk + "','unpaid')";
                                db.execSQL(sqlundo);
                                view();
                                Toast.makeText(getActivity(), "Data Telah Dikembalikan", Toast.LENGTH_SHORT).show();
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
        return v;
    }
    private void view(){
        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM data WHERE (jenis='piutang' AND status='unpaid') ORDER BY _id DESC;",null);
            adapter = new SimpleCursorAdapter(getActivity(),R.layout.list_single,cursor,
                    new String[] {"nama","nominal","deskripsi"},
                    new int[] {R.id.nama,R.id.nominal,R.id.deskripsi});
            numberList.setAdapter(adapter);}
        catch(Exception e){
            //Your Code Here
        }
    }
}
