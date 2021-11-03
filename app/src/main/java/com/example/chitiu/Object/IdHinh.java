package com.example.chitiu.Object;

public class IdHinh {
    private int mID;
    private String mValue;

    public IdHinh(int mID) {
        this.mID = mID;
    }

    public IdHinh(String mValue) {
        this.mValue = mValue;
    }

    public IdHinh(int mID, String mValue) {
        this.mID = mID;
        this.mValue = mValue;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public String getmValue() {
        return mValue;
    }

    public void setmValue(String mValue) {
        this.mValue = mValue;
    }
}
