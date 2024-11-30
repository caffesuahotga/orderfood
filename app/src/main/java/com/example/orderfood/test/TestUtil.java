package com.example.orderfood.test;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.orderfood.R;
import com.example.orderfood.models.Product;
import com.example.orderfood.data.HandleData;
import com.example.orderfood.sqlLite.DatabaseHelper;
import com.example.orderfood.sqlLite.dao.CartDAO;
import com.example.orderfood.sqlLite.model.Cart;

import java.util.List;

public class TestUtil extends AppCompatActivity {

    private static final String TAG = "ProductTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.test_util_activity);

    }
}
