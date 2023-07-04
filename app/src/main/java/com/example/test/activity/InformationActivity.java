package com.example.test.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.test.R;
import com.example.test.api.retrofit_client;
import com.example.test.dto.DogDto;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InformationActivity extends AppCompatActivity {
    Call<DogDto> call;
    ImageView dogImg;
    TextView dogBredFor;
    TextView dogLifeSpan;
    TextView dogTemperant;
    TextView dogWeightHeight;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        dogImg = findViewById(R.id.dogImg);
        dogBredFor = findViewById(R.id.bredForData);
        dogLifeSpan = findViewById(R.id.lifeSpanData);
        dogTemperant = findViewById(R.id.temperantData);
        dogWeightHeight = findViewById(R.id.weightAndheightData);
        
        //액션바 뒤로가기 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        Intent infoIntent = getIntent();
        Toast.makeText(this, "아이디 : " + infoIntent.getIntExtra("id", 0), Toast.LENGTH_SHORT).show();
        int id = infoIntent.getIntExtra("id", 0);
        url = infoIntent.getStringExtra("imgUrl");

        call = retrofit_client.getApiService().test_api_get("live_mtPILdV1Vd1b0kcRxjsB1KVMOOHipR18xuUthvy0Y8gH9ZGvNW69RrCip5CErxth", id + "");
        call.enqueue(new Callback<DogDto>() {
            @Override
            public void onResponse(Call<DogDto> call, Response<DogDto> response) {

                Log.d("TAG", "onResponse: " + response.body().getBred_for());
                if (Objects.isNull(response.body().getBred_for()) || response.body().getBred_for().isEmpty()) {
                    dogBredFor.setText(". . . . ");
                }
                else
                    dogBredFor.setText(response.body().getBred_for());
                dogLifeSpan.setText(response.body().getLifeSpan());
                dogTemperant.setText(response.body().getTemperament());
                toolbar.setTitle(response.body().getName());

                //Weight, Height의 평균 구하기
                String avgWeight = response.body().getWeight().getMetric();
                String avgHeight = response.body().getHeight().getMetric();
                if (response.body().getWeight().getMetric().contains("-"))
                    avgWeight = response.body().getWeight().getWeightAvg().toString();
                if (response.body().getHeight().getMetric().contains("-"))
                    avgHeight = response.body().getHeight().getHeightAvg().toString();


                dogWeightHeight.setText("avg. " + avgWeight + "kg / " + avgHeight + "cm");
                Glide.with(InformationActivity.this)
                        .load(url) // 이미지 소스 로드
                        .thumbnail(0.1f) // 실제 이미지 크기 중 30%만 먼저 가져와서 흐릿하게 보여줌
                        .into(dogImg); // 이미지 띄울 view 선택
            }

            @Override
            public void onFailure(Call<DogDto> call, Throwable t) {

            }
        });

    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case android.R.id.home: //툴바 뒤로가기 버튼 눌렸을 때 동작
                finish ();
                return true;
            default:
                return super.onOptionsItemSelected (item);
        }
    }
}