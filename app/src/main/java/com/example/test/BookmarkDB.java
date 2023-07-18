package com.example.test;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.test.dto.DogDto;
import com.example.test.room.DogDataDatabase;

import java.util.ArrayList;

public class BookmarkDB {

    private final DogDataDatabase db;
    ArrayList<DogDto> dataList;
    int position;
    int i = 0;

    public BookmarkDB(ArrayList<DogDto> dataList, int position, Context context) {
        this.dataList = dataList;
        this.position = position;
        Log.d("TAG", "디비 포지션" + position);
        db = Room.databaseBuilder(context, DogDataDatabase.class, "DogData").allowMainThreadQueries().build(); //db 빌드
    }


    public boolean dbCheck() {
        // 저장 되어 있지 않음
        return db.getDogDao().checkData(dataList.get(position).getId()); // 저장 되어 있음
    }

    public void updateBookmark() {
        Log.d("TAG", "updateBookmark: 업데이트 " + db.getDogDao().checkData(dataList.get(position).getId()));
        db.getDogDao().updateBookmarkCheck(!db.getDogDao().checkData(dataList.get(position).getId()), dataList.get(position).getId());
    }
}
