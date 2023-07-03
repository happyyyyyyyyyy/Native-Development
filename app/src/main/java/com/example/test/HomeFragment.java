package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
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
    RecyclerView recyclerView;
    Adapter adapter;
    Context ct;
    Call<ArrayList<DogDto>> call;
    ArrayList<DogDto> apiDataList;
    ArrayList<DogDto> searchList;

    DogDto dogInfo;
    ArrayList<DogDto> result;
    private DogDataDatabase db;

    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ct = container.getContext();
        db = Room.databaseBuilder(ct, DogDataDatabase.class, "DogData").allowMainThreadQueries().build(); //db 빌드

        searchView = v.findViewById(R.id.search);
        recyclerView = v.findViewById(R.id.dog_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)); // 상하 스크롤
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)); // 좌우 스크롤

        adapter = new Adapter(ct, this);
        apiDataList = new ArrayList<>();

        //room에 저장된 dogData를 recyclerView에 set
        List<DogData> dogDataList = db.getDogDao().getAll(); //DB에 있는 Data를 List에 저장
        int i = 0;

        //for문을 통해 DogDto 객체에 저장
        for (DogData one : dogDataList) {
            DogDto dogInfo = new DogDto(one.id, one.name, one.bredFor,
                    "Stubborn, Curious, Playful, Adventurous, Active, Fun-loving", "12", false, R.drawable.unselected_bookmark_icon, i);
            Image img = new Image();
            img.setUrl(one.img);
            dogInfo.setImage(img);
            Log.d("TAG", "onCreateView: " + dogInfo.image.getUrl());
            i++;
            apiDataList.add(dogInfo);

        }
        adapter.setItems(apiDataList);
        recyclerView.setAdapter(adapter);
        
        //searchView Event 구현
        searchList = new ArrayList<>();
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast.makeText(ct, "x 클릭" , Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onClose: 닫기");
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //검색 버튼 누를 때 호출
                search(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("TAG", "onQueryTextChange: " + newText);
                //검색창이 비어 있으면 전체 데이터 출력
                if(newText.isEmpty()){
                    adapter.setItems(apiDataList);
                }
                //검색 창에서 글자가 변경이 일어날 때마다 호출
                else {
                    search(newText);
                }
                return false;
            }
        });
        return v;
    }

    //RecyclerView 클릭 시 이벤트 처리 부분
    @Override
    public void onItemSelected(View v, int position, ArrayList<DogDto> arrayList) {
        Toast.makeText(getContext(), "position" + position, Toast.LENGTH_SHORT).show();
        if (arrayList.get(position).getBookmark_check()) {
            arrayList.get(position).setBookmark_img(R.drawable.unselected_bookmark_icon);
            Log.d("TAG", "북마크 이미지 제거 " + arrayList.get(position).getBookmark_img());
            arrayList.get(position).setBookmark_check(false);
            Toast.makeText(getContext(), "북마크 제거" + position, Toast.LENGTH_SHORT).show();
        } else {
            arrayList.get(position).setBookmark_img(R.drawable.selected_bookmark_icon);
            Log.d("TAG", "북마크 이미지 추가 " + arrayList.get(position).getBookmark_img());
            arrayList.get(position).setBookmark_check(true);
            Toast.makeText(getContext(), "북마크 추가" + position, Toast.LENGTH_SHORT).show();
        }

        //북마크 DB 체크 다 하고


        //RecyclerView 업데이트
        adapter.notifyItemChanged(position);
    }

    @Override
    public void changeScreen(int id, String imgUrl) {
        Intent intent = new Intent(ct, InformationActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("imgUrl", imgUrl);
        startActivity(intent);
    }

    
    //검색 메소드 구현
    public void search(String query){
        searchList.clear();
        List<DogData> searchDataList = db.getDogDao().search("%"+query+"%", query);
        int i = 0;
        for(DogData one : searchDataList){
            DogDto searchData = new DogDto(one.id, one.name, one.bredFor,
                    "Stubborn, Curious, Playful, Adventurous, Active, Fun-loving", "12", false, R.drawable.unselected_bookmark_icon, i);
            Image img = new Image();
            img.setUrl(one.img);
            searchData.setImage(img);
            i++;
            searchList.add(searchData);
        }
        adapter.setItems(searchList);
    }

}