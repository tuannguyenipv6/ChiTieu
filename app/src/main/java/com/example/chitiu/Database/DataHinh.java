package com.example.chitiu.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.chitiu.Object.ChiTieu;
import com.example.chitiu.Object.IdHinh;

import java.util.ArrayList;
import java.util.List;

public class DataHinh extends SQLiteOpenHelper {
    String TENBANG = "DatabaseHinh";
    String D_1ID = "dID";
    String D_2VALUE = "dValue";
    public DataHinh(@Nullable Context context) {
        super(context, "DatabaseHinh.sql", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateChiTieu = "CREATE TABLE IF NOT EXISTS " +
                TENBANG + "(" + D_1ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                D_2VALUE + " VARCHAR(255))";
        db.execSQL(CreateChiTieu);
        String insert = "INSERT INTO " + TENBANG + " VALUES(null, '123')";
        db.execSQL(insert);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //todo*-> Thêm
    public void Them(String Value){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(D_2VALUE, Value);
        //Insert
        database.insert(TENBANG, "", contentValues);
        database.close();//đóng bản
    }

    //todo*-> Xóa
    public int Xoa(int ID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TENBANG, D_1ID + "=?", new String[]{String.valueOf(ID)});
    }

    //todo*-> Lấy ALL
    public List<IdHinh> AllHinh(){
        List<IdHinh> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TENBANG;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            int dID = cursor.getInt(0);
            if (dID > 1){
                String dValue = cursor.getString(1);
                IdHinh idHinh = new IdHinh(dID, dValue);
                list.add(idHinh);
            }
            cursor.moveToNext();
        }
        this.close();
        return list;
    }

    //todo*-> Check Bảng có data không
    //kiểm tra tài khoảng có hay không
    public boolean Check(){
        boolean result = false;
        String sql = "SELECT * FROM " + TENBANG;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        String Value = "";
        while (!cursor.isAfterLast()){
             Value = cursor.getString(1);
            cursor.moveToNext();
        }
        try {
            int Check = Integer.parseInt(Value);
            if (Check == 123){
                result = true;
            }
        }catch (Exception ex){
            result = false;
        }
        return result;
    }

    //todo*-> xóa Hinh
    public int Delete(int ID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TENBANG, D_1ID + "=?", new String[]{String.valueOf(ID)});
    }
}
