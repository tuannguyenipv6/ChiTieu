package com.example.chitiu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import com.example.chitiu.Database.DataAnimation;
import com.example.chitiu.Database.DataChiTieu;
import com.example.chitiu.Database.DataHinh;
import com.example.chitiu.Object.ChiTieu;
import com.example.chitiu.Object.IdHinh;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity_DanhSach extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    //todo*-> Bố cục ứng dụng
    AppBarLayout mAppBarLayout;                                 //AppBarLayout: bố cục thanh ứng dụng
    CollapsingToolbarLayout mCollapsingToolbarLayout;           //CollapsingToolbarLayout: thu gọn bố cục thanh công cụ
    Toolbar mToolbar;                                           //của tk androidx,Toolbar: thanh công cụ
    FloatingActionButton mFloatingActionButton;                 //là nút mỡ rộng, FloatingActionButton: nút hành động nỗi
    boolean isExpanded = true;                                  //trạng thái của FloatingActionButton

    //todo*-> set RecyclerView
    RecyclerView mRecyclerView;
    AdapterChiTieu adapter;
    List<ChiTieu> mChiTieus;

    //todo*-> Menu, Database, KEY int, SwipeRefreshLayout, dialog mỡ rộng, DataAnimation, DataHinh
    Menu mMenu;
    DataChiTieu dataChiTieu;
    int KEY_Check = 0;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Dialog dialog;
    DataAnimation dataAnimation;
    public static DataHinh dataHinh;

    //todo*-> dialog Sửa
    TextView dlg_SuaNgay;
    Date SetTimeDate = null;

    //todo*-> chạy hình
    public static ViewPager mViewPager;
    CircleIndicator mCircleIndicator;
    public static AdapterHinh adapterHinh;
    public static List<IdHinh> mIdHinhs;
    Timer mTimer; //sử dụng timer để auto next Image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_danh_sach);
        Anhxa();

        //todo*-> Gọi đến hàm hiện nút mở rộng
        initToolbar();

        //todo*-> Gọi đến hàm khởi tạo item cho RecyclerView
        initRecyclerView(KEY_Check);

        //todo*-> Gọi đến hàm khởi tạo các hoạt ảnh công cụ
        initToolbarAnimations();

        //todo*-> Gọi đến hàm bắt sự kiện nút mỡ rộng
        onClickBtnAdd();

        //todo*-> Gọi đến hàm sự kiện SwipeRefreshLayout
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //set màu cho SwipeRefreshLayout
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_toolbar));

        //todo*-> CircleIndicator set auto load anh
        mIdHinhs = GetHinh();
        adapterHinh.setmIdHinhs(mIdHinhs);
        //set adapter cho ViewPager
        mViewPager.setAdapter(adapterHinh);
        //set ViewPager cho CircleIndicator
        mCircleIndicator.setViewPager(mViewPager);
        adapterHinh.registerDataSetObserver(mCircleIndicator.getDataSetObserver());
        autoSlideImg();//auto Slide

    }
    private void Anhxa() {
        //todo*-> Get Intent
        Intent intent = getIntent();
        KEY_Check = intent.getIntExtra("KEY_CHECK", 0);

        mAppBarLayout = findViewById(R.id.mAppBarLayout);
        mCollapsingToolbarLayout = findViewById(R.id.mCollapsingToolbarLayout);
        mToolbar = findViewById(R.id.mToolbar);
        mFloatingActionButton = findViewById(R.id.mFloatingActionButton);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mSwipeRefreshLayout = findViewById(R.id.mSwipeRefreshLayout);
        dataChiTieu = new DataChiTieu(this);
        adapter = new AdapterChiTieu(dataChiTieu, this, KEY_Check);
        dataAnimation = new DataAnimation(this);
        dataHinh = new DataHinh(this);
        mCircleIndicator = (CircleIndicator) findViewById(R.id.mCircle_indicator);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        adapterHinh = new AdapterHinh(this, dataHinh);
    }

    //todo*-> Onclick Main_DS
    public void OnClickMainDS(View view){
        switch (view.getId()){
            //bắt sự kiện các nút trong dialog mỡ rộng
            case R.id.mr_DS_ALL:
                setChiTieu(KEY_Check, 3);
                break;
            case R.id.mr_DS_Tuan:
                setChiTieu(KEY_Check, 2);
                break;
            case R.id.mr_DS_HmNay:
                setChiTieu(KEY_Check, 1);
                break;
            case R.id.mr_BD_Thang:
                StarActivityBieuDo(3);
                dialog.cancel();
                break;
            case R.id.mr_BD_Tuan:
                StarActivityBieuDo(2);
                dialog.cancel();
                break;
            case R.id.mr_BD_Ngay:
                StarActivityBieuDo(1);
                dialog.cancel();
                break;
        }
    }

    private void StarActivityBieuDo(int i){
        Intent intent = new Intent(MainActivity_DanhSach.this, MainActivity_BieuDo.class);
        intent.putExtra("Key_Check", i);
        intent.putExtra("Key_Check_CT", KEY_Check);
        startActivity(intent);
    }

    //todo*-> set lại ds chi tiêu theo HmNay || Tuần này || Tháng này.
    private void setChiTieu(int Check, int CusNgay){
        Date date = new Date();
        CheckAnimation();
        if (CusNgay == 1){
            mChiTieus = dataChiTieu.AllChiTieuHmNay(Check, date);
            Collections.sort(mChiTieus, ChiTieu.AZ_ChiTieu);
            mCollapsingToolbarLayout.setTitle("DS Hôm nay");
        }else if (CusNgay == 2){
            mChiTieus = dataChiTieu.AllChiTieuTuan(Check, date);
            Collections.sort(mChiTieus, ChiTieu.AZ_ChiTieu);
            mCollapsingToolbarLayout.setTitle("DS Tuần này");
        }else{
            mChiTieus = dataChiTieu.AllChiTieu(Check);
            Collections.sort(mChiTieus, ChiTieu.AZ_ChiTieu);
            mCollapsingToolbarLayout.setTitle("DS Tất cả");
        }
        adapter.setmChiTieus(mChiTieus);
        dialog.cancel();
    }

    //todo*-> check animation
    public void CheckAnimation(){
        int CheckDataAnimation = dataAnimation.LayData();
        if (CheckDataAnimation == 1){
            LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animations_trai_sang_phai);
            mRecyclerView.setLayoutAnimation(layoutAnimationController);
        }else if (CheckDataAnimation == 2){
            LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animations_phai_sang_trai);
            mRecyclerView.setLayoutAnimation(layoutAnimationController);
        }else if (CheckDataAnimation == 3){
            LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animations_duoi_len);
            mRecyclerView.setLayoutAnimation(layoutAnimationController);
        }else {
            LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animations_tren_suong);
            mRecyclerView.setLayoutAnimation(layoutAnimationController);
        }
    }

    //todo*-> Hàm hiện nút mở rộng
    private void initToolbar(){
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//để hiện thị nút mở rộng
        }
    }

    //todo*-> Hàm khởi tạo item cho RecyclerView
    private void initRecyclerView(int Check){
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemViewCacheSize(20);
        Date date = new Date();
        mChiTieus = dataChiTieu.AllChiTieuThang(Check, date);
        Collections.sort(mChiTieus, ChiTieu.AZ_ChiTieu);            //sắp xếp lại theo id
        CheckAnimation();
        adapter.setmChiTieus(mChiTieus);
        mRecyclerView.setAdapter(adapter);
    }

    //todo*-> Khởi tạo các hoạt ảnh công cụ
    private void initToolbarAnimations(){
        mCollapsingToolbarLayout.setTitle("DS Tháng này");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_tien);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
