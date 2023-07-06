package com.example.test.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test.R;
import com.example.test.fragment.BookmarkFragment;
import com.example.test.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    //바텀 네비게이션 객체 변수
    BottomNavigationView btnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //바텀 네비게이션 객체를 받아옴
        btnv = findViewById(R.id.bottomNav);

        //Fragment매니저를 불러와서
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new HomeFragment()).commit();

        //바텀 네비게이션에서 아이템이 클릭 됐을 때의 리스너를 설정
        btnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.home)
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new HomeFragment()).commit();
                else if (menuItem.getItemId() == R.id.bookmark){
                    //네트워크 연결 안되면 경고창
                    boolean isConnected = SplashActivity.isNetworkConnected(MainActivity.this);
                    if (!isConnected)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("네트워크 연결이 원활하지 않아 \n이미지가 로딩 되지 않을 수도 있습니다.")
                                    .setCancelable(false)
                                    .setPositiveButton("CANCEL", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new BookmarkFragment()).commit();
                                        }
                                    }).show();
                        }
                        else
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new BookmarkFragment()).commit();
                }
                    return true;
            }
        });
    }
}