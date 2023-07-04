package com.example.test.activity;

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
                else if (menuItem.getItemId() == R.id.bookmark)
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new BookmarkFragment()).commit();
                return true;
            }
        });
    }
}