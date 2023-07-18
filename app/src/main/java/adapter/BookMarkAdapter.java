package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.test.R;
import com.example.test.dto.BookmarkDto;

import java.util.ArrayList;
import java.util.List;

import Interface.OnBookmarkListItemSelectListener;

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.ItemViewHolder> {

    private final OnBookmarkListItemSelectListener mListener;
    Context mContext;
    private final ArrayList<BookmarkDto> bookmarkItemList;

    public BookMarkAdapter(Context context, OnBookmarkListItemSelectListener mListener) {
        this.mContext = context;
        this.mListener = mListener;
        bookmarkItemList = new ArrayList<>();
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_dog_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Log.d("TAG", "onBindViewHolder: 실행?");
        BookmarkDto bookmark = bookmarkItemList.get(position);
        RequestOptions crop = new RequestOptions().centerCrop();
        Glide.with(mContext)
                .load(bookmark.getImg()) // 이미지 소스 로드
                .apply(crop)
                .thumbnail(0.1f) // 실제 이미지 크기 중 30%만 먼저 가져와서 흐릿하게 보여줌
                .into(holder.bookmarkImg); // 이미지 띄울 view 선택
    }

    @Override
    public int getItemCount() {
        return bookmarkItemList.size();
    }

    public ArrayList<BookmarkDto> getBookmarkItemList() {
        return new ArrayList<>(bookmarkItemList);
    }

    public void setItems(List<BookmarkDto> items) {
        bookmarkItemList.clear();
        bookmarkItemList.addAll(items);
        for (BookmarkDto one : bookmarkItemList)
            Log.d("TAG", "setItems: " + one.getName());

    }

    //bookmark_dog_list에 있는 view 인플레이팅 및 클릭 이벤트 처리
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageButton bookmarkImg;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            bookmarkImg = itemView.findViewById(R.id.bookmarkImage);

            //이미지 클릭 시 상세정보로 넘어가는 이벤트 구현
            bookmarkImg.setOnClickListener(view -> mListener.onDogImageClick(bookmarkItemList.get(getAdapterPosition()).getId(), bookmarkItemList.get(getAdapterPosition()).getImg(), getAdapterPosition()));
            //이미지 길게 클릭 시 북마크에서 삭제하는 이벤트 구현
            bookmarkImg.setOnLongClickListener(view -> {
                mListener.onDogImageLongClick(bookmarkItemList.get(getAdapterPosition()).getId(), getAdapterPosition());
                return false;
            });
        }
    }
}
