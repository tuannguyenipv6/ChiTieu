package com.example.chitiu.Object;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomNgay {
    private int mNgay;
    private int mTuan;
    private int mThang;
    private int mNam;

    //todo*-> nhận vào chuổi s định dạng ra CustomNgay
    public CustomNgay(String S) {
        String[] parts = S.split("/");
        this.mNgay = Integer.parseInt(parts[0]);
        this.mTuan = Integer.parseInt(parts[1]);
        this.mThang = Integer.parseInt(parts[2]);
        this.mNam = Integer.parseInt(parts[3]);
    }
    //todo*-> Nhận vào Date định dạng ra CustomNgay
    public CustomNgay(Date date) {
        //format lại: [Ngày trong tuần trong tháng]/[Tuần trong tháng]/[Tháng trong năm]/[Năm]
        SimpleDateFormat formatter = new SimpleDateFormat("u/WW/MM/yyyy");
        String CustomNgayGio = formatter.format(date);
        String[] parts = CustomNgayGio.split("/");

        this.mNgay = Integer.parseInt(parts[0]);
        this.mTuan = Integer.parseInt(parts[1]);
        this.mThang = Integer.parseInt(parts[2]);
        this.mNam = Integer.parseInt(parts[3]);
    }

    public int getmNgay() {
        return mNgay;
    }

    public void setmNgay(int mNgay) {
        this.mNgay = mNgay;
    }

    public int getmTuan() {
        return mTuan;
    }

    public void setmTuan(int mTuan) {
        this.mTuan = mTuan;
    }

    public int getmThang() {
        return mThang;
    }

    public void setmThang(int mThang) {
        this.mThang = mThang;
    }

    public int getmNam() {
        return mNam;
    }

    public void setmNam(int mNam) {
        this.mNam = mNam;
    }
}
