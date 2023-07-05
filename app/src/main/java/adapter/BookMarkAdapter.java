package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;

import Interface.onListItemSelectedInterface;

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.ViewHolder> {

    private final onListItemSelectedInterface mListener;

    public BookMarkAdapter(onListItemSelectedInterface mListener) {
        this.mListener = mListener;
    }


    @NonNull
    @Override
    public BookMarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_dog_list, parent, false);
        return new BookMarkAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookMarkAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
                    
                    //mListener.changeScreen(arrayList2.get(getAdapterPosition()).getId(), arrayList2.get(getAdapterPosition()).getImage().getUrl());
                }
            });
        }
    }
}
