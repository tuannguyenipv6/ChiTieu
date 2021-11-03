package com.example.chitiu.Object;

import java.util.Comparator;

public class ChiTieu {
    private int mID;
    private String mNoiDung;
    private int mKhoanTien;
    private String mNgayGio;
    private String mCustomNgayGio;
    private int mCheck;

    public ChiTieu(int mID, String mNoiDung, int mKhoanTien, String mNgayGio, String mCustomNgayGio, int mCheck) {
        this.mID = mID;
        this.mNoiDung = mNoiDung;
        this.mKhoanTien = mKhoanTien;
        this.mNgayGio = mNgayGio;
        this.mCustomNgayGio = mCustomNgayGio;
        this.mCheck = mCheck;
    }

    public ChiTieu(int mID, String mNoiDung, int mKhoanTien) {
        this.mID = mID;
        this.mNoiDung = mNoiDung;
        this.mKhoanTien = mKhoanTien;
    }

    public ChiTieu(int mID, String mNoiDung, int mKhoanTien, String mNgayGio, String mCustomNgayGio) {
        this.mID = mID;
        this.mNoiDung = mNoiDung;
        this.mKhoanTien = mKhoanTien;
        this.mNgayGio = mNgayGio;
        this.mCustomNgayGio = mCustomNgayGio;
    }

    public ChiTieu(String mNoiDung, int mKhoanTien, String mNgayGio, String mCustomNgayGio, int Check) {
        this.mNoiDung = mNoiDung;
        this.mKhoanTien = mKhoanTien;
        this.mNgayGio = mNgayGio;
        this.mCustomNgayGio = mCustomNgayGio;
        this.mCheck = Check;
    }

    public int getmCheck() {
        return mCheck;
    }

    public void setmCheck(int mCheck) {
        this.mCheck = mCheck;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public String getmNoiDung() {
        return mNoiDung;
    }

    public void setmNoiDung(String mNoiDung) {
        this.mNoiDung = mNoiDung;
    }

    public int getmKhoanTien() {
        return mKhoanTien;
    }

    public void setmKhoanTien(int mKhoanTien) {
        this.mKhoanTien = mKhoanTien;
    }

    public String getmNgayGio() {
        return mNgayGio;
    }

    public void setmNgayGio(String mNgayGio) {
        this.mNgayGio = mNgayGio;
    }

    public String getmCustomNgayGio() {
        return mCustomNgayGio;
    }

    public void setmCustomNgayGio(String mCustomNgayGio) {
        this.mCustomNgayGio = mCustomNgayGio;
    }

    //todo*-> sắp xếp theo id
    public static Comparator<ChiTieu> AZ_ChiTieu = new Comparator<ChiTieu>() {
        @Override
        public int compare(ChiTieu o1, ChiTieu o2) {
            return o2.getmID() - o1.getmID();
        }
    };
}
