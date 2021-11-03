package com.example.chitiu.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataAnimation extends SQLiteOpenHelper {
    String TEN_DATABASE = "Animation";
    String D_1ID = "dID";
    String D_2NoiDung = "dNoiDung";

    public DataAnimation(@Nullable Context context) {
        super(context, "DatabaseAnimatin.sql", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //todo*-> Khởi tạo tên bản cùng các thuộc tính
        String CreateChiTieu = "CREATE TABLE IF NOT EXISTS " +
                TEN_DATABASE + "(" + D_1ID + " INTEGER PRIMARY KEY, " +
                D_2NoiDung + " INTEGER(1))";
        db.execSQL(CreateChiTieu);

        String insert = "INSERT INTO " + TEN_DATABASE + " VALUES('1', '4')";
        db.execSQL(insert);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public int LayData(){
        String sql = "SELECT * FROM " + TEN_DATABASE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        int dCheck = cursor.getInt(1);
        return dCheck;
    }
    public int SuaData(int Data){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(D_2NoiDung, Data);
        return sqLiteDatabase.update(TEN_DATABASE, contentValues, D_1ID + "=?", new String[]{String.valueOf("1")});
    }
}
