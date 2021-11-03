package com.example.chitiu.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.chitiu.Object.ChiTieu;
import com.example.chitiu.Object.CustomNgay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataChiTieu extends SQLiteOpenHelper {
    private String TEN_DATABASE = "ChiTieu";
    private String D_1ID = "dID";
    private String D_2NoiDung = "dNoiDung";
    private String D_3KhoanTien = "dKhoanTien";
    private String D_4NgayGio = "dNgayGio";
    private String D_5CustomNgayGio = "dCustomNgayGio";
    private String D_6Check = "dCheck";

    //todo*-> Contructo
    public DataChiTieu(@Nullable Context context) {
        super(context, "DatabaseChiTieu.sql", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //todo*-> Khởi tạo tên bản cùng các thuộc tính
        String CreateChiTieu = "CREATE TABLE IF NOT EXISTS " +
                TEN_DATABASE + "(" + D_1ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                D_2NoiDung + " VARCHAR(200), " +
                D_3KhoanTien + " INTEGER(9), " +
                D_4NgayGio + " VARCHAR(200), " +
                D_5CustomNgayGio+ " VARCHAR(200), " +
                D_6Check+ " INTEGER(1))";
        db.execSQL(CreateChiTieu);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //todo*-> Thêm 1 khoản chi tiêu.
    public void ThemChiTieu(ChiTieu chiTieu){
        //getWritableDatabase(): truy vấn
        SQLiteDatabase database = this.getWritableDatabase();

        //Insert thông qua ContentValues
        ContentValues contentValues = new ContentValues();
        contentValues.put(D_2NoiDung, chiTieu.getmNoiDung());
        contentValues.put(D_3KhoanTien, chiTieu.getmKhoanTien());
        contentValues.put(D_4NgayGio, chiTieu.getmNgayGio());
        contentValues.put(D_5CustomNgayGio, chiTieu.getmCustomNgayGio());
        contentValues.put(D_6Check, chiTieu.getmCheck());

        //Insert
        database.insert(TEN_DATABASE, "", contentValues);
        database.close();//đóng bản
    }

    //todo*-> Lấy Chi tiêu theo Tháng
    public List<ChiTieu> AllChiTieuThang(int L_Check, Date L_date){
        List<ChiTieu> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TEN_DATABASE;                             //truy vấn tất cả trong bản SinhVien
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);            //dùng cursor để điều hướng kết quả truy vấn
        cursor.moveToFirst();                                                     //di chuyển con trỏ đến hàng đầu tiên của bảng

        while (!cursor.isAfterLast()){                                           //vòng lặp while chạy từ hàng đầu tiên tới hàng cuối cùng qua cursor
            //so sánh check
            int dCheck = cursor.getInt(5);
            //so sánh tháng
            CustomNgay L_Ngay = new CustomNgay(L_date);
            String dCusNgay = cursor.getString(4);
            CustomNgay D_Ngay = new CustomNgay(dCusNgay);

            if (L_Check == dCheck && L_Ngay.getmNam() == D_Ngay.getmNam() && L_Ngay.getmThang() == D_Ngay.getmThang()){
                int dID = cursor.getInt(0);
                String dNoiDung = cursor.getString(1);
                int dKhoanTien = cursor.getInt(2);
                String dNgay = cursor.getString(3);
                ChiTieu chiTieu = new ChiTieu(dID, dNoiDung, dKhoanTien, dNgay, dCusNgay, dCheck);
                list.add(chiTieu);
            }
            cursor.moveToNext();
        }
        this.close();

        return list;
    }

    //todo*-> Lấy Chi tiêu theo TUẦN
    public List<ChiTieu> AllChiTieuTuan(int L_Check, Date L_date){
        List<ChiTieu> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TEN_DATABASE;                             //truy vấn tất cả trong bản SinhVien
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);            //dùng cursor để điều hướng kết quả truy vấn
        cursor.moveToFirst();                                                     //di chuyển con trỏ đến hàng đầu tiên của bảng

        while (!cursor.isAfterLast()){                                           //vòng lặp while chạy từ hàng đầu tiên tới hàng cuối cùng qua cursor
            //so sánh check
            int dCheck = cursor.getInt(5);
            //so sánh tháng
            CustomNgay L_Ngay = new CustomNgay(L_date);
            String dCusNgay = cursor.getString(4);
            CustomNgay D_Ngay = new CustomNgay(dCusNgay);

            if (L_Check == dCheck && L_Ngay.getmNam() == D_Ngay.getmNam() && L_Ngay.getmTuan() == D_Ngay.getmTuan() && L_Ngay.getmThang() == D_Ngay.getmThang()){
                int dID = cursor.getInt(0);
                String dNoiDung = cursor.getString(1);
                int dKhoanTien = cursor.getInt(2);
                String dNgay = cursor.getString(3);
                ChiTieu chiTieu = new ChiTieu(dID, dNoiDung, dKhoanTien, dNgay, dCusNgay, dCheck);
                list.add(chiTieu);
            }
            cursor.moveToNext();
        }
        this.close();

        return list;
    }

    //todo*-> Lấy Chi tiêu theo Hôm nay
    public List<ChiTieu> AllChiTieuHmNay(int L_Check, Date L_date){
        List<ChiTieu> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TEN_DATABASE;                             //truy vấn tất cả trong bản SinhVien
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);            //dùng cursor để điều hướng kết quả truy vấn

        SimpleDateFormat formatter = new SimpleDateFormat("u/WW/MM/yyyy");
        String NgayNhan = formatter.format(L_date);
        cursor.moveToFirst();                                                     //di chuyển con trỏ đến hàng đầu tiên của bảng

        while (!cursor.isAfterLast()){                                           //vòng lặp while chạy từ hàng đầu tiên tới hàng cuối cùng qua cursor
            //so sánh check
            int dCheck = cursor.getInt(5);

            String dCusNgay = cursor.getString(4);

            if (L_Check == dCheck && NgayNhan.equals(dCusNgay)){
                int dID = cursor.getInt(0);
                String dNoiDung = cursor.getString(1);
                int dKhoanTien = cursor.getInt(2);
                String dNgay = cursor.getString(3);

                ChiTieu chiTieu = new ChiTieu(dID, dNoiDung, dKhoanTien, dNgay, dCusNgay, dCheck);
                list.add(chiTieu);
            }
            cursor.moveToNext();
        }
        this.close();

        return list;
    }

    //todo*-> Lấy ALL Chi tiêu
    public List<ChiTieu> AllChiTieu(int L_Check){
        List<ChiTieu> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TEN_DATABASE;                             //truy vấn tất cả trong bản SinhVien
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);            //dùng cursor để điều hướng kết quả truy vấn
        cursor.moveToFirst();                                                     //di chuyển con trỏ đến hàng đầu tiên của bảng

        while (!cursor.isAfterLast()){                                           //vòng lặp while chạy từ hàng đầu tiên tới hàng cuối cùng qua cursor
            //so sánh check
            int dCheck = cursor.getInt(5);

            if (L_Check == dCheck){
                int dID = cursor.getInt(0);
                String dNoiDung = cursor.getString(1);
                int dKhoanTien = cursor.getInt(2);
                String dNgay = cursor.getString(3);
                String dCusNgay = cursor.getString(4);
                ChiTieu chiTieu = new ChiTieu(dID, dNoiDung, dKhoanTien, dNgay, dCusNgay, dCheck);
                list.add(chiTieu);
            }
            cursor.moveToNext();
        }
        this.close();

        return list;
    }

    //todo*-> xóa ChiTieu
    public int DeleteChiTieu(int ID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TEN_DATABASE, D_1ID + "=?", new String[]{String.valueOf(ID)});
    }

    //todo*-> Sửa Chitieu không có ngày
    public int UpdateKoNgay(ChiTieu chiTieu){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(D_2NoiDung, chiTieu.getmNoiDung());
        contentValues.put(D_3KhoanTien, chiTieu.getmKhoanTien());
        return sqLiteDatabase.update(TEN_DATABASE, contentValues, D_1ID + "=?", new String[]{String.valueOf(chiTieu.getmID())});
    }

    //todo*-> Sửa Chitieu có ngày
    public int UpdateCoNgay(ChiTieu chiTieu){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(D_2NoiDung, chiTieu.getmNoiDung());
        contentValues.put(D_3KhoanTien, chiTieu.getmKhoanTien());
        contentValues.put(D_4NgayGio, chiTieu.getmNgayGio());
        contentValues.put(D_5CustomNgayGio, chiTieu.getmCustomNgayGio());
        return sqLiteDatabase.update(TEN_DATABASE, contentValues, D_1ID + "=?", new String[]{String.valueOf(chiTieu.getmID())});
    }

    //todo*-> Truy vấn lấy ra chi tiêu của 5 tuần [Theo tháng và năm]
    public int[] DataTuan(Date date, int Key_CT){
        int[] result = new int[5];

        String sql = "SELECT * FROM " + TEN_DATABASE;                             //truy vấn tất cả trong bản SinhVien
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);            //dùng cursor để điều hướng kết quả truy vấn

        CustomNgay customNgayNhan = new CustomNgay(date);
        for (int i = 0 ; i <= 4 ; i++){
            cursor.moveToFirst();
            int gtri = 0;
            while (!cursor.isAfterLast()){                                           //vòng lặp while chạy từ hàng đầu tiên tới hàng cuối cùng qua cursor
                String NgayLay = cursor.getString(4);
                CustomNgay customNgayLay = new CustomNgay(NgayLay);
                int dCheck = cursor.getInt(5);
                if (customNgayNhan.getmThang() == customNgayLay.getmThang() && customNgayNhan.getmNam() == customNgayLay.getmNam() && customNgayLay.getmTuan() == i+1 && dCheck == Key_CT){
                    int KhoanTien = cursor.getInt(2);
                    gtri = gtri + KhoanTien;
                }
                cursor.moveToNext();
            }
            result[i] = gtri;
        }
        this.close();
        return result;
    }

    //todo*-> Truy vấn lấy ra chi tiêu của 7 ngày [Theo tuần trong tháng]
    public int[] DataNgay(Date date, int Key_CT){
        int[] result = new int[7];

        String sql = "SELECT * FROM " + TEN_DATABASE;                             //truy vấn tất cả trong bản SinhVien
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);            //dùng cursor để điều hướng kết quả truy vấn

        CustomNgay customNgayNhan = new CustomNgay(date);
        for (int i = 0 ; i <= 6 ; i++){
            cursor.moveToFirst();
            int gtri = 0;
            while (!cursor.isAfterLast()){                                           //vòng lặp while chạy từ hàng đầu tiên tới hàng cuối cùng qua cursor
                String NgayLay = cursor.getString(4);
                CustomNgay customNgayLay = new CustomNgay(NgayLay);
                int dCheck = cursor.getInt(5);
                if (customNgayNhan.getmTuan() == customNgayLay.getmTuan() && customNgayNhan.getmThang() == customNgayLay.getmThang() && customNgayNhan.getmNam() == customNgayLay.getmNam() && customNgayLay.getmNgay() == i+1 && dCheck == Key_CT){
                    int KhoanTien = cursor.getInt(2);
                    gtri = gtri + KhoanTien;
                }
                cursor.moveToNext();
            }
            result[i] = gtri;
        }
        this.close();
        return result;
    }

    //todo*-> Truy vấn lấy ra chi tiêu của 12 tháng [Theo năm]
    public int[] DataThang(Date date, int Key_CT){
        int[] result = new int[12];

        String sql = "SELECT * FROM " + TEN_DATABASE;                             //truy vấn tất cả trong bản SinhVien
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);            //dùng cursor để điều hướng kết quả truy vấn

        CustomNgay customNgayNhan = new CustomNgay(date);
        for (int i = 0 ; i <= 11 ; i++){
            cursor.moveToFirst();
            int gtri = 0;
            while (!cursor.isAfterLast()){                                           //vòng lặp while chạy từ hàng đầu tiên tới hàng cuối cùng qua cursor
                String NgayLay = cursor.getString(4);
                CustomNgay customNgayLay = new CustomNgay(NgayLay);
                int dCheck = cursor.getInt(5);
                if (customNgayLay.getmThang() == i+1  && customNgayNhan.getmNam() == customNgayLay.getmNam()&& dCheck == Key_CT){
                    int KhoanTien = cursor.getInt(2);
                    gtri = gtri + KhoanTien;
                }
                cursor.moveToNext();
            }
            result[i] = gtri;
        }
        this.close();
        return result;
    }
}
