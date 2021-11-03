package com.example.chitiu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.chitiu.Database.DataChiTieu;
import com.example.chitiu.Object.ChiTieu;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class MainActivity extends AppCompatActivity {
    RadioButton radio_ChiTieu;
    EditText edt_KhoanTien, edt_NoiDung;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private int CHECK_TIEN_SU_DUNG = 1;
    DataChiTieu dataChiTieu;

    //todo*-> quảng cáo
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();

        //todo*-> quảng cáo.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        //quảng cáo nhỏ
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //quảng cáo full
        AdRequest adRequest1 = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-9144843441540790/2662376415", adRequest1, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });

                mInterstitialAd = interstitialAd;
                Log.d("BBB-Ckeck1", "quảng cáo được tải");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.d("BBB-Ckeck2", loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });

        if (mInterstitialAd != null) {
            mInterstitialAd.show(MainActivity.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }

        //todo8-> set gtri ban đầu của RadioButton.
        radio_ChiTieu.setChecked(true);

        //todo*-> Bắt sự kiện void Nội dung.
        edt_NoiDung.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (edt_NoiDung.getRight() - edt_NoiDung.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        promptSpeechInput();
                        return true;
                    }
                }
                return false;
            }
        });
    }
    private void Anhxa(){
        radio_ChiTieu = findViewById(R.id.radio_ChiTieu);
        edt_KhoanTien = findViewById(R.id.edt_KhoanTien);
        edt_NoiDung = findViewById(R.id.edt_NoiDung);
        dataChiTieu = new DataChiTieu(this);
    }
    public void OnclickMain(View view){
        switch (view.getId()){
            //todo*->  4 btn Chi tiêu, Thu nhập, Cho vay, Tiết kiệm.
            case R.id.btn_ChiTieu:
                ChuyenActivity(1);
                break;
            case R.id.btn_ThuNhap:
                ChuyenActivity(2);
                break;
            case R.id.btn_ChoVay:
                ChuyenActivity(3);
                break;
            case R.id.btn_TietKiem:
                ChuyenActivity(4);
                break;

            //todo*->  4 RadioButton Chi tiêu, Thu nhập, Cho vay, Tiết kiệm.
            case R.id.radio_ChiTieu:
                CHECK_TIEN_SU_DUNG = 1;
                break;
            case R.id.radio_ThuNhap:
                CHECK_TIEN_SU_DUNG = 2;
                break;
            case R.id.radio_ChoVay:
                CHECK_TIEN_SU_DUNG = 3;
                break;
            case R.id.radio_TietKiem:
                CHECK_TIEN_SU_DUNG = 4;
                break;

                //todo*->  NÚT OK
            case R.id.OK_Main:
                InsertChiTieu();
                break;
        }
    }


    //todo*-> Sử dụng voice
    /**
     * Gọi dialog của google speech thông qua Intent
     * Một số action quan trọng trong Intent như
     * ACTION_RECOGNIZE_SPEECH, LANGUAGE_MODEL_FREE_FORM, EXTRA_PROMPT
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this,
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
    //Trả lại dữ liệu sau khi nhập giọng nói vào
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edt_NoiDung.setText(result.get(0));
                }
                break;
            }
        }
    }

    //todo*-> Insert dữ liệu nhập vào Database
    private void InsertChiTieu(){
        String sKhoanTien = edt_KhoanTien.getText().toString().trim();
        String NoiDung = edt_NoiDung.getText().toString().trim();
        if (sKhoanTien.length() > 0 && NoiDung.length() > 0){
            int KhoanTien = Integer.parseInt(sKhoanTien);
            Date date = new Date();

            //khởi tạo SimpleDateFormat: Thứ, Giờ:phút Ngày/Tháng/Năm
            SimpleDateFormat formatter = new SimpleDateFormat("E, HH:mm dd/MM/yyyy");
            String NgayGio = formatter.format(date);

            //format lại: [Ngày trong tuần]/[Tuần trong tháng]/[Tháng trong năm]/[Năm]
            formatter = new SimpleDateFormat("u/WW/MM/yyyy");
            String CustomNgayGio = formatter.format(date);

            ChiTieu chiTieu = new ChiTieu(NoiDung, KhoanTien, NgayGio, CustomNgayGio, CHECK_TIEN_SU_DUNG);
            dataChiTieu.ThemChiTieu(chiTieu);

            edt_KhoanTien.setText("");
            edt_NoiDung.setText("");
        }else Toast.makeText(this, "Nhập đủ thông tin", Toast.LENGTH_SHORT).show();
    }

    //todo*-> Chuyển màn hình danh sách
    private void ChuyenActivity(int Check){
        Intent intent = new Intent(MainActivity.this, MainActivity_DanhSach.class);
        intent.putExtra("KEY_CHECK", Check);
        startActivity(intent);
    }
}