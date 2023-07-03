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

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<DogDto> arrayList2;

    private onListItemSelectedInterface mListener;
    Context mContext;
    public Adapter(Context context, onListItemSelectedInterface listener){
        this.mContext = context;
        this.mListener = listener;
        arrayList2 = new ArrayList<>();
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_dog_list,parent,false);
        return new Adapter.ViewHolder(view);
    }

    
    //ViewHolder의 데이터 설정
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { //position 값 -> 보여지는 데이터의 위치.
        Log.d("TAG", "위치 " + arrayList2.get(position).getName());
        holder.text_name.setText(arrayList2.get(position).getName());
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

    public void setArrayData(DogDto dogInfo){
        Log.d("TAG", "setArrayData: " + dogInfo.getName());
        arrayList2.add(dogInfo);
        Log.d("TAG", "setArrayData: " + arrayList2.get(0).getName());
        Log.d("TAG", "setArrayData: " + arrayList2.size());

//        for (int i = 0; i < arrayList2.size(); i++) {
//            Log.d("TAG", "Element at index " + i + ": " + arrayList2.get(i).getName());
//        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        TextView text_bred;
        ImageButton button;
        ImageView dogImg;

        ViewHolder(View itemView){
            super(itemView);

            text_name = itemView.findViewById(R.id.name_text);
            text_bred = itemView.findViewById(R.id.bred_for_text);
            button = itemView.findViewById(R.id.bookmark_button);
            dogImg = itemView.findViewById(R.id.dog_img);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v,getAdapterPosition(), arrayList2, button, text_name);
                    Log.d("test","포지션="+getAdapterPosition());
                    //여기서 textview넘겨주기
                }
            });


        }

    }

}


