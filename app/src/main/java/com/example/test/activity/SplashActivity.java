package com.example.test.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.test.R;
import com.example.test.api.retrofit_client;
import com.example.test.dto.DogDto;
import com.example.test.room.DogData;
import com.example.test.room.DogDataDatabase;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    long delayTime = 5000L; //스플래시 화면 지연 시간 정의
    int delayCnt = 1;
    TextView text_loading;
    Call<ArrayList<DogDto>> call;
    ArrayList<DogDto> arrayList;
    Intent intent;
    DogDto dogInfo;
    private DogDataDatabase db;

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

        //delayTime 동안 Loading text 변경 애니메이션을 위한 타이머 생성
        CountDownTimer loadingTimer = new CountDownTimer(delayTime, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                switch (delayCnt) {
                    case 1:
                        text_loading.setText("Loading .");
                        break;
                    case 2:
                        text_loading.setText("Loading . .");
                        break;
                    case 3:
                        text_loading.setText("Loading . . .");
                        break;
                }
                if (delayCnt == 3)
                    delayCnt = 1;
                else
                    delayCnt++;
            }
            @Override
            public void onFinish() {
                Log.d("TAG", "onFinish: 타이머가 끝남");
                if (db.getDogDao().getAll().isEmpty()){
                    Toast.makeText(SplashActivity.this, "저장된 데이터가 없으므로 \n네트워크 연결 후 다시 실행해주세요.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                }
            }
        };
        loadingTimer.start(); //로딩을 위한 타이머 시작

        //네트워크 체크 후 로직 실행
        disconnectHandler handler = new disconnectHandler(loadingTimer);
        handler.run();

        //아래 코드는 splash 화면이 안나온 상태로 delayTime이 지나가고 Main 화면으로 넘어감..
//        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//        try{
//            Thread.sleep(delayTime);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        startActivity(intent);

    }

    //인터넷 연결 체크 Handler 구현
    private class disconnectHandler implements Runnable
    {
        private CountDownTimer loadingTimer;
        public disconnectHandler(CountDownTimer loadingTimer) {
            this.loadingTimer = loadingTimer;
        }

        @Override
        public void run()
        {
            boolean isConnected = isNetworkConnected(SplashActivity.this);
            if (!isConnected)//인터넷 연결 X
            {
                Log.d("TAG", "run: 인터넷 연결이 원활하지 않아 db의 데이터로 호출");
            }
            else//인터넷 연결 O
            {
                //Dog API에서 값 가져오기
                arrayList = new ArrayList<DogDto>();
                call = retrofit_client.getApiService().test_api_get2("live_mtPILdV1Vd1b0kcRxjsB1KVMOOHipR18xuUthvy0Y8gH9ZGvNW69RrCip5CErxth", "500");
                call.enqueue(new Callback<ArrayList<DogDto>>() {
                    @Override
                    public void onResponse(Call<ArrayList<DogDto>> call, Response<ArrayList<DogDto>> response) {
                        ArrayList<DogDto> result = response.body(); //API에서 받아온 값 result에 담기
                        Log.d("TAG", "onResponse: JSON확인 " + result);

                        Log.d("TAG", "onResponse: 테이블" + db.getDogDao().getAll().isEmpty());
                        DogData dogData = new DogData();

                        //Dog API에서 받아온 값 room의 DogData Entity가 null이면 저장 아니면 update
                        if (db.getDogDao().getAll().isEmpty()) {
                            Log.d("TAG", "onResponse: 크기" + result.size());
                            db.getDogDao().deleteAll(); //DB 초기화
                            for (int i = 0; i < result.size(); i++) {
                                dogData.id = result.get(i).getId();
                                dogData.name = result.get(i).getName();
                                dogData.bredFor = result.get(i).getBred_for();
                                dogData.img = result.get(i).getImage().getUrl();
                                dogData.bookmarkCheck = false;
                                db.getDogDao().insert(dogData);
                                Log.d("TAG", "Room 확인 " + result.get(i).getId());
                            }
                        }else{

                            for (int i = 0; i < result.size(); i++) {
                                Log.d("TAG", "체크 " + db.getDogDao().checkData2(result.get(i).getId()));
                                if(db.getDogDao().checkData2(result.get(i).getId()) == 0){
                                    dogData.id = result.get(i).getId();
                                    dogData.name = result.get(i).getName();
                                    dogData.bredFor = result.get(i).getBred_for();
                                    dogData.img = result.get(i).getImage().getUrl();
                                    dogData.bookmarkCheck = false;
                                    db.getDogDao().insert(dogData);
                                    Log.d("TAG", "Room 확인2 " + result.get(i).getId());
                                }
                            }
                        }
                        loadingTimer.cancel();
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onFailure(Call<ArrayList<DogDto>> call, Throwable t) {
                        Log.d("TAG", "onFailure: API 호출 실패");
                    }
                });
            }
        }
    }

    //인터넷 연결 체크
    public static boolean isNetworkConnected(Activity activity) {
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network currentNetwork = manager.getActiveNetwork();
        if(currentNetwork == null){
            return false;
        }
        else{
            return true;
        }

    }
}