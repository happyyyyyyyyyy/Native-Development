package com.example.test;

import android.view.View;

import java.util.ArrayList;

public interface onListItemSelectedInterface {
    void onItemSelected(View v, int position, ArrayList<DogDto> arrayList);

    void changeScreen(int id, String imgUrl);
}