//                int myColor = palette.getVibrantColor(getResources().getColor(R.color.color_toolbar));
                Resources res = getResources();
                int myColor = res.getColor(R.color.color_toolbar);
                mCollapsingToolbarLayout.setContentScrimColor(myColor);
                mCollapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.black_trans));
            }
        });
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) > 200){
                    isExpanded = false;
                }else isExpanded = true;
                invalidateOptionsMenu();
            }
        });
    }

    //todo*-> Hàm bắt sự kiện nút mỡ rộng
    private void onClickBtnAdd(){
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override//hàm bắt sự kiện nút mỡ rộng
            public void onClick(View v) {
                DialogDefault(KEY_Check);
            }
        });
    }

    //todo*-> Menu [ Khởi tạo menu, gán nút FloatingActionButton vào menu, bắt sự kiện menu ]
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;//gán menu ở phần khai báo bằng với menu ta tạo trong resoucre
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mMenu != null && (!isExpanded || mMenu.size() != 1)){
            //gán nút add (FloatingActionButton) vào menu
            mMenu.add("Headline").setIcon(R.drawable.ic_headline).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return super.onPrepareOptionsMenu(mMenu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.mnSetting:
                Intent intent = new Intent(MainActivity_DanhSach.this, MainActivity_Setting.class);
                startActivity(intent);
                return true;
        }

        //bắt sự kiện nút add (FloatingActionButton) khi đc gán vào menu
        if (item.getTitle() == "Headline"){
            DialogDefault(KEY_Check);
        }
        return super.onOptionsItemSelected(item);
    }

    //todo*-> dialog mỡ rộng
    private void DialogDefault(int Check){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_mo_rong);

        TextView mr_DS_ALL = dialog.findViewById(R.id.mr_DS_ALL);
        TextView mr_DS_Tuan = dialog.findViewById(R.id.mr_DS_Tuan);
        TextView mr_DS_HmNay = dialog.findViewById(R.id.mr_DS_HmNay);

        if (Check == 1){
            mr_DS_ALL.setText("D/S Tất cả Chi Tiêu");
            mr_DS_Tuan.setText("D/S Chi Tiêu Tuần này");
            mr_DS_HmNay.setText("D/S Chi Tiêu hôm nay");
        }else if (Check == 2){
            mr_DS_ALL.setText("D/S Tất cả Thu Nhập");
            mr_DS_Tuan.setText("D/S Thu Nhập Tuần này");
            mr_DS_HmNay.setText("D/S Thu Nhập hôm nay");
        }else if (Check == 3){
            mr_DS_ALL.setText("D/S Tất cả Cho Vay");
            mr_DS_Tuan.setText("D/S Cho Vay Tuần này");
            mr_DS_HmNay.setText("D/S Cho Vay hôm nay");
        }else {
            mr_DS_ALL.setText("D/S Tất cả Tiết Kiệm");
            mr_DS_Tuan.setText("D/S Tiết Kiệm Tuần này");
            mr_DS_HmNay.setText("D/S Tiết Kiệm hôm nay");
        }

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        //Bo tròn và set vị trí hiển thị dialog
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
        windowLayoutParams.gravity = Gravity.CENTER; //hiển thị ở giữa
        window.setAttributes(windowLayoutParams);
        dialog.show();
    }

    private void setDS(){
        mCollapsingToolbarLayout.setTitle("DS Tháng này");
        Date date = new Date();
        mChiTieus = dataChiTieu.AllChiTieuThang(KEY_Check, date);
        Collections.sort(mChiTieus, ChiTieu.AZ_ChiTieu);
        CheckAnimation();
        //set lại data cho RV
        adapter.setmChiTieus(mChiTieus);
    }

    //todo*-> sự kiện cho SwipeRefreshLayout load lại ChiTieu Tháng
    @Override
    public void onRefresh() {
        setDS();
        //vd cho nó load trong 2s với Handler
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //lệnh tắt SwipeRefreshLayout
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 700);//tắt sau 2s
    }

    //todo*-> dialog sửa ChiTieu
    public void DialogSua(ChiTieu chiTieu){
        Dialog dialogSuaCT = new Dialog(this);
        dialogSuaCT.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSuaCT.setContentView(R.layout.dialog_sua_chitieu);

        dlg_SuaNgay = dialogSuaCT.findViewById(R.id.dlg_SuaNgay);
        TextView dlg_SuaSoTien = dialogSuaCT.findViewById(R.id.dlg_SuaSoTien);
        TextView dlg_SuaNoiDung = dialogSuaCT.findViewById(R.id.dlg_SuaNoiDung);
        dlg_SuaNgay.setText(chiTieu.getmNgayGio());
        dlg_SuaSoTien.setText(chiTieu.getmKhoanTien() + "");
        dlg_SuaNoiDung.setText(chiTieu.getmNoiDung());

        TextView dlg_HuySua = dialogSuaCT.findViewById(R.id.dlg_HuySua);
        TextView dlg_OKSua = dialogSuaCT.findViewById(R.id.dlg_OKSua);

        dlg_SuaNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChonNgay();
            }
        });

        dlg_HuySua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSuaCT.cancel();
            }
        });
        dlg_OKSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SKhoanTien = dlg_SuaSoTien.getText().toString().trim();
                String NoiDung = dlg_SuaNoiDung.getText().toString().trim();
                if (SKhoanTien.length() > 0 && NoiDung.length() > 0){
                    int KhoanTien = Integer.parseInt(SKhoanTien);
                    if (SetTimeDate != null){
                        String NgayGio = dlg_SuaNgay.getText().toString().trim();
                        SimpleDateFormat formatter = new SimpleDateFormat("u/WW/MM/yyyy");
                        String CustomNgayGio = formatter.format(SetTimeDate);
                        Log.d("BBB", CustomNgayGio);
                        ChiTieu chiTieu1 = new ChiTieu(chiTieu.getmID(), NoiDung, KhoanTien, NgayGio, CustomNgayGio);
                        if (dataChiTieu.UpdateCoNgay(chiTieu1) > 0){
                            Toast.makeText(MainActivity_DanhSach.this, "Thành Công", Toast.LENGTH_SHORT).show();
                            setDS();
                            dialogSuaCT.cancel();
                        }else
                            Toast.makeText(MainActivity_DanhSach.this, "Thất bại", Toast.LENGTH_SHORT).show();
                    }else {
                        ChiTieu chiTieu1 = new ChiTieu(chiTieu.getmID(), NoiDung, KhoanTien);
                        if (dataChiTieu.UpdateKoNgay(chiTieu1) > 0){
                            Toast.makeText(MainActivity_DanhSach.this, "Thành công", Toast.LENGTH_SHORT).show();
                            setDS();
                            dialogSuaCT.cancel();
                        }else
                            Toast.makeText(MainActivity_DanhSach.this, "Thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Window window = dialogSuaCT.getWindow();
        if (window == null){
            return;
        }
        //Bo tròn và set vị trí hiển thị dialog
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
        windowLayoutParams.gravity = Gravity.CENTER; //hiển thị ở giữa
        window.setAttributes(windowLayoutParams);
        dialogSuaCT.show();
    }

    //todo*-> Chọn ngày
    private void ChonNgay(){
        //tạo 1 Calendar lấy ngày mặt định cho dialog
        Calendar calendar = Calendar.getInstance();
        int Ngay = calendar.get(Calendar.DATE);
        int Thang = calendar.get(Calendar.MONTH);
        int Nam = calendar.get(Calendar.YEAR);
        //Khởi tạo DatePickerDialog (cần truyền vào 5 tham số là: Context, Sự kiện click, Năm, Tháng, Ngày)
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new
                DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth); //gán ngày đã chọn vào calendar
                        SetTimeDate = calendar.getTime();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd/MM/yyyy"); //định dạng
                        dlg_SuaNgay.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, Nam, Thang, Ngay); //gán ngày mặt định của dialog khi show
        datePickerDialog.show();
    }

    //todo*-> function auto trược (Slide) Image
    private void autoSlideImg(){
        if (mIdHinhs == null || mIdHinhs.isEmpty() || mViewPager == null){
            return;
        }
        if (mTimer == null){
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //Khai báo 1 Handler
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //khai báo 1 biến lấy gtrị hiện tại của item ViewPager
                        int idImg = mViewPager.getCurrentItem();
                        //tổng size của mIdHinhs
                        int tongSize = mIdHinhs.size() - 1;
                        if (idImg < tongSize){
                            //check nếu nhỏ hơn thì ta tăng lên 1 cho item ViewPager
                            idImg++;
                            mViewPager.setCurrentItem(idImg);
                        }else {
                            //ngược lại nếu = hoặc lớn hơn thì Slide lại từ 0
                            mViewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        },2000, 3000);
        //sau 2s thì chạy , và mỗi lần Slide Img trong 3s
    }
    @Override//todo*-> Dừng tk timer
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null){//chéck xem mtime null hay k
            //nếu khác null ta dừng mTimer và gán lại = null
            mTimer.cancel();
            mTimer = null;
        }
    }

    //todo*-> Get Hinh
    public static List<IdHinh> GetHinh(){
        //add hình vào List
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
}
