package com.example.orderfood.services;

import android.os.Bundle;
import android.util.Log;

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
import com.example.orderfood.models.Order;
import com.example.orderfood.models.OrderDetail;
import com.example.orderfood.models.Product;
import com.example.orderfood.models.Store;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private category_adapter categoryAdapter;
    private List<Category> categoryList;
    private RecyclerView recyclerView1;
    private product_adapter productAdapter;
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
        recyclerView.setAdapter(categoryAdapter);  // Set adapter cho RecyclerView





        recyclerView1 = findViewById(R.id.view_product);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 2);
        recyclerView1.setLayoutManager(gridLayoutManager1);
        // Áp dụng LayoutManager
        HandleData handleData1 = new HandleData();
        productList = handleData1.getAllProducts();

        productAdapter = new product_adapter(productList);
        recyclerView1.setAdapter(productAdapter);

        addProduct();

    };

    private void addProduct() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(
                15,
                "Tào phớ",
                101,
                new ArrayList<>(Arrays.asList("https://res.cloudinary.com/duf1lmvzu/image/upload/v1732809151/w1r70m8rpcioi7cx4ogu.jpg",
                        "https://res.cloudinary.com/duf1lmvzu/image/upload/v1732809165/lcsoktdi899vws6kw367.jpg",
                        "https://res.cloudinary.com/duf1lmvzu/image/upload/v1732809166/u5pw0dqc1itewjm2gtj1.jpg")),
                20000,
                4.3,
                10,
                "",
                1,
                3
        ));
        products.add(new Product(
                16,
                "Chè bắp",
                101,
                new ArrayList<>(Arrays.asList("https://res.cloudinary.com/duf1lmvzu/image/upload/v1732808991/cbjeujf2yeb9vedkmmb7.jpg",
                        "https://res.cloudinary.com/duf1lmvzu/image/upload/v1732808978/lm6vzs7rxlsdzfsppklu.jpg",
                        "https://res.cloudinary.com/duf1lmvzu/image/upload/v1732808994/jzaca32jysc6j4akx6in.jpg")),
                20000,
                4.0,
                10,
                "",
                1,
                3
        ));
        products.add(new Product(
                17,
                "Rau câu bánh flan",
                101,
                new ArrayList<>(Arrays.asList("https://res.cloudinary.com/duf1lmvzu/image/upload/v1732809286/vemge1nq1h5gxmpy3nuu.jpg",
                        "https://res.cloudinary.com/duf1lmvzu/image/upload/v1732809290/hg7m0o8rldgx4mzf7c25.jpg",
                        "https://res.cloudinary.com/duf1lmvzu/image/upload/v1732809297/a8elu7vbhg0xu3qhbci5.jpg")),
                25000,
                4.7,
                10,
                "",
                1,
                3
        ));

        for (Product pro : products) {
            Map<String, Object> proMap = new HashMap<>();
            proMap.put("id", pro.getId());
            proMap.put("name", pro.getName());
            proMap.put("image_source", pro.getImage_source());
            proMap.put("image", pro.getImage());
            proMap.put("price", pro.getPrice());
            proMap.put("rate", pro.getRate());
            proMap.put("minutes", pro.getMinutes());
            proMap.put("description", pro.getDescription());
            proMap.put("storeID", pro.getStoreID());
            proMap.put("categoryID", pro.getCategoryID());

            db.collection("product")
                    .document(String.valueOf(pro.getId()))
                    .set(proMap)
                    .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi thêm pro", e));
        }
    }

}
