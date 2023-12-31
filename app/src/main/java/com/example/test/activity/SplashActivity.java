package com.example.test.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.test.BuildConfig;
import com.example.test.R;
import com.example.test.api.RetrofitClient;
import com.example.test.dto.DogDto;
import com.example.test.room.DogData;
import com.example.test.room.DogDataDatabase;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static final int DELAY_COUNT = 3;
    private static final int DELAY_TIME = 5; //스플래시 화면 지연 시간 정의

    private TextView loadingText;
    private Intent intent;
    private DogDataDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        db = Room.databaseBuilder(this, DogDataDatabase.class, "DogData").allowMainThreadQueries().build(); //db 빌드

        initialize();

        //delayTime 동안 Loading text 변경 Thread 생성
        LoadingThread loading = new LoadingThread();
        loading.start();
    }

    private void initialize(){
        loadingText = findViewById(R.id.textLoading);
        intent = new Intent(SplashActivity.this, MainActivity.class);
    }

    //로딩 화면 애니메이션 thread 구현
    public class LoadingThread extends Thread {
        public void run() {
            //애니메이션 작업 스레드 시작
            Thread animationThread = new Thread(new Runnable() {
                int delayCnt = 1;
                int i = 0;

                @Override
                public void run() {
                    try {
                        while (i <= DELAY_TIME && !Thread.currentThread().isInterrupted()) {
                            runOnUiThread(() -> {
                                switch (delayCnt) {
                                    case 1:
                                        loadingText.setText(String.format("%s .", getString(R.string.splash_loading_text)));
                                        break;
                                    case 2:
                                        loadingText.setText(String.format("%s . .", getString(R.string.splash_loading_text)));
                                        break;
                                    case 3:
                                        loadingText.setText(String.format("%s . . .", getString(R.string.splash_loading_text)));
                                        break;
                                }
                            });
                            delayCnt = (delayCnt % DELAY_COUNT) + 1;

                            // 스레드에게 수행시킬 동작들 구현
                            Thread.sleep(500); // 0.5초간 Thread를 잠재운다
                            i++;
                            Log.d("TAG", "시간" + i);
                        }
                        if (!Thread.currentThread().isInterrupted()) {
                            Log.d("TAG", "onFinish: 타이머가 끝남");
                            if (db.getDogDao().getAll().isEmpty()) { //DB에 저장된 데이터가 없으면 앱 종료
                                Toast.makeText(SplashActivity.this, R.string.splash_no_data_text, Toast.LENGTH_SHORT).show();
                                finish();
                            } else { //DB에 저장된 데이터로 앱 실행
                                startActivity(intent);
                                finish();
                            }
                        }
                    } catch (InterruptedException e) {
                        Log.d("TAG", "애니메이션 스레드 종료");
                        Thread.currentThread().interrupt(); // 인터럽트 상태를 유지
                    }
                }
                });
            animationThread.start();
                // 네트워크 체크 후 데이터 가져오는 작업 스레드 시작
                Thread dataThread = new Thread(() -> {
                    //네트워크 체크 후 로직 실행
                    if (isNetworkConnected(SplashActivity.this)) { // 인터넷 연결 X
                        Log.d("TAG", "run: 인터넷 연결이 원활하지 않아 db의 데이터로 호출");
                    } else { // 인터넷 연결 O
                        getApiData();
                        animationThread.interrupt();
                    }
                });
            dataThread.start();
            }
        }

        // 인터넷 연결 체크
        public static boolean isNetworkConnected(Activity activity) {
            ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            Network currentNetwork = manager.getActiveNetwork();
            return currentNetwork == null;
        }

        //api
        public void getApiData() {
            //Dog API에서 값 가져오기
            Call<ArrayList<DogDto>> call = RetrofitClient.getApiService().getApiData(BuildConfig.DOG_API_KEY, "500");
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<DogDto>> call, @NonNull Response<ArrayList<DogDto>> response) {
                    ArrayList<DogDto> result = response.body(); //API에서 받아온 값 result에 담기

                    //Dog API에서 받아온 값 room의 DogData Entity가 null이면 저장 아니면 update
                    if (db.getDogDao().getAll().isEmpty()) {
                        db.getDogDao().deleteAll(); //DB 초기화
                        for (int i = 0; i < Objects.requireNonNull(result).size(); i++) {
                            insertApiDataIntoDB(result.get(i));
                        }
                    } else {
                        for (int i = 0; i < Objects.requireNonNull(result).size(); i++) {
                            if (db.getDogDao().checkData2(result.get(i).getId()) == 0) {
                                insertApiDataIntoDB(result.get(i));
                            }
                        }
                    }
                    //로딩 끝내고 main으로 화면 전환
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<DogDto>> call, @NonNull Throwable t) {
                    Log.d("TAG", "onFailure: API 호출 실패");
                }
            });
        }

        //DB에 받아온 API Data Insert
        public void insertApiDataIntoDB(DogDto dogDto) {
            DogData dogData = new DogData();
            dogData.id = dogDto.getId();
            dogData.name = dogDto.getName();
            dogData.bredFor = dogDto.getBred_for();
            dogData.img = dogDto.getImage().getUrl();
            dogData.bookmarkCheck = false;
            db.getDogDao().insert(dogData);
        }
    }