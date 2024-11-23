package com.example.orderfood.test;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.orderfood.R;
import com.example.orderfood.models.Category;
import com.example.orderfood.models.HandleData;

import java.util.List;

public class TestUtil extends AppCompatActivity {

    private static final String TAG = "CategoryTest";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.test_util_activity);
        HandleData handleData = new HandleData();



        new Thread(() -> { // Chạy trong background thread
            List<Category> categories = handleData.getAllCategories();
            if (categories != null && !categories.isEmpty()) {
                for (Category category : categories) {
                    Log.d(TAG, "Category Found: ID: " + category.getId() +
                            ", Name: " + category.getName() +
                            ", Image: " + category.getImage());
                }
            } else {
                Log.d(TAG, "No categories found or error occurred.");
            }
        }).start();






    }

}
