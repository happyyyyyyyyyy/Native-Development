package com.example.test.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test.R;
import com.example.test.fragment.BookmarkFragment;
import com.example.test.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private boolean isConnected;
    //바텀 네비게이션 객체 변수
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //바텀 네비게이션 객체를 받아옴
        bottomNavigationView = findViewById(R.id.bottomNav);

        //Fragment매니저를 불러와서
        getSupportFragmentManager().beginTransaction().add(R.id.mainFrame, new HomeFragment()).commit();

        //바텀 네비게이션에서 아이템이 클릭 됐을 때의 리스너를 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.home)
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new HomeFragment()).commit();
                else if (menuItem.getItemId() == R.id.bookmark) {
                    //네트워크 확인 thread 실행
                    NetworkThread networkThread = new NetworkThread();
                    networkThread.start();
                    try {
                        //네트워크 확인 전까지 기다림
                        networkThread.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    //네트워크 연결 안되면 경고창
                    if (!isConnected) {
                        showNetworkWarningDialog();
                    }
                    //북마크로 화면 전환
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new BookmarkFragment()).commit();
                }
                return true;
            }
        });
    }

    public class NetworkThread extends Thread {
        public void run() {
            isConnected = SplashActivity.isNetworkConnected(MainActivity.this);
            Log.d("TAG", "run: 네트워크 확인");
        }
    }

    private void showNetworkWarningDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.main_dialog_text)
                .setPositiveButton("OK", null).show();
    }
}