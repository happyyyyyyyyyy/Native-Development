package com.example.test;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<DogDto> arrayList;

    private onListItemSelectedInterface mListener;
    Context mContext;
    public Adapter(Context context, onListItemSelectedInterface listener){
        this.mContext = context;
        this.mListener = listener;
        arrayList = new ArrayList<>();
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
        Log.d("TAG", "위치 " + arrayList.get(position).getName());
        holder.text_name.setText(arrayList.get(position).getName());
        holder.text_bred.setText(arrayList.get(position).getBred_for());
        holder.button.setImageResource(arrayList.get(position).getBookmark_img());
        Log.d("TAG", "북마크 이미지 현재 " + arrayList.get(position).getBookmark_img());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setArrayData(DogDto dogInfo){
        arrayList.add(dogInfo);
//        for(DogDto item:arrayList){
//            Log.d("TAG", "이름: " + item.name +" 북마크 여부: " +  item.bookmark_check + " 품종: " +item.bred_for);
//        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        TextView text_bred;
        ImageButton button;

        ViewHolder(View itemView){
            super(itemView);

            text_name = itemView.findViewById(R.id.name_text);
            text_bred = itemView.findViewById(R.id.bred_for_text);
            button = itemView.findViewById(R.id.bookmark_button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v,getAdapterPosition(), arrayList, button, text_name);
                    Log.d("test","포지션="+getAdapterPosition());
                    //여기서 textview넘겨주기
                }
            });


        }

    }

}


