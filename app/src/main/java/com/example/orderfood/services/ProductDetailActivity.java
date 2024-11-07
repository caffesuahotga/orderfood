package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.orderfood.R;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);


        ImageView imageView = findViewById(R.id.pd_main_pic);

        // Tải hình ảnh từ URL vào ImageView
        Glide.with(this)
                .load("https://tamnhuhoa.com/datafiles/37/2023-08/31848625-com-tam-suon-bi-ba-chi.png")  // URL hình ảnh của bạn
                .into(imageView);  // ImageView để hiển thị hình ảnh
    }
}