package com.example.orderfood.services;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;  // Thêm GridLayoutManager để tạo lưới
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.orderfood.R;
import com.example.orderfood.component.category_adapter;

import com.example.orderfood.models.Category;
import com.example.orderfood.models.HandleData;
import com.example.orderfood.models.Product;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private category_adapter categoryAdapter;
    private List<Category> categoryList;
    private RecyclerView recyclerView1;

    private List<Product> productList;




    private static final String TAG = "FirestoreData";
    FirebaseFirestore db = FirebaseFirestore.getInstance();









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);

        // Khởi tạo ImageSlider và thêm các slide hình ảnh
        ImageSlider imageSlider = findViewById(R.id.image_slider);
        ArrayList<SlideModel> slideModelArrayList = new ArrayList<>();

        slideModelArrayList.add(new SlideModel(R.drawable.img_splash_1, ScaleTypes.FIT));
        slideModelArrayList.add(new SlideModel(R.drawable.img_splash_2, ScaleTypes.FIT));
        slideModelArrayList.add(new SlideModel(R.drawable.logo, ScaleTypes.FIT));

        imageSlider.setImageList(slideModelArrayList, ScaleTypes.FIT);
// Khởi tạo RecyclerView cho category
        recyclerView = findViewById(R.id.view_item);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);  // Áp dụng LayoutManager

        // Khởi tạo danh sách category
        categoryList = new ArrayList<>();

        // Gọi phương thức getCategories từ HandleData
        HandleData handleData = new HandleData();
        handleData.getCategories(new HandleData.FirestoreCallback() {
            @Override
            public void onCallback(List<Category> categories) {
                if (categories != null) {
                    categoryList.clear();
                    categoryList.addAll(categories);  // Thêm danh sách category từ Firestore vào categoryList
                    categoryAdapter = new category_adapter(categoryList);
                    recyclerView.setAdapter(categoryAdapter);  // Set adapter cho RecyclerView
                }
            }
        });


    }








//        recyclerView = findViewById(R.id.view_item);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
//        recyclerView.setLayoutManager(gridLayoutManager);  // Áp dụng LayoutManager
//
//        // Khởi tạo danh sách category
//        categoryList = new ArrayList<>();
//        categoryList.add(new Category(R.drawable.icon_rice, "Cơm"));
//        categoryList.add(new Category(R.drawable.icon_phos, "Phở"));
//        categoryList.add(new Category(R.drawable.icon_drink, "Đồ uống"));
//        categoryList.add(new Category(R.drawable.icon_banh_trang, "Bánh tráng"));
//        categoryList.add(new Category(R.drawable.icon_banh_kem, "Bánh kem"));
//        categoryList.add(new Category(R.drawable.icon_com_chay, "Cơm cháy"));
//        categoryList.add(new Category(R.drawable.icon_trai_cay, "Trái cây"));
//
//
//        // Thêm các phần tử khác nếu cần
//
//        // Set adapter cho RecyclerView
//        categoryAdapter = new category_adapter(categoryList);
//        recyclerView.setAdapter(categoryAdapter);

}

