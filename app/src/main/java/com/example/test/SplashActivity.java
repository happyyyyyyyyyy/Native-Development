package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.test.room.DogData;
import com.example.test.room.DogDataDatabase;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private DogDataDatabase db;
    long delayTime = 5000L; //스플래시 화면 지연 시간 정의
    int delayCnt = 1;
    TextView text_loading;

    Call<ArrayList<DogDto>> call;
    ArrayList<DogDto> arrayList;
    Intent intent;
    DogDto dogInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        db = Room.databaseBuilder(this, DogDataDatabase.class, "DogData").allowMainThreadQueries().build(); //db 빌드
        //인플레이팅
        text_loading = findViewById(R.id.text_loading);
        ImageView imageView = findViewById(R.id.icon);

        //스플래시 화면에 있는 원형 아이콘 처리
        imageView.setImageResource(R.drawable.splash_icon);

        //delayTime 이후 메인 화면으로 넘어감
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent = new Intent(SplashActivity.this, MainActivity.class);
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

        //Dog API에서 값 가져오기
        arrayList = new ArrayList<DogDto>();
        call = retrofit_client.getApiService().test_api_get2("live_mtPILdV1Vd1b0kcRxjsB1KVMOOHipR18xuUthvy0Y8gH9ZGvNW69RrCip5CErxth", "500");
        call.enqueue(new Callback<ArrayList<DogDto>>() {
            @Override
            public void onResponse(Call<ArrayList<DogDto>> call, Response<ArrayList<DogDto>> response) {
                ArrayList<DogDto> result = response.body(); //API에서 받아온 값 result에 담기
                Log.d("TAG", "onResponse: JSON확인 " + result);

                db.getDogDao().deleteAll(); //DB 초기화(Update 부분 구현 시 삭제)
                Log.d("TAG", "onResponse: 테이블" + db.getDogDao().getAll().isEmpty());
                DogData dogData = new DogData();
                
                //Dog API에서 받아온 값 room의 DogData Entity가 null이면 저장 아니면 update
                if(db.getDogDao().getAll().isEmpty()){
                    Log.d("TAG", "onResponse: 크기" + result.size());
                    for(int i = 0; i<result.size(); i++){
                        dogData.id = result.get(i).getId();
                        dogData.name = result.get(i).getName();
                        dogData.bredFor = result.get(i).getBred_for();
                        db.getDogDao().insert(dogData);
                        Log.d("TAG", "Room 확인 " + result.get(i).getId());
                    }
                }else{
                 // 변경된 Data Update 하는 부분
                    ;
                }



            }

            @Override
            public void onFailure(Call<ArrayList<DogDto>> call, Throwable t) {

            }
        });

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