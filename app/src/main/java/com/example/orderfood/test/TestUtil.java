package com.example.orderfood.test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.orderfood.R;
import com.example.orderfood.data.HandleData;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TestUtil extends AppCompatActivity {

    private static final String TAG = "ProductTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.test_util_activity);
        TextView textView = findViewById(R.id.myTextView);

        // Bắt đầu thực hiện tác vụ bất đồng bộ
         // 1 là ID sản phẩm, 4 là đánh giá mới
    }


}