package com.example.chitiu;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.chitiu.Database.DataHinh;
import com.example.chitiu.Object.IdHinh;

import java.util.List;

public class AdapterHinh extends PagerAdapter {
    private Context context;
    private List<IdHinh> mIdHinhs;
    private DataHinh dataHinh;

    public void setmIdHinhs(List<IdHinh> mIdHinhs) {
        this.mIdHinhs = mIdHinhs;
        notifyDataSetChanged();
    }

    public AdapterHinh(Context context, DataHinh dataHinh) {
        this.context = context;
        this.dataHinh = dataHinh;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_hinh, container, false);
        //khai báo và ánh xạ view
        ImageView imgHinh = (ImageView) view.findViewById(R.id.itHinh);
        IdHinh idHinh = mIdHinhs.get(position);
        if (idHinh != null){//check idHinh null hay k
            if (!dataHinh.Check()){
                Uri uri = Uri.parse(idHinh.getmValue());
                Glide.with(context).load(uri).into(imgHinh);
            }else {
                Glide.with(context).load(idHinh.getmID()).into(imgHinh);
            }
        }
        //add view vào ViewGroup
        container.addView(view);
        return view;
    }
    @Override
    public int getCount() {
        if (mIdHinhs != null){
            //nếu có giá trị thì return vè đúng số lg item trong mIdHinh
            return mIdHinhs.size();
        }
        return 0;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;//return view == object
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //xóa view từ ViewGroup
        container.removeView((View) object);
    }
}
