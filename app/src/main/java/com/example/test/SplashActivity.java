package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    long delayTime = 10000L; //스플래시 화면 지연 시간 정의
    int delayCnt = 1;
    TextView text_loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //인플레이팅
        text_loading = findViewById(R.id.text_loading);
        ImageView imageView = findViewById(R.id.icon);

        //스플래시 화면에 있는 원형 아이콘 처리
        imageView.setImageResource(R.drawable.splash_icon);

        //delayTime 이후 메인 화면으로 넘어감
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, delayTime);

        //delayTime 동안 Loading text 변경 애니메이션을 위한 타이머 생성
        CountDownTimer loadingTimer = new CountDownTimer(delayTime,500) {
            @Override
            public void onTick(long millisUntilFinished) {
                switch (delayCnt){
                    case 1: text_loading.setText("Loading ."); break;
                    case 2: text_loading.setText("Loading . ."); break;
                    case 3: text_loading.setText("Loading . . ."); break;
                }
                if(delayCnt == 3)
                    delayCnt = 1;
                else
                    delayCnt++;
            }

            @Override
            public void onFinish() {
                Log.d("TAG", "onFinish: 타이머가 끝남");
            }
        };
        loadingTimer.start(); //로딩을 위한 타이머 시작
        
        
        //아래 코드는 splash 화면이 안나온 상태로 delayTime이 지나가고 Main 화면으로 넘어감..
//        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//        try{
//            Thread.sleep(delayTime);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        startActivity(intent);

    }
}