package com.tugas.adi.edebts;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;
/**
 * Created by Imam Muhtadi on 6/25/2015.
 */
public class MySQLHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    public static final String TABLE = "data";

    // Columns
    public static final String nama = "nama";
    public static final String jenis = "jenis";
    public static final String nominal = "nominal";
    public static final String deskripsi = "deskripsi";
    public static final String status = "status";

    public MySQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //TODO Auto-generated constructor stub
        }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE + "( _id" + " integer primary key autoincrement, "
                + nama + " text not null, "
                + jenis + " text not null, "
                + nominal + " int not null, "
                + deskripsi + " int not null, "
                + status + " text not null);";
        String sql1 = "insert into data values(null,'Adi','hutang',1000,'makan','unpaid');";
        Log.d("Data", "onCreate: " + sql);
        db.execSQL(sql);
        db.execSQL(sql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO Auto-generated method stub
    }
}