package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.test.R;
import com.example.test.dto.DogDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Interface.onListItemSelectedInterface;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final onListItemSelectedInterface mListener;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context mContext;
    private ArrayList<DogDto> homeItemList = null;

    public HomeAdapter(Context context, onListItemSelectedInterface listener) {
        this.mContext = context;
        this.mListener = listener;
        homeItemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_dog_list, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_dog_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    //ViewHolder의 데이터 설정
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) { //position 값 -> 보여지는 데이터의 위치.
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            DogDto dog = homeItemList.get(position);
            itemViewHolder.bindData(dog);
        }
    }

    @Override
    public int getItemCount() {
        return homeItemList == null ? 0 : homeItemList.size();
    }

    public ArrayList<DogDto> getHomeItemList() {
        ArrayList<DogDto> target = new ArrayList<>(homeItemList);
        return target;
    }

    @Override
    public int getItemViewType(int position) {
        return homeItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    public void setItems(List<DogDto> items) {
        homeItemList.clear();
        homeItemList.addAll(items);
//        for(DogDto one : arrayList2)
//            Log.d("TAG", "setItems: " + one.getName());
        notifyDataSetChanged();
    }

    //얕은 복사를 깊은 복사로 해결하는 방법
//    public void setItems(ArrayList<DogDto> items){
//        arrayList2.clear();
//        arrayList2.addAll(items);
//        for(DogDto one : arrayList2)
//            Log.d("TAG", "setItems: " + one.getName());
//        notifyDataSetChanged();
//    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        TextView text_bred;
        ImageButton button;
        ImageView dogImg;

        public ItemViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.nameText);
            text_bred = itemView.findViewById(R.id.bredForText);
            button = itemView.findViewById(R.id.bookmarkButton);
            dogImg = itemView.findViewById(R.id.dogImg);


            //북마크 클릭 시 이벤트 처리
            button.setOnClickListener(view -> {
                mListener.onItemSelected(view, getAdapterPosition(), homeItemList);
                Log.d("test", "포지션=" + getAdapterPosition());
            });

            //itemView 클릭 시 상세 정보로 넘어 가는 이벤트 처리
            itemView.setOnClickListener(view -> {
                mListener.changeScreen(homeItemList.get(getAdapterPosition()).getId(), homeItemList.get(getAdapterPosition()).getImage().getUrl(), getAdapterPosition());
            });
        }

        public void bindData(DogDto dog) {
            text_name.setText(dog.getName());
            text_bred.setText(Objects.requireNonNullElse(dog.getBred_for(), ". . . . "));
            button.setImageResource(dog.getBookmark_img());

            RequestOptions circleCrop = new RequestOptions().circleCrop();
            Glide.with(mContext)
                    .load(dog.getImage().getUrl())
                    .apply(circleCrop)
                    .thumbnail(0.1f)
                    .into(dogImg);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.loadingIndicator); // ProgressBar 초기화
        }
    }
}






