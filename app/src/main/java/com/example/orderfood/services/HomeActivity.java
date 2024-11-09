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
import com.example.orderfood.component.product_adapter;
import com.example.orderfood.models.category;
import com.example.orderfood.models.product;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private category_adapter categoryAdapter;
    private List<category> categoryList;
    private RecyclerView recyclerView1;
    private product_adapter  productAdapter;
    private List<product> productList;

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

        // Tìm RecyclerView và sử dụng GridLayoutManager để chia thành 8 cột
        recyclerView = findViewById(R.id.view_item);

        // Sử dụng GridLayoutManager với 8 cột
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);  // Áp dụng LayoutManager

        // Khởi tạo danh sách category
        categoryList = new ArrayList<>();
        categoryList.add(new category(R.drawable.icon_rice, "Cơm"));
        categoryList.add(new category(R.drawable.icon_phos, "Phở"));
        categoryList.add(new category(R.drawable.icon_drink, "Đồ uống"));
        categoryList.add(new category(R.drawable.icon_banh_trang, "Bánh tráng"));
        categoryList.add(new category(R.drawable.icon_banh_kem, "Bánh kem"));
        categoryList.add(new category(R.drawable.icon_com_chay, "Cơm cháy"));
        categoryList.add(new category(R.drawable.icon_trai_cay, "Trái cây"));


        // Thêm các phần tử khác nếu cần

        // Set adapter cho RecyclerView
        categoryAdapter = new category_adapter(categoryList);
        recyclerView.setAdapter(categoryAdapter);


        recyclerView1 = findViewById(R.id.view_product);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 2);
        recyclerView1.setLayoutManager(gridLayoutManager1);  // Áp dụng LayoutManager
        productList = new ArrayList<>();
        productList.add(new product("cơm gà xối mỡ", R.drawable.icon_com_chay, "24000", 3.0));
        productList.add(new product("cơm gà xối mỡ", R.drawable.icon_com_chay, "24000", 5.0));
        productList.add(new product("cơm gà xối mỡ", R.drawable.icon_com_chay, "24000", 4.5));
        productList.add(new product("cơm gà xối mỡ", R.drawable.icon_com_chay, "24000", 3.0));
        productList.add(new product("cơm gà xối mỡ", R.drawable.icon_com_chay, "24000", 5.0));
        productList.add(new product("cơm gà xối mỡ", R.drawable.icon_com_chay, "24000", 4.5));
        productAdapter = new product_adapter(productList);
        recyclerView1.setAdapter(productAdapter);


    }
}
