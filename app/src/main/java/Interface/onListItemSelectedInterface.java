package Interface;

import android.view.View;

import com.example.test.dto.DogDto;

import java.util.ArrayList;

public interface onListItemSelectedInterface {
    void onItemSelected(View v, int position, ArrayList<DogDto> arrayList);

    void changeScreen(int id, String imgUrl, int position);
}
