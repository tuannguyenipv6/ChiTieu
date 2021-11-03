package com.example.chitiu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chitiu.Database.DataChiTieu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity_BieuDo extends AppCompatActivity {
    RelativeLayout mRelativeLayout;
    ScaleGestureDetector scaleGestureDetector;

    TextView Th1, Th2, Th3, Th4, Th5, Th6, Th7, Th8, Th9, Th10, Th11, Th12;
    TextView txTh1, txTh2, txTh3, txTh4, txTh5, txTh6, txTh7, txTh8, txTh9, txTh10, txTh11, txTh12;
    TextView VNDTh1, VNDTh2, VNDTh3, VNDTh4, VNDTh5, VNDTh6, VNDTh7, VNDTh8, VNDTh9, VNDTh10, VNDTh11, VNDTh12, BD_TenBD;
    View Ngang3, Ngang2;
    DataChiTieu dataChiTieu;

    int max = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bieu_do);
        Anhxa();
        //todo*-> Phóng to thu nhỏ
        scaleGestureDetector = new ScaleGestureDetector(this, new MyGesture());
        mRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        Intent intent = getIntent();
        int Key_Check = intent.getIntExtra("Key_Check", 0);
        int Key_Check_CT = intent.getIntExtra("Key_Check_CT", 0);

        //check hiện view theo id truyền vào Tháng, Tuần, ngày.
        AnView(Key_Check);

        //todo*-> Set Biểu đồ
        if (Key_Check == 3){
            //Tháng 12 tháng
            SetHeiThang(Key_Check_CT);
        }else if (Key_Check == 2){
            //Tuần 5 tuần
            SetHeiTuan(Key_Check_CT);
        }else {
            //Ngày 7 ngày
            SetHeiNgay(Key_Check_CT);
        }

    }

    private void Anhxa(){
        dataChiTieu = new DataChiTieu(this);
        mRelativeLayout = findViewById(R.id.mRelativeLayout);
        BD_TenBD = findViewById(R.id.BD_TenBD);
        Th1 = findViewById(R.id.Moth1);
        Th2 = findViewById(R.id.Moth2);
        Th3 = findViewById(R.id.Moth3);
        Th4 = findViewById(R.id.Moth4);
        Th5 = findViewById(R.id.Moth5);
        Th6 = findViewById(R.id.Moth6);
        Th7 = findViewById(R.id.Moth7);
        Th8 = findViewById(R.id.Moth8);
        Th9 = findViewById(R.id.Moth9);
        Th10 = findViewById(R.id.Moth10);
        Th11 = findViewById(R.id.Moth11);
        Th12 = findViewById(R.id.Moth12);

        VNDTh1 = findViewById(R.id.VNDth1);
        VNDTh2 = findViewById(R.id.VNDth2);
        VNDTh3 = findViewById(R.id.VNDth3);
        VNDTh4 = findViewById(R.id.VNDth4);
        VNDTh5 = findViewById(R.id.VNDth5);
        VNDTh6 = findViewById(R.id.VNDth6);
        VNDTh7 = findViewById(R.id.VNDth7);
        VNDTh8 = findViewById(R.id.VNDth8);
        VNDTh9 = findViewById(R.id.VNDth9);
        VNDTh10 = findViewById(R.id.VNDth10);
        VNDTh11 = findViewById(R.id.VNDth11);
        VNDTh12 = findViewById(R.id.VNDth12);
        Ngang3 = findViewById(R.id.Ngang3);
        Ngang2 = findViewById(R.id.Ngang2);

        txTh1 = findViewById(R.id.txMoth1);
        txTh2 = findViewById(R.id.txMoth2);
        txTh3 = findViewById(R.id.txMoth3);
        txTh4 = findViewById(R.id.txMoth4);
        txTh5 = findViewById(R.id.txMoth5);
        txTh6 = findViewById(R.id.txMoth6);
        txTh7 = findViewById(R.id.txMoth7);
        txTh8 = findViewById(R.id.txMoth8);
        txTh9 = findViewById(R.id.txMoth9);
        txTh10 = findViewById(R.id.txMoth10);
        txTh11 = findViewById(R.id.txMoth11);
        txTh12 = findViewById(R.id.txMoth12);
    }

    //todo*-> funsion dùng để phóng to thu nhỏ
    class MyGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        //khai báo 3 biến lll: biến lấy giá trị phóng ban đầu, biến giữ giá trị phóng, và biến cuôi cùng kết thúc phóng
        float scale = 1.0F, onScaleStart = 0, onScaleEnd = 0;
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            //gọi lại biến bắt đầu "scale" nhân cho tỉ lệ mà người dùng phóng "detector.getScaleFactor();"
            scale *= detector.getScaleFactor();
            mRelativeLayout.setScaleX(scale); //chiều rộn
            mRelativeLayout.setScaleY(scale); //chiều cao
            return super.onScale(detector);
        }
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            //lần lần phóng tiếp theo t cũng bắt nó tự độ phóng trk đó và gán vào onScaleStart
            onScaleStart = scale;
            return super.onScaleBegin(detector);
        }
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            //tương tự khi phóng kết thúc thì gán lại vào onScaleEnd
            onScaleEnd = scale;
            super.onScaleEnd(detector);
        }
    }

    //todo*-> check ẩn View theo Tháng, Tuần, ngày.
    private void AnView(int i){
        if (i == 2){
            //Tuần trong tháng có 4 tuần
            BD_TenBD.setText("Biểu đồ Tuần trong tháng.");
            txTh1.setText("T.1");
            txTh2.setText("T.2");
            txTh3.setText("T.3");
            txTh4.setText("T.4");
            txTh5.setText("T.5");

            Th6.setVisibility(View.INVISIBLE);
            Th7.setVisibility(View.INVISIBLE);
            Th8.setVisibility(View.INVISIBLE);
            Th9.setVisibility(View.INVISIBLE);
            Th10.setVisibility(View.INVISIBLE);
            Th11.setVisibility(View.INVISIBLE);
            Th12.setVisibility(View.INVISIBLE);

            VNDTh6.setVisibility(View.INVISIBLE);
            VNDTh7.setVisibility(View.INVISIBLE);
            VNDTh8.setVisibility(View.INVISIBLE);
            VNDTh9.setVisibility(View.INVISIBLE);
            VNDTh10.setVisibility(View.INVISIBLE);
            VNDTh11.setVisibility(View.INVISIBLE);
            VNDTh12.setVisibility(View.INVISIBLE);

            txTh6.setVisibility(View.INVISIBLE);
            txTh7.setVisibility(View.INVISIBLE);
            txTh8.setVisibility(View.INVISIBLE);
            txTh9.setVisibility(View.INVISIBLE);
            txTh10.setVisibility(View.INVISIBLE);
            txTh11.setVisibility(View.INVISIBLE);
            txTh12.setVisibility(View.INVISIBLE);

            Ngang2.setVisibility(View.INVISIBLE);
            Ngang3.setVisibility(View.INVISIBLE);
        }else if (i == 1){
            //ngày trong tuần có 7
            BD_TenBD.setText("Biểu đồ thứ trong Tuần.");
            txTh1.setText("Th.2");
            txTh2.setText("Th.3");
            txTh3.setText("Th.4");
            txTh4.setText("Th.5");
            txTh5.setText("Th.6");
            txTh6.setText("Th.7");
            txTh7.setText("CN");

            Th8.setVisibility(View.INVISIBLE);
            Th9.setVisibility(View.INVISIBLE);
            Th10.setVisibility(View.INVISIBLE);
            Th11.setVisibility(View.INVISIBLE);
            Th12.setVisibility(View.INVISIBLE);

            VNDTh8.setVisibility(View.INVISIBLE);
            VNDTh9.setVisibility(View.INVISIBLE);
            VNDTh10.setVisibility(View.INVISIBLE);
            VNDTh11.setVisibility(View.INVISIBLE);
            VNDTh12.setVisibility(View.INVISIBLE);


            txTh8.setVisibility(View.INVISIBLE);
            txTh9.setVisibility(View.INVISIBLE);
            txTh10.setVisibility(View.INVISIBLE);
            txTh11.setVisibility(View.INVISIBLE);
            txTh12.setVisibility(View.INVISIBLE);

            Ngang3.setVisibility(View.INVISIBLE);
        }
    }

    //todo*-> Set chiều cao của textview thành biểu đồ
    private void setDataChar(int gtrHT, TextView Th, TextView VND){
        double Height = ((double) gtrHT * 1000) / max;
        Th.setHeight((int) Height);
        VND.setText(gtrHT + " VNĐ");
    }

    //todo*-> Set Chiều cao cho Tuần trong tháng
    private void SetHeiTuan(int Check){
        Date date = new Date();
        int[] mMax = dataChiTieu.DataTuan(date, Check);
        for (int num: mMax) {
            if(max < num){
                max = num;
            }
        }
        //set chiều cao:
        setDataChar(mMax[0], Th1, VNDTh1);
        setDataChar(mMax[1], Th2, VNDTh2);
        setDataChar(mMax[2], Th3, VNDTh3);
        setDataChar(mMax[3], Th4, VNDTh4);
        setDataChar(mMax[4], Th5, VNDTh5);
    }

    //todo*-> Set Chiều cao cho Ngày trong tuần
    private void SetHeiNgay(int Check){
        Date date = new Date();
        int[] mMax = dataChiTieu.DataNgay(date, Check);
        for (int num: mMax) {
            if(max < num){
                max = num;
            }
        }
        //set chiều cao:
        setDataChar(mMax[0], Th1, VNDTh1);
        setDataChar(mMax[1], Th2, VNDTh2);
        setDataChar(mMax[2], Th3, VNDTh3);
        setDataChar(mMax[3], Th4, VNDTh4);
        setDataChar(mMax[4], Th5, VNDTh5);
        setDataChar(mMax[5], Th6, VNDTh6);
        setDataChar(mMax[6], Th7, VNDTh7);
    }

    //todo*-> Set Chiều cao cho Ngày trong tuần
    private void SetHeiThang(int Check){
        Date date = new Date();
        int[] mMax = dataChiTieu.DataThang(date, Check);
        for (int num: mMax) {
            if(max < num){
                max = num;
            }
        }
        //set chiều cao:
        setDataChar(mMax[0], Th1, VNDTh1);
        setDataChar(mMax[1], Th2, VNDTh2);
        setDataChar(mMax[2], Th3, VNDTh3);
        setDataChar(mMax[3], Th4, VNDTh4);
        setDataChar(mMax[4], Th5, VNDTh5);
        setDataChar(mMax[5], Th6, VNDTh6);
        setDataChar(mMax[6], Th7, VNDTh7);
        setDataChar(mMax[7], Th8, VNDTh8);
        setDataChar(mMax[8], Th9, VNDTh9);
        setDataChar(mMax[9], Th10, VNDTh10);
        setDataChar(mMax[10], Th11, VNDTh11);
        setDataChar(mMax[11], Th12, VNDTh12);
    }

    public void OnClickBieuDo(View view){
        switch (view.getId()){
            case R.id.BD_Lich:
                XemLich();
                break;
            case R.id.BD_Thoat:
                finish();
                break;
        }
    }

    private void XemLich(){
        //tạo 1 Calendar lấy ngày mặt định cho dialog
        Calendar calendar = Calendar.getInstance();
        int Ngay = calendar.get(Calendar.DATE);
        int Thang = calendar.get(Calendar.MONTH);
        int Nam = calendar.get(Calendar.YEAR);
        //Khởi tạo DatePickerDialog (cần truyền vào 5 tham số là: Context, Sự kiện click, Năm, Tháng, Ngày)
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth); //gán ngày đã chọn vào calendar
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("W, EEEE, dd MMMM yyyy"); //định dạng
                String s ="Tuần " + simpleDateFormat.format(calendar.getTime());
                Toast.makeText(MainActivity_BieuDo.this, s, Toast.LENGTH_SHORT).show();
            }
        }, Nam, Thang, Ngay);

        datePickerDialog.show();
    }
}
