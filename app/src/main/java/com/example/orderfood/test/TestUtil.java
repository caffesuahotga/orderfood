package com.example.orderfood.test;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.orderfood.R;
import com.example.orderfood.models.Product;
import com.example.orderfood.data.HandleData;

import java.util.List;

public class TestUtil extends AppCompatActivity {

    private static final String TAG = "ProductTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.test_util_activity);

        HandleData handleData = new HandleData();

        new Thread(() -> { // Chạy trong background thread
            List<Product> products = handleData.getAllProducts();
            if (products != null && !products.isEmpty()) {
                for (Product product : products) {
                    Log.d(TAG, "Product Found: ID: " + product.getId() +
                            ", Name: " + product.getName() +
                            ", Image Source: " + product.getImage_source() +
                            ", Price: " + product.getPrice() +
                            ", Rate: " + product.getRate() +
                            ", Minutes: " + product.getMinutes() +
                            ", Description: " + product.getDescription() +
                            ", StoreID: " + product.getStoreID() +
                            ", CategoryID: " + product.getCategoryID());
                }
            } else {
                Log.d(TAG, "No products found or error occurred.");
            }
        }).start();
    }
}
