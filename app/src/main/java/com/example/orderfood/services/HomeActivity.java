package com.example.orderfood.services;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;  // Thêm GridLayoutManager để tạo lưới
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.orderfood.R;
import com.example.orderfood.component.HistoryOrderAdapter;
import com.example.orderfood.component.category_adapter;

import com.example.orderfood.component.product_adapter;
import com.example.orderfood.data.OrderUtil;
import com.example.orderfood.models.Category;
import com.example.orderfood.data.HandleData;
import com.example.orderfood.models.Order;
import com.example.orderfood.models.Product;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HomeActivity extends BaseTopBottomViewActivity {

    private RecyclerView recyclerView;
    private category_adapter categoryAdapter;
    private List<Category> categoryList;
    private RecyclerView recyclerView1;
    private product_adapter productAdapter;
    private List<Product> productList;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final String TAG = "FirestoreData";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getLayoutInflater().inflate(R.layout.activity_home, findViewById(R.id.content_frame_top_bot));
        BindData();

        swipeRefreshLayout = findViewById(R.id.home_page_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Show loading spinner
            swipeRefreshLayout.setRefreshing(true);

            // Call lại API hoặc logic tải dữ liệu trong background thread
            new Thread(() -> {
                loadHome();


                // Sau khi dữ liệu được tải, thực hiện thao tác trên UI thread
                runOnUiThread(() -> {
                    // Kết thúc hiệu ứng refresh
                    swipeRefreshLayout.setRefreshing(false);
                });
            }).start();
        });
    }
        private void BindData () {
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
            productAdapter = new product_adapter(this,productList);
            recyclerView1.setAdapter(productAdapter);
        }

        // cập nhật data khi kéo
        private void loadHome () {
            runOnUiThread(() -> {
                BindData();
            });

        }
    }


