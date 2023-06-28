package com.example.test;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public interface onListItemSelectedInterface {
    void onItemSelected(View v, int position, ArrayList<DogDto> arrayList, ImageButton button, TextView text_name);
}
