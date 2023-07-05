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

import Interface.onBookmarkListItemSelectedInterface;

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.ViewHolder> {

    Context mContext;
    private final onBookmarkListItemSelectedInterface mListener;
    private ArrayList<BookmarkDto> bookmarkList  = null;

    public BookMarkAdapter(Context context, onBookmarkListItemSelectedInterface mListener) {
        this.mContext = context;
        this.mListener = mListener;
        bookmarkList = new ArrayList<>();
    }


    @NonNull
    @Override
    public BookMarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_dog_list, parent, false);
        return new BookMarkAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookMarkAdapter.ViewHolder holder, int position) {
        Log.d("TAG", "onBindViewHolder: 실행?");
        BookmarkDto bookmark = bookmarkList.get(position);
        RequestOptions crop = new RequestOptions().centerCrop();
        Glide.with(mContext)
                .load(bookmark.getImg()) // 이미지 소스 로드
                .apply(crop)
                .thumbnail(0.1f) // 실제 이미지 크기 중 30%만 먼저 가져와서 흐릿하게 보여줌
                .into(holder.bookmarkImg); // 이미지 띄울 view 선택
    }

    @Override
    public int getItemCount() {
        return bookmarkList.size();
    }

    public ArrayList<BookmarkDto> getBookmarkList() {
        ArrayList<BookmarkDto> target = new ArrayList<>(bookmarkList);
        return target;
    }

    public void setItems(List<BookmarkDto> items){
        bookmarkList.clear();
        bookmarkList.addAll(items);
        for(BookmarkDto one : bookmarkList)
            Log.d("TAG", "setItems: " + one.getName());

    }

    //bookmark_dog_list에 있는 view 인플레이팅 및 클릭 이벤트 처리
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageButton bookmarkImg;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookmarkImg = itemView.findViewById(R.id.bookmarkImage);

            //이미지 클릭 시 상세정보로 넘어가는 이벤트 구현
            bookmarkImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.changeScreen(bookmarkList.get(getAdapterPosition()).getId(), bookmarkList.get(getAdapterPosition()).getImg(), getAdapterPosition());
                }
            });

            bookmarkImg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mListener.deleteBookmark(bookmarkList.get(getAdapterPosition()).getId(), getAdapterPosition());
                    return false;
                }
            });
        }
    }
}
