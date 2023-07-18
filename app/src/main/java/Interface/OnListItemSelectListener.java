package Interface;

import android.view.View;

import com.example.test.dto.DogDto;

import java.util.ArrayList;

public interface OnListItemSelectListener {
    void onRecyclerViewBookmarkSelected(View v, int position, ArrayList<DogDto> arrayList);

    void onRecyclerViewItemSelected(int id, String imgUrl, int position);
}
