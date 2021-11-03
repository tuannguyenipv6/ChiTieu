package com.example.chitiu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitiu.Database.DataChiTieu;
import com.example.chitiu.Object.ChiTieu;
import com.ramotion.foldingcell.FoldingCell;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterChiTieu extends RecyclerView.Adapter<AdapterChiTieu.ViewHolderChiTieu> {
    List<ChiTieu> mChiTieus;
    DataChiTieu dataChiTieu;
    MainActivity_DanhSach mainActivity_danhSach;
    int KEY_Check = 0;

    public AdapterChiTieu(DataChiTieu dataChiTieu, MainActivity_DanhSach mainActivity_danhSach, int Key_CHECK) {
        this.dataChiTieu = dataChiTieu;
        this.mainActivity_danhSach = mainActivity_danhSach;
        this.KEY_Check = Key_CHECK;
    }

    public void setmChiTieus(List<ChiTieu> mChiTieus) {
        this.mChiTieus = mChiTieus;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderChiTieu onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chi_tieu, parent, false);
        return new ViewHolderChiTieu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterChiTieu.ViewHolderChiTieu holder, int position) {
        ChiTieu chiTieu = mChiTieus.get(position);

        //todo*-> set hình lên layout
        if (chiTieu.getmCheck() == 1){
            //chi tiêu
            holder.itCT_Hinh.setImageResource(R.drawable.img_ql_tieutien);
            holder.itCT_Hinh1.setImageResource(R.drawable.img_ql_tieutien);
        }else if (chiTieu.getmCheck() == 2){
            //thu nhâp
            holder.itCT_Hinh.setImageResource(R.drawable.img_ql_thunhap);
            holder.itCT_Hinh1.setImageResource(R.drawable.img_ql_thunhap);
        }else if (chiTieu.getmCheck() == 3){
            //cho vay
            holder.itCT_Hinh.setImageResource(R.drawable.img_ql_chovay);
            holder.itCT_Hinh1.setImageResource(R.drawable.img_ql_chovay);
        }else {
            //tiết kiệm
            holder.itCT_Hinh.setImageResource(R.drawable.img_ql_tietkiem);
            holder.itCT_Hinh1.setImageResource(R.drawable.img_ql_tietkiem);
        }

        //gán các thông tin còn lại
        holder.itCT_NoiDung.setText(RutGon(chiTieu.getmNoiDung()));
        holder.itCT_NoiDung1.setText(chiTieu.getmNoiDung());
        holder.itCT_VND.setText(chiTieu.getmKhoanTien() + " VND");
        holder.itCT_VND1.setText(chiTieu.getmKhoanTien() + " VND");
        holder.itCT_Ngay.setText(chiTieu.getmNgayGio());
        holder.itCT_Ngay1.setText(chiTieu.getmNgayGio());

        //todo*-> sự kiện click hiện thông tin với
        holder.mFoldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mFoldingCell.toggle(false);
            }
        });

        //todo*-> Xóa, sửa
        holder.itCT_More.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogXoaSua(chiTieu);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mChiTieus != null){
            return mChiTieus.size();
        }
        return 0;
    }

    public class ViewHolderChiTieu extends RecyclerView.ViewHolder {
        FoldingCell mFoldingCell;
        CircleImageView itCT_Hinh;
        ImageView itCT_Hinh1;
        TextView itCT_NoiDung, itCT_NoiDung1, itCT_VND, itCT_VND1, itCT_Ngay, itCT_Ngay1;
        ImageButton itCT_More;
        public ViewHolderChiTieu(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            itCT_Hinh = itemView.findViewById(R.id.itCT_Hinh);
            itCT_Hinh1 = itemView.findViewById(R.id.itCT_Hinh1);
            itCT_NoiDung = itemView.findViewById(R.id.itCT_NoiDung);
            itCT_NoiDung1 = itemView.findViewById(R.id.itCT_NoiDung1);
            itCT_VND = itemView.findViewById(R.id.itCT_VND);
            itCT_VND1 = itemView.findViewById(R.id.itCT_VND1);
            itCT_Ngay = itemView.findViewById(R.id.itCT_Ngay);
            itCT_Ngay1 = itemView.findViewById(R.id.itCT_Ngay1);
            mFoldingCell = itemView.findViewById(R.id.mFoldingCell);
            itCT_More = itemView.findViewById(R.id.itCT_More);
        }
    }
    private String RutGon(String s){
        String RutGon = s;
        String[] parts = RutGon.split(" ");
        if (parts.length > 4){
            RutGon = parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[3] + " " + parts[4] + "...";
            Log.d("BBBB", RutGon);
        }
        return RutGon;
    }

    //todo*-> dialog xóa, sửa
    private void DialogXoaSua(ChiTieu chiTieu){
        AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity_danhSach);
        dialog.setTitle("Thông báo!");
        dialog.setMessage("Bạn muốn thực hiện?");
        dialog.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataChiTieu.DeleteChiTieu(chiTieu.getmID());
                Toast.makeText(mainActivity_danhSach, "đã xóa", Toast.LENGTH_SHORT).show();
                mainActivity_danhSach.mCollapsingToolbarLayout.setTitle("DS Tháng này");
                Date date = new Date();
                mChiTieus = dataChiTieu.AllChiTieuThang(KEY_Check, date);
                Collections.sort(mChiTieus, ChiTieu.AZ_ChiTieu);
                mainActivity_danhSach.CheckAnimation();
                //set lại data cho RV
                mainActivity_danhSach.adapter.setmChiTieus(mChiTieus);
            }
        });
        dialog.setNegativeButton("Sửa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mainActivity_danhSach.DialogSua(chiTieu);
            }
        });
        dialog.show();
    }


}
