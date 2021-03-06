package com.example.chitiu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chitiu.Database.DataAnimation;
import com.example.chitiu.Database.DataHinh;
import com.example.chitiu.Object.IdHinh;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity_Setting extends AppCompatActivity {
    Toolbar mToolbarSetting;
    ViewPager mViewPagerSetting;
    CircleIndicator mCircle_indicatorSetting;
    RadioButton Setting_TrenSuong, Setting_DuoiLen, Setting_TraiQua, Setting_PhaiQua;
    DataAnimation dataAnimation;
    DataHinh dataHinh;

    AdapterHinh adapterHinh;
    List<IdHinh> mIdHinhs;
    Timer mTimer;

    Dialog dialog;      //dialog setphoto
    int Check = 0, REQUEST_CODE_IMAGE = 123;
    String Link_Photo = "";
    ImageView Set_Photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setting);
        Anhxa();

        //todo*-> G???i ?????n h??m h??? tr??? tool bar
        setSupportActionBar(mToolbarSetting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //todo*-> check hi???u ???ng l??u trong database
        CheckAnimation();

        //todo*-> CircleIndicator set auto load anh
        mIdHinhs = GetHinh();
        adapterHinh.setmIdHinhs(mIdHinhs);
        //set adapter cho ViewPager
        mViewPagerSetting.setAdapter(adapterHinh);
        //set ViewPager cho CircleIndicator
        mCircle_indicatorSetting.setViewPager(mViewPagerSetting);
        adapterHinh.registerDataSetObserver(mCircle_indicatorSetting.getDataSetObserver());
        autoSlideImg();//auto Slide
    }

    private void Anhxa(){
        mToolbarSetting = findViewById(R.id.mToolbarSetting);
        mViewPagerSetting = findViewById(R.id.mViewPagerSetting);
        mCircle_indicatorSetting = findViewById(R.id.mCircle_indicatorSetting);
        Setting_TrenSuong = findViewById(R.id.Setting_TrenSuong);
        Setting_DuoiLen = findViewById(R.id.Setting_DuoiLen);
        Setting_TraiQua = findViewById(R.id.Setting_TraiQua);
        Setting_PhaiQua = findViewById(R.id.Setting_PhaiQua);
        dataAnimation = new DataAnimation(this);
        dataHinh = new DataHinh(this);
        adapterHinh = new AdapterHinh(this, dataHinh);
    }

    //todo*-> S??? ki???n n??t home toobar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    //todo*-> B???t s??? ki???n Onclick Setting
    public void OnclickSetting(View view){
        switch (view.getId()){
            case R.id.Setting_TrenSuong:
                dataAnimation.SuaData(4);
                break;
            case R.id.Setting_DuoiLen:
                dataAnimation.SuaData(3);
                break;
            case R.id.Setting_TraiQua:
                dataAnimation.SuaData(1);
                break;
            case R.id.Setting_PhaiQua:
                dataAnimation.SuaData(2);
                break;

            case R.id.Setting_ThemAnh:
                if (dataHinh.Check()){
                    DialogXN();
                }else DialogAddPhoto();
                break;
            case R.id.Setting_XoaAnh:
                XoaHinh();
                break;

                //S??? ki???n dialog set photo
            case R.id.Set_Photo:
                //READ_EXTERNAL_STORAGE: xin quy???n ?????c folder
                ActivityCompat.requestPermissions(
                        MainActivity_Setting.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_IMAGE);
                break;
            case R.id.Huy_Photo:
                dialog.cancel();
                break;
            case R.id.OK_Photo:
                ThemHinh(Link_Photo);
                dialog.cancel();
                break;
        }
    }

    //todo*-> check hi???u ???ng l??u trong database
    private void CheckAnimation(){
        int CheckAnimation = dataAnimation.LayData();
        if (CheckAnimation == 1){
            Setting_TraiQua.setChecked(true);
        }else if (CheckAnimation == 2){
            Setting_PhaiQua.setChecked(true);
        }else if (CheckAnimation == 3){
            Setting_DuoiLen.setChecked(true);
        }else {
            Setting_TrenSuong.setChecked(true);
        }
    }

    //todo*-> Get Hinh
    private List<IdHinh> GetHinh(){
        //add h??nh v??o List
        List<IdHinh> idHinhs = new ArrayList<>();
        if (dataHinh.Check()){
            idHinhs.add(new IdHinh(R.drawable.img_ql_chovay));
            idHinhs.add(new IdHinh(R.drawable.img_ql_thunhap));
            idHinhs.add(new IdHinh(R.drawable.img_ql_tieutien));
            idHinhs.add(new IdHinh(R.drawable.img_ql_tietkiem));
        }else {
            idHinhs = dataHinh.AllHinh();
        }
        return idHinhs;
    }
    //todo*-> function auto tr?????c (Slide) Image
    private void autoSlideImg(){
        if (mIdHinhs == null || mIdHinhs.isEmpty() || mViewPagerSetting == null){
            return;
        }
        if (mTimer == null){
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //Khai b??o 1 Handler
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //khai b??o 1 bi???n l???y gtr??? hi???n t???i c???a item ViewPager
                        int idImg = mViewPagerSetting.getCurrentItem();
                        //t???ng size c???a mIdHinhs
                        int tongSize = mIdHinhs.size() - 1;
                        if (idImg < tongSize){
                            //check n???u nh??? h??n th?? ta t??ng l??n 1 cho item ViewPager
                            idImg++;
                            mViewPagerSetting.setCurrentItem(idImg);
                        }else {
                            //ng?????c l???i n???u = ho???c l???n h??n th?? Slide l???i t??? 0
                            mViewPagerSetting.setCurrentItem(0);
                        }
                    }
                });
            }
        },2000, 3000);
        //sau 2s th?? ch???y , v?? m???i l???n Slide Img trong 3s
    }

    //todo*-> Dialog h???i x??c nh???n x??a h??nh m???t ?????nh ????? th??m h??nh thi???c l???p
    private void DialogXN(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Th??ng b??o!");
        dialog.setMessage("X??c nh???n th??m h??nh m???i l?? x??a c??c h??nh m???t ?????nh ??i.");
        dialog.setNegativeButton("H???y", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DialogAddPhoto();
            }
        });
        dialog.show();
    }

    //todo*-> Dialog th??m h??nh
    private void DialogAddPhoto(){
        Check = 0;
        Link_Photo = "";
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_photo);
        Set_Photo = dialog.findViewById(R.id.Set_Photo);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    //todo*-> Xin quy???n folder
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_IMAGE && grantResults.length > 0 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_IMAGE);
        }else Toast.makeText(this, "B???n ch??a c???p quy???n", Toast.LENGTH_SHORT).show();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override//todo*-> L???y uri
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            Link_Photo = uri.toString();
            Glide.with(this).load(uri).into(Set_Photo);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void ThemHinh(String s){
        dataHinh.Them(s);
        mIdHinhs.clear();
        mIdHinhs = GetHinh();
        adapterHinh.setmIdHinhs(mIdHinhs);
        mViewPagerSetting.setAdapter(adapterHinh);
        MainActivity_DanhSach.mIdHinhs.clear();
        MainActivity_DanhSach.mIdHinhs = MainActivity_DanhSach.GetHinh();
        MainActivity_DanhSach.adapterHinh.setmIdHinhs(MainActivity_DanhSach.mIdHinhs);
        MainActivity_DanhSach.mViewPager.setAdapter(MainActivity_DanhSach.adapterHinh);
    }

    private void XoaHinh(){
        if (dataHinh.Check()){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Th??ng b??o!");
            dialog.setMessage("B???n ch??a thi???c l???p h??nh");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog.show();
        }else {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_item_xoa_hinh);
            Window window = dialog.getWindow();
            if (window == null){
                return;
            }
            //Bo tr??n v?? set v??? tr?? hi???n th??? dialog
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
            windowLayoutParams.gravity = Gravity.CENTER; //hi???n th??? ??? gi???a
            window.setAttributes(windowLayoutParams);

            ImageButton Dlg_Xoa_Exit = dialog.findViewById(R.id.Dlg_Xoa_Exit);
            RecyclerView Dlg_Xoa_ItemHinh = dialog.findViewById(R.id.Dlg_Xoa_ItemHinh);
            AdapterXoaHinh adapterXoaHinh = new AdapterXoaHinh(this);
            List<IdHinh> idHinhList = GetHinh();

            Dlg_Xoa_Exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

            //todo*-> onClick x??a h??nh
            adapterXoaHinh.setOnClick(new OnClickXoaHinh() {
                @Override
                public void onClick(IdHinh idHinh) {
                    if (dataHinh.Xoa(idHinh.getmID()) > 0){
                        if (!dataHinh.Check()){
                            List<IdHinh> list = GetHinh();
                            adapterXoaHinh.setmIdHinhs(list);

                            //set l???i
                            mIdHinhs.clear();
                            mIdHinhs = GetHinh();
                            adapterHinh.setmIdHinhs(mIdHinhs);
                            mViewPagerSetting.setAdapter(adapterHinh);
                            MainActivity_DanhSach.mIdHinhs.clear();
                            MainActivity_DanhSach.mIdHinhs = MainActivity_DanhSach.GetHinh();
                            MainActivity_DanhSach.adapterHinh.setmIdHinhs(MainActivity_DanhSach.mIdHinhs);
                            MainActivity_DanhSach.mViewPager.setAdapter(MainActivity_DanhSach.adapterHinh);
                        }else {
                            dialog.cancel();
                            //set l???i
                            mIdHinhs.clear();
                            mIdHinhs = GetHinh();
                            adapterHinh.setmIdHinhs(mIdHinhs);
                            mViewPagerSetting.setAdapter(adapterHinh);
                            MainActivity_DanhSach.mIdHinhs.clear();
                            MainActivity_DanhSach.mIdHinhs = MainActivity_DanhSach.GetHinh();
                            MainActivity_DanhSach.adapterHinh.setmIdHinhs(MainActivity_DanhSach.mIdHinhs);
                            MainActivity_DanhSach.mViewPager.setAdapter(MainActivity_DanhSach.adapterHinh);
                        }
                    }
                }
            });

            //s??? d???ng GridLayoutManager ????? set d???ng Gird ra cho RecyclerView
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            Dlg_Xoa_ItemHinh.setLayoutManager(gridLayoutManager);
            adapterXoaHinh.setmIdHinhs(idHinhList);
            Dlg_Xoa_ItemHinh.setAdapter(adapterXoaHinh);

            Toast.makeText(this, "Click v??o h??nh ????? x??a", Toast.LENGTH_SHORT).show();
            dialog.show();
        }
    }
}