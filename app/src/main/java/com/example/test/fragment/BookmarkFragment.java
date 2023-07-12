package com.example.test.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.test.R;
import com.example.test.activity.InformationActivity;
import com.example.test.dto.BookmarkDto;
import com.example.test.room.DogDataDatabase;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import Interface.OnBookmarkListItemSelecteListener;
import adapter.BookMarkAdapter;


public class BookmarkFragment extends Fragment implements OnBookmarkListItemSelecteListener {

    private BookMarkAdapter bookmarkAdapter;
    private Context ct;
    private View v;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private List<BookmarkDto> bookmarkDBInfo;
    private ArrayList<BookmarkDto> bookmarkInfo;
    private TextView totalText;
    private DogDataDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_bookmark, container, false);
        ct = container.getContext();
        db = Room.databaseBuilder(ct, DogDataDatabase.class, "DogData").allowMainThreadQueries().build();
        totalText = v.findViewById(R.id.countText);

        initializeBookmarkRecyclerView();
        setActivityResultLauncher();

        return v;
    }

    private void initializeBookmarkRecyclerView(){
        RecyclerView bookmarkRecyclerView = v.findViewById(R.id.bookmarkRecyclerGridView);
        bookmarkRecyclerView.setLayoutManager(new GridLayoutManager(ct, 3));

        bookmarkAdapter = new BookMarkAdapter(ct, this);
        bookmarkDBInfo = db.getDogDao().getBookmarkAll();
        bookmarkInfo = new ArrayList<>(bookmarkDBInfo);

        setBookmarkRecyclerView(bookmarkInfo);
        bookmarkRecyclerView.setAdapter(bookmarkAdapter);
    }

    private void  setActivityResultLauncher(){
        //상세 정보 화면에서 다시 돌아올 때 북마크 정보 새로고침
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) { //resultCode가 0으로 넘어왔다면
                // 새로고침 작업 수행
                bookmarkDBInfo = db.getDogDao().getBookmarkAll();
                ArrayList<BookmarkDto> bookmarkInfo2 = new ArrayList<>(bookmarkDBInfo);
                setBookmarkRecyclerView(bookmarkInfo2);
            }
        });
    }



    @Override
    public void onDogImageClick(int id, String imgUrl, int position) {
        switchToInfoScreen(id, imgUrl, position);
    }

    //각각의 사진 클릭 시 상세 정보로 화면 전환
    private void switchToInfoScreen(int id, String imgUrl, int position){
        Intent intent = new Intent(ct, InformationActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("imgUrl", imgUrl);
        intent.putExtra("position", position);
        activityResultLauncher.launch(intent);
    }


    @Override
    public void onDogImageLongClick(int id, int position) {
        deleteBookmark(id, position);
    }

    private void deleteBookmark(int id, int position){
        //View 길게 누르면 북마크 삭제 이벤트 구현
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(ct)
                .setTitle("Delete Bookmark")
                .setMessage("Are you sure?")
                .setPositiveButton("DELETE", (dialogInterface, i) -> {
                    ArrayList<BookmarkDto> bookmarkInfo = new ArrayList<>(bookmarkAdapter.getBookmarkItemList());
                    db.getDogDao().updateBookmarkCheck(!db.getDogDao().checkData(id), id);
                    bookmarkInfo.remove(position);
                    setBookmarkRecyclerView(bookmarkInfo);
                })
                .setNegativeButton("CANCEL", null);
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }

    private void setBookmarkRecyclerView(ArrayList<BookmarkDto> itemList){
        totalText.setText(MessageFormat.format("{0} {1}", bookmarkInfo.size(), getString(R.string.bookmark_total_text)));
        bookmarkAdapter.setItems(itemList);
        bookmarkAdapter.notifyDataSetChanged();

    }
}