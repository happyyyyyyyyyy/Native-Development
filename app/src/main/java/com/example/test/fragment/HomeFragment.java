package com.example.test.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.test.BookmarkDB;
import com.example.test.R;
import com.example.test.activity.InformationActivity;
import com.example.test.dto.DogDto;
import com.example.test.dto.DogImage;
import com.example.test.room.DogData;
import com.example.test.room.DogDataDatabase;

import java.util.ArrayList;
import java.util.List;

import Interface.OnListItemSelectListener;
import adapter.HomeAdapter;


public class HomeFragment extends Fragment implements OnListItemSelectListener {
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private Context ct;
    private View v;
    private DogDataDatabase db;
    private ArrayList<DogDto> apiDataList;
    private ArrayList<DogDto> searchList;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    private TextView noDataText;
    private SearchView searchView;
    private final int PAGE_SIZE = 15;
    private int pageNumber = 1;
    private int pageOffset;
    private boolean searchFlag = false;
    private boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);
        ct = container.getContext();
        db = Room.databaseBuilder(ct, DogDataDatabase.class, "DogData").allowMainThreadQueries().build(); //db 빌드

        initializeSearchView();
        initializeRecyclerView();
        setActivityResultLauncher();
        setDataRecyclerView();
        initializeNoDataTextView();

        return v;
    }

    private void initializeSearchView() {
        searchView = v.findViewById(R.id.search);
        searchList = new ArrayList<>();
        //searchView Event 구현
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {//검색 버튼 누를 때 호출
                searchFlag = true;
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {//검색창이 비어 있으면 전체 데이터 출력
                    searchbarEmpty();
                } else {//검색 창에서 글자가 변경이 일어날 때마다 호출
                    searchFlag = true;
                    search(newText);
                }
                return false;
            }
        });
    }

    private void initializeRecyclerView() {
        recyclerView = v.findViewById(R.id.dogRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)); // 상하 스크롤
        //recyclerView 페이징 처리
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //recyclerView 가장 마지막 index
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                //받아온 recyclerView 카운트
                int totalCount = recyclerView.getAdapter().getItemCount();
                if (lastPosition == totalCount - 1) {
                    //스크롤 끝까지 하면 작동
                    Log.d("TAG", "onScrolled: 스크롤 끝" + lastPosition + " " + totalCount + " " + homeAdapter.getItemViewType(lastPosition) + " ");
                    if (!searchFlag)
                        if (!isLoading) {
                            loadMoreData();
                        }
                    if(isLoading){
                        Toast.makeText(ct, "No Data", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void initializeNoDataTextView() {
        noDataText = v.findViewById(R.id.noDataText);
    }

    //처음 recyclerView에 데이터 set
    private void setDataRecyclerView() {
        homeAdapter = new HomeAdapter();
        apiDataList = new ArrayList<>();
        homeAdapter.setListener(this);
        setRecyclerView();
    }

    //처음 recyclerView에 데이터 set
    private void setRecyclerView() {
        //room에 저장된 dogData를 recyclerView에 set
        pageOffset = (pageNumber - 1) * PAGE_SIZE;
        List<DogData> dogDataList = db.getDogDao().getItemsByPage(PAGE_SIZE, pageOffset);//DB에 있는 Data를 List에 저장
        pageNumber++;
        int i = 0;
        //for문을 통해 DogDto 객체에 저장
        for (DogData one : dogDataList) {
            apiDataList.add(returnDogDto(one, i));
            i++;
        }
        apiDataList.add(null);
        homeAdapter.setItems(apiDataList);
        homeAdapter.notifyItemInserted(apiDataList.size() - 1);
        recyclerView.setAdapter(homeAdapter);
    }

    //RecyclerView 클릭 시 이벤트 처리 부분 콜백함수
    @Override
    public void onRecyclerViewBookmarkSelected(View v, int position, ArrayList<DogDto> arrayList) {
        setBookmark(position, arrayList);
    }

    private void setBookmark(int position, ArrayList<DogDto> arrayList){
        // 북마크 상태 저장 변수 true -> 북마크 O false -> 북마크 X
        BookmarkDB bookmarkDB = new BookmarkDB(arrayList, position, ct);
        boolean checkData = bookmarkDB.dbCheck(); // 데이터가 북마크 되어 있는 상태인지 확인

        if (checkData) { // 북마크에 저장 되어 있음 -> 삭제 작업
            arrayList.get(position).setBookmark_img(R.drawable.unselected_bookmark_icon);
            bookmarkDB.updateBookmark();
            arrayList.get(position).setBookmark_check(false);
        } else { // 북마크에 저장 되어 있지 않음 -> 추가 작업
            arrayList.get(position).setBookmark_img(R.drawable.selected_bookmark_icon);
            bookmarkDB.updateBookmark();
            arrayList.get(position).setBookmark_check(true);
        }
        //RecyclerView 업데이트
        homeAdapter.notifyItemChanged(position);
    }

    //북마크 새로고침 작업 수행
    private void setActivityResultLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) { //resultCode가 0으로 넘어왔다면
                Intent data = result.getData();
                ArrayList<DogDto> bookmarkInfo = new ArrayList<>();
                int i = 0;
                for (DogDto dog : homeAdapter.getHomeItemList()) {
                    if(dog != null){
                    bookmarkInfo.add(new DogDto(dog));
                    bookmarkInfo.get(i++).setImage(dog.getImage());
                    }
                    else {
                        bookmarkInfo.add(null);
                    }
                }
                if (db.getDogDao().checkData(data.getIntExtra("id", -1))) {
                    bookmarkInfo.get(data.getIntExtra("position", -1)).setBookmark_img(R.drawable.selected_bookmark_icon);
                } else {
                    bookmarkInfo.get(data.getIntExtra("position", -1)).setBookmark_img(R.drawable.unselected_bookmark_icon);
                }
                homeAdapter.setItems(bookmarkInfo);
                homeAdapter.notifyItemChanged(data.getIntExtra("position", -1));
            }
        });
    }


    @Override
    public void onRecyclerViewItemSelected(int id, String imgUrl, int position) {
        switchToInfoScreen(id, imgUrl, position);
    }

    //상세 정보 화면으로 넘어 가는 기능 구현
    private void switchToInfoScreen(int id, String imgUrl, int position){
        Intent intent = new Intent(ct, InformationActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("imgUrl", imgUrl);
        intent.putExtra("position", position);
        activityResultLauncher.launch(intent);
    }

    //검색창 비어 있을 때 메소드 구현
    public void searchbarEmpty() {
        searchFlag = false;
        recyclerView.setVisibility(View.VISIBLE);
        noDataText.setVisibility(View.INVISIBLE);
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

    //검색 메소드 구현
    public void search(String query) {
        searchList.clear();
        List<DogData> searchDataList = db.getDogDao().search("%" + query + "%", query);
        if (searchDataList.size() == 0) {
            noDataText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noDataText.setVisibility(View.INVISIBLE);
            int i = 0;
            for (DogData one : searchDataList) {
                searchList.add(returnDogDto(one, i));
                i++;
            }
            homeAdapter.setItems(searchList);
        }
    }

    //DogDto에 Data 넣어서 return 하는 메소드 구현
    public DogDto returnDogDto(DogData one, int i) {
        DogDto resultData = new DogDto(one.id, one.name, one.bredFor,
                "Stubborn, Curious, Playful, Adventurous, Active, Fun-loving", "12", false, R.drawable.unselected_bookmark_icon, i, 1);
        DogImage img = new DogImage();
        img.setUrl(one.img);
        resultData.setImage(img);
        if (db.getDogDao().checkData(one.id))
            resultData.setBookmark_img(R.drawable.selected_bookmark_icon);
        return resultData;
    }

    //LoadingIndicator 구현
    private void loadMoreData() {
        pageOffset = (pageNumber++ - 1) * PAGE_SIZE; // 페이징 offset
        List<DogData> dogDataList = db.getDogDao().getItemsByPage(PAGE_SIZE, pageOffset); //DB에 있는 Data를 List에 저장
        if (dogDataList.isEmpty()) {
            apiDataList.remove(apiDataList.size()-1);
            homeAdapter.setItems(apiDataList);
            isLoading = true;
        } else {
            apiDataList.remove(apiDataList.size() - 1);
            int scrollPosition = apiDataList.size();
            homeAdapter.notifyItemRemoved(scrollPosition);
            //room에 저장된 dogData를 recyclerView에 set
            //for문을 통해 DogDto 객체에 저장
            int i = 0;
            for (DogData one : dogDataList) {
                apiDataList.add(returnDogDto(one, i));
                i++;
            }
            apiDataList.add(null);
            homeAdapter.setItems(apiDataList);
            isLoading = false;
        }
    }
}