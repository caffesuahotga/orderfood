package com.example.orderfood.services;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;  // Thêm GridLayoutManager để tạo lưới
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.orderfood.R;
import com.example.orderfood.component.category_adapter;

import com.example.orderfood.component.product_adapter;
import com.example.orderfood.models.Category;
import com.example.orderfood.data.HandleData;
import com.example.orderfood.models.Product;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private category_adapter categoryAdapter;
    private List<Category> categoryList;
    private RecyclerView recyclerView1;
    private product_adapter productAdapter;
    private List<Product> productList;
    private TextView wellcome;

    private static final String TAG = "FirestoreData";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);


        //lấy thông tin đã lưu
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Quý Khách");
        wellcome = findViewById(R.id.wellcome);
        wellcome.setText("Chào mừng, " + username + "!");

        // Khởi tạo ImageSlider và thêm các slide hình ảnh
        ImageSlider imageSlider = findViewById(R.id.image_slider);
        ArrayList<SlideModel> slideModelArrayList = new ArrayList<>();

        slideModelArrayList.add(new SlideModel(R.drawable.image_slider_1, ScaleTypes.FIT));
        slideModelArrayList.add(new SlideModel(R.drawable.image_slider_2, ScaleTypes.FIT));
        slideModelArrayList.add(new SlideModel(R.drawable.image_slider_3, ScaleTypes.FIT));
        slideModelArrayList.add(new SlideModel(R.drawable.image_slider_4, ScaleTypes.FIT));

        imageSlider.setImageList(slideModelArrayList, ScaleTypes.FIT);




        recyclerView = findViewById(R.id.view_item);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);  // Áp dụng LayoutManager

        // Khởi tạo danh sách category
        HandleData handleData = new HandleData();
        categoryList = handleData.getAllCategories();
        categoryAdapter = new category_adapter(categoryList);
        recyclerView.setAdapter(categoryAdapter);
        // Set adapter cho RecyclerView







        recyclerView1 = findViewById(R.id.view_product);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 2);
        recyclerView1.setLayoutManager(gridLayoutManager1);

        HandleData handleData1 = new HandleData();
        productList = handleData1.getAllProducts();
        productAdapter = new product_adapter(productList);
        recyclerView1.setAdapter(productAdapter);


    };

}

