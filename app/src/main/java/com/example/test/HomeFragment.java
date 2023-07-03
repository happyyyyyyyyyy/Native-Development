package com.example.test;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.test.room.DogData;
import com.example.test.room.DogDataDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


public class HomeFragment extends Fragment implements onListItemSelectedInterface {
    private DogDataDatabase db;
    RecyclerView recyclerView;
    Adapter adapter;
    Context ct;
    Call<ArrayList<DogDto>> call;
    ArrayList<DogDto> arrayList;

    DogDto dogInfo;
    ArrayList<DogDto> result;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ct = container.getContext();
        db = Room.databaseBuilder(ct, DogDataDatabase.class, "DogData").allowMainThreadQueries().build(); //db 빌드
        recyclerView = v.findViewById(R.id.dog_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)); // 상하 스크롤
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)); // 좌우 스크롤

        adapter = new Adapter(ct, this);


        //room에 저장된 dogData를 recyclerView에 set
        List<DogData> dogDataList = db.getDogDao().getAll(); //DB에 있는 Data를 List에 저장
        int i =0;
        //for문을 통해 DogDto 객체에 저장
        for(DogData one : dogDataList){
            DogDto dogInfo = new DogDto(one.id, one.name, one.bredFor,
                    "Stubborn, Curious, Playful, Adventurous, Active, Fun-loving", 12, false, R.drawable.unselected_bookmark_icon, i);
            Image img = new Image();
            img.setUrl(one.img);
            dogInfo.setImage(img);
            Log.d("TAG", "onCreateView: " + dogInfo.image.getUrl());
            i++;
            adapter.setArrayData(dogInfo); //adapter에 set
        }
//        for(int i = 0; i<100; i++){
//            DogDto dogInfo = new DogDto(i, "Affenpinscher"+i, "Small rodent hunting, lapdog",
//                    "Stubborn, Curious, Playful, Adventurous, Active, Fun-loving", 12, false, R.drawable.unselected_bookmark_icon, i);
//            adapter.setArrayData(dogInfo);
//        }

        recyclerView.setAdapter(adapter);

        return v;
    }

    //RecyclerView 클릭 시 이벤트 처리 부분
    @Override
    public void onItemSelected(View v, int position, ArrayList<DogDto> arrayList, ImageButton button, TextView text_name) {
        Toast.makeText(getContext(), "position" + position, Toast.LENGTH_SHORT).show();
        if(arrayList.get(position).getBookmark_check()){
            arrayList.get(position).setBookmark_img(R.drawable.unselected_bookmark_icon);
            Log.d("TAG", "북마크 이미지 제거 " + arrayList.get(position).getBookmark_img());
            arrayList.get(position).setBookmark_check(false);
            Toast.makeText(getContext(), "북마크 제거" + position, Toast.LENGTH_SHORT).show();
        }
        else{
            arrayList.get(position).setBookmark_img(R.drawable.selected_bookmark_icon);
            Log.d("TAG", "북마크 이미지 추가 " + arrayList.get(position).getBookmark_img());
            arrayList.get(position).setBookmark_check(true);
            Toast.makeText(getContext(), "북마크 추가" + position, Toast.LENGTH_SHORT).show();
        }

        //북마크 DB 체크 다 하고


        //RecyclerView 업데이트
        adapter.notifyItemChanged(position);
    }
}