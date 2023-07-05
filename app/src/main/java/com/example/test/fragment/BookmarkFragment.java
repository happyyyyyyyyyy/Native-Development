package com.example.test.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.test.R;
import com.example.test.dto.BookmarkDto;
import com.example.test.room.DogDataDatabase;

import java.util.ArrayList;
import java.util.List;

import Interface.onBookmarkListItemSelectedInterface;
import adapter.BookMarkAdapter;


public class BookmarkFragment extends Fragment implements onBookmarkListItemSelectedInterface {

    BookMarkAdapter bookmarkAdapter;
    Context ct;
    private DogDataDatabase db;
    RecyclerView bookmarkRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bookmark, container, false);
        ct = container.getContext();
        db = Room.databaseBuilder(ct, DogDataDatabase.class, "DogData").allowMainThreadQueries().build();
        bookmarkRecyclerView = v.findViewById(R.id.bookmarkRecyclerGridView);
        bookmarkRecyclerView.setLayoutManager(new GridLayoutManager(ct, 3));
//        bookmarkRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        bookmarkAdapter = new BookMarkAdapter(ct,this);
        List<BookmarkDto> bookmarkDBInfo = db.getDogDao().getBookmarkAll();
        ArrayList<BookmarkDto> bookmarkInfo = new ArrayList<>(bookmarkDBInfo);

        bookmarkAdapter.setItems(bookmarkInfo);
        bookmarkRecyclerView.setAdapter(bookmarkAdapter);
        bookmarkAdapter.notifyDataSetChanged();
        return v;
    }


    //각각의 사진 클릭 시 상세 정보로 화면 전환
    @Override
    public void changeScreen(int id, String imgUrl, int position) {

    }
}