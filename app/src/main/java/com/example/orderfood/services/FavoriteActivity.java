package com.example.orderfood.services;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.orderfood.R;
import com.example.orderfood.component.FavoriteAdapter;
import com.example.orderfood.component.category_adapter;
import com.example.orderfood.component.product_adapter;
import com.example.orderfood.data.CurrentUser;
import com.example.orderfood.data.HandleData;
import com.example.orderfood.models.Account;
import com.example.orderfood.models.Category;
import com.example.orderfood.models.dto.FavoriteDTO;
import com.example.orderfood.sqlLite.dao.FavoriteDAO;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends BaseTopBottomViewActivity {
    private Account currentUser = CurrentUser.getCurrentUser();
    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private List<FavoriteDTO>  favoriteDTOList = new ArrayList<>();
    private FavoriteDAO favoriteDAO = new FavoriteDAO(this);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getLayoutInflater().inflate(R.layout.activity_favorite, findViewById(R.id.content_frame_top_bot));

        favoriteDTOList = favoriteDAO.getAllProducts(currentUser.getId());
        if (favoriteDTOList == null || favoriteDTOList.size()==0) {
            Toast.makeText(this, "Bạn chưa có sản  phẩm yêu thích" , Toast.LENGTH_SHORT).show();
        }
        else{
            recyclerView = findViewById(R.id.view_favorite);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(gridLayoutManager);


            favoriteAdapter = new FavoriteAdapter(this,this, favoriteDTOList);
            Log.d("FavoriteActivity", favoriteDTOList.get(0).getName());
            recyclerView.setAdapter(favoriteAdapter);

        }






    };

}
