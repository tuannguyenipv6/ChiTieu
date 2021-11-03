package com.example.chitiu;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chitiu.Object.IdHinh;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class AdapterXoaHinh extends RecyclerView.Adapter<AdapterXoaHinh.ViewHolderXoaHinh> {
    private List<IdHinh> mIdHinhs;
    Context context;

    public AdapterXoaHinh(Context context) {
        this.context = context;
    }

    public void setmIdHinhs(List<IdHinh> mIdHinhs) {
        this.mIdHinhs = mIdHinhs;
        notifyDataSetChanged();
    }

    //todo*-> Khai báo hàm onClick
    OnClickXoaHinh onClick;
    public void setOnClick(OnClickXoaHinh onClick) {
        this.onClick = onClick;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderXoaHinh onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xoa_hinh, parent, false);
        return new ViewHolderXoaHinh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterXoaHinh.ViewHolderXoaHinh holder, int position) {
        IdHinh idHinh = mIdHinhs.get(position);
        holder.itImg_Ma.setText("ID: " + idHinh.getmID());
        Uri uri = Uri.parse(idHinh.getmValue());
        Glide.with(context).load(uri).into(holder.itImg_Xoa);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onClick(idHinh);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mIdHinhs != null){
            return mIdHinhs.size();
        }
        return 0;
    }

    public class ViewHolderXoaHinh extends RecyclerView.ViewHolder {
        ImageView itImg_Xoa;
        TextView itImg_Ma;
        public ViewHolderXoaHinh(@NonNull @NotNull View itemView) {
            super(itemView);
            itImg_Ma = itemView.findViewById(R.id.itImg_Ma);
            itImg_Xoa = itemView.findViewById(R.id.itImg_Xoa);
        }
    }
}
