package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class BaseBottomShipperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_bottom_shipper);
    }
}