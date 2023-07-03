package com.example.test;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context mContext;
    private ArrayList<DogDto> arrayList2 = null;
    private final onListItemSelectedInterface mListener;

    public Adapter(Context context, onListItemSelectedInterface listener) {
        this.mContext = context;
        this.mListener = listener;
        arrayList2 = new ArrayList<>();
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_dog_list, parent, false);
        return new Adapter.ViewHolder(view);
    }


    //ViewHolder의 데이터 설정
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { //position 값 -> 보여지는 데이터의 위치.
        Log.d("TAG", "위치 " + arrayList2.get(position).getName());
        holder.text_name.setText(arrayList2.get(position).getName());
        if (Objects.isNull(arrayList2.get(position).getBred_for()) || arrayList2.get(position).getBred_for().isEmpty())
            holder.text_bred.setText(". . . . ");
        else
            holder.text_bred.setText(arrayList2.get(position).getBred_for());
        holder.button.setImageResource(arrayList2.get(position).getBookmark_img());
        //Glide를 이용해서 이미지 출력
        RequestOptions circleCrop = new RequestOptions().circleCrop(); //이미지 원형으로 나타내기 위한 사전 작업
        Glide.with(mContext)
                .load(arrayList2.get(position).getImage().getUrl()) // 이미지 소스 로드
                .apply(circleCrop) // 이미지 원형으로 출력
                .thumbnail(0.1f) // 실제 이미지 크기 중 30%만 먼저 가져와서 흐릿하게 보여줌
                .into(holder.dogImg); // 이미지 띄울 view 선택

    }

    @Override
    public int getItemCount() {
        return arrayList2.size();
    }



    public void setItems(List<DogDto> items){
        arrayList2.clear();
        arrayList2.addAll(items);
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
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        TextView text_bred;
        ImageButton button;
        ImageView dogImg;

        ViewHolder(View itemView) {
            super(itemView);

            text_name = itemView.findViewById(R.id.name_text);
            text_bred = itemView.findViewById(R.id.bred_for_text);
            button = itemView.findViewById(R.id.bookmark_button);
            dogImg = itemView.findViewById(R.id.dogImg);

            //북마크 클릭 시 이벤트 처리
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, getAdapterPosition(), arrayList2);
                    Log.d("test", "포지션=" + getAdapterPosition());
                }
            });

            //itemView 클릭 시 상세 정보로 넘어 가는 이벤트 처리
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.changeScreen(arrayList2.get(getAdapterPosition()).getId(), arrayList2.get(getAdapterPosition()).getImage().getUrl());
                }
            });


        }

    }

}


