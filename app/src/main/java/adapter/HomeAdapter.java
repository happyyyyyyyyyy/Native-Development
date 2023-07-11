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

import Interface.OnListItemSelectedInterface;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private OnListItemSelectedInterface listener = null;
    private Context context;
    private final ArrayList<DogDto> homeItemList;
    private enum ViewType{
        ITEM(0),
        LOADING(1);
        private final int value;
        ViewType(int i) {
            this.value = i;
        }
    }
    public HomeAdapter() {
        homeItemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == ViewType.ITEM.value) {
            View view = LayoutInflater.from(context).inflate(R.layout.home_dog_list, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.home_dog_loading, parent, false);
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
        return homeItemList.size();
    }

    public ArrayList<DogDto> getHomeItemList() {
        return new ArrayList<>(homeItemList);
    }

    @Override
    public int getItemViewType(int position) {
        return homeItemList.get(position) == null ? ViewType.LOADING.value : ViewType.ITEM.value;
    }

    public void setListener(OnListItemSelectedInterface listener){
        this.listener = listener;
    }

    public void setItems(List<DogDto> items) {
        homeItemList.clear();
        homeItemList.addAll(items);
        notifyDataSetChanged();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
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
        }

        //데이터 set
        public void bindData(DogDto dog) {
            text_name.setText(dog.getName());
            text_bred.setText(Objects.requireNonNullElse(dog.getBred_for(), ". . . . "));
            button.setImageResource(dog.getBookmark_img());

            RequestOptions circleCrop = new RequestOptions().circleCrop();
            Glide.with(context)
                    .load(dog.getImage().getUrl())
                    .apply(circleCrop)
                    .thumbnail(0.1f)
                    .into(dogImg);

            //북마크 클릭 시 이벤트 처리
            button.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onRecyclerViewBookmarkSelected(view, getAdapterPosition(), homeItemList);
                    Log.d("test", "포지션=" + getAdapterPosition());
                }
            });

            //itemView 클릭 시 상세 정보로 넘어 가는 이벤트 처리
            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onRecyclerViewItemSelected(homeItemList.get(getAdapterPosition()).getId(), homeItemList.get(getAdapterPosition()).getImage().getUrl(), getAdapterPosition());
                }
            });
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






