package com.example.test.fragment;

import static android.app.Activity.RESULT_OK;

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

import com.example.test.BookmarkDB;
import com.example.test.R;
import com.example.test.activity.InformationActivity;
import com.example.test.dto.DogDto;
import com.example.test.dto.Image;
import com.example.test.room.DogData;
import com.example.test.room.DogDataDatabase;

import java.util.ArrayList;
import java.util.List;

import Interface.onListItemSelectedInterface;
import adapter.HomeAdapter;
import retrofit2.Call;


public class HomeFragment extends Fragment implements onListItemSelectedInterface {
    RecyclerView recyclerView;
    HomeAdapter homeAdapter;
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

        homeAdapter = new HomeAdapter(ct, this);
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
            if(db.getDogDao().checkData(one.id))
                dogInfo.setBookmark_img(R.drawable.selected_bookmark_icon);
            Log.d("TAG", "onCreateView: " + dogInfo.getImage().getUrl());
            i++;
            apiDataList.add(dogInfo);

        }
        homeAdapter.setItems(apiDataList);
        recyclerView.setAdapter(homeAdapter);
        
        //searchView Event 구현
        searchList = new ArrayList<>();

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
                    List<DogDto> tempList = new ArrayList<>();
                    for (DogDto dog : apiDataList) {
                        if (db.getDogDao().checkData(dog.getId()))
                            dog.setBookmark_img(R.drawable.selected_bookmark_icon);
                        else
                            dog.setBookmark_img(R.drawable.unselected_bookmark_icon);
                        tempList.add(dog);
                    }
                    homeAdapter.setItems(tempList);
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
        boolean checkData = false; // 북마크 상태 저장 변수 true -> 북마크 O false -> 북마크 X
        BookmarkDB bookmarkDB = new BookmarkDB(arrayList, position, ct);
        checkData = bookmarkDB.dbCheck(); // 데이터가 북마크 되어 있는 상태인지 확인

        if (checkData) { // 북마크에 저장 되어 있음 -> 삭제 작업
            arrayList.get(position).setBookmark_img(R.drawable.unselected_bookmark_icon);
            Log.d("TAG", "북마크 이미지 제거 " + arrayList.get(position).getBookmark_img());
            bookmarkDB.updateBookmark();
            arrayList.get(position).setBookmark_check(false);
            Toast.makeText(getContext(), "북마크 제거" + position, Toast.LENGTH_SHORT).show();
        } else { // 북마크에 저장 되어 있지 않음 -> 추가 작업
            arrayList.get(position).setBookmark_img(R.drawable.selected_bookmark_icon);
            Log.d("TAG", "북마크 이미지 추가 " + arrayList.get(position).getBookmark_img());
            bookmarkDB.updateBookmark();
            arrayList.get(position).setBookmark_check(true);
            Toast.makeText(getContext(), "북마크 추가" + position, Toast.LENGTH_SHORT).show();
        }

        //RecyclerView 업데이트
        homeAdapter.notifyItemChanged(position);
    }

    @Override
    public void changeScreen(int id, String imgUrl, int position) {
        Intent intent = new Intent(ct, InformationActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("imgUrl", imgUrl);
        intent.putExtra("position", position);
        startActivityForResult(intent, 1);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // 새로고침 작업 수행
            ArrayList<DogDto> bookmarkInfo = new ArrayList<>();
            int i = 0;
            for(DogDto dog : homeAdapter.getArrayList2()){
                bookmarkInfo.add(new DogDto(dog));
                bookmarkInfo.get(i++).setImage(dog.getImage());
            }

            if(db.getDogDao().checkData(data.getIntExtra("id", -1))){
                bookmarkInfo.get(data.getIntExtra("position", -1)).setBookmark_img(R.drawable.selected_bookmark_icon);
            }
            else{
                bookmarkInfo.get(data.getIntExtra("position", -1)).setBookmark_img(R.drawable.unselected_bookmark_icon);
            }
            Log.d("TAG", "onActivityResult: 실행?" + data.getIntExtra("position", -1));
            homeAdapter.setItems(bookmarkInfo);
            homeAdapter.notifyItemChanged(data.getIntExtra("position", -1));
        }
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
            if(db.getDogDao().checkData(one.id))
                searchData.setBookmark_img(R.drawable.selected_bookmark_icon);
            i++;
            searchList.add(searchData);
        }
        homeAdapter.setItems(searchList);
    }

}