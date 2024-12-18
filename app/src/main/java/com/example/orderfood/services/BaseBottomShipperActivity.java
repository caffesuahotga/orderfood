package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.orderfood.R;

public class BaseBottomShipperActivity extends AppCompatActivity {
    private ImageView buttonHome, buttonHistory, buttonSelfMe;
    private Button noti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_bottom_shipper);
        noti = findViewById(R.id.shipper_noti);
        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hiển thị ProgressDialog
                ProgressDialog progressDialog = new ProgressDialog(BaseBottomShipperActivity.this);
                progressDialog.setMessage("Đang tải...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                // Mô phỏng một tác vụ tải dữ liệu với thời gian trễ
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Ẩn ProgressDialog
                        progressDialog.dismiss();

                        // Chuyển tới ShipperNotiActivity
                        Intent intent = new Intent(BaseBottomShipperActivity.this, ShipperNotiActivity.class);
                        startActivity(intent);
                    }
                }, 1000);
            }
        });


        // Lấy các ImageView bằng id
        buttonHome = findViewById(R.id.ship_button_home);
        buttonHistory = findViewById(R.id.ship_button_history);
        buttonSelfMe = findViewById(R.id.ship_button_self_me);

        // Kiểm tra trạng thái biểu tượng hiện tại từ SharedPreferences
        SharedPreferences preferences = getSharedPreferences("icon_state", MODE_PRIVATE);
        String activeIcon = preferences.getString("active_icon", "ShipperHome");

        switch (activeIcon) {
            case "ShipperHome":
                buttonHome.setImageResource(R.drawable.ic_baseline_home_click_24);
                break;
            case "ShipperHistory":
                buttonHistory.setImageResource(R.drawable.ic_baseline_history_click_24);
                break;
            case "ShipperProfile":
                buttonSelfMe.setImageResource(R.drawable.ic_baseline_account_circle_click_24);
                break;
            default:
                break;
        }

        // Đặt sự kiện onClick cho các ImageView
        buttonHome.setOnClickListener(v -> navigateTo("ShipperHome"));
        buttonHistory.setOnClickListener(v -> navigateTo("ShipperHistory"));
        buttonSelfMe.setOnClickListener(v -> navigateTo("ShipperProfile"));

        if (savedInstanceState == null) {
            String currentDestination = preferences.getString("current_destination", "ShipperHome");
            navigateTo(currentDestination);
        }
    }

    private void navigateTo(String destination) {
        SharedPreferences preferences = getSharedPreferences("icon_state", MODE_PRIVATE);
        String currentDestination = preferences.getString("current_destination", "");

        // Kiểm tra nếu trang hiện tại đang được hiển thị, không chuyển trang
        if (destination.equals(currentDestination)) {
            return;
        }

        reload_image();
        SharedPreferences.Editor editor = preferences.edit();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang chuyển trang, vui lòng chờ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Handler().postDelayed(() -> {
            Intent intent;
            switch (destination) {
                case "ShipperHome":
                    // Điều hướng đến HomeActivity
                    buttonHome.setImageResource(R.drawable.ic_baseline_home_click_24);
                    editor.putString("active_icon", "ShipperHome");
                    intent = new Intent(this, ShipperHomeActivity.class);
                    startActivity(intent);
                    break;
                case "ShipperHistory":
                    // Điều hướng đến HistoryActivity
                    buttonHistory.setImageResource(R.drawable.ic_baseline_history_click_24);
                    editor.putString("active_icon", "ShipperHistory");
                    intent = new Intent(this, ShipperHistoryOrderActivity.class);
                    startActivity(intent);
                    break;
                case "ShipperProfile":
                    // Điều hướng đến ProfileActivity
                    buttonSelfMe.setImageResource(R.drawable.ic_baseline_account_circle_click_24);
                    editor.putString("active_icon", "ShipperProfile");
                    intent = new Intent(this, ProfileShipperActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            editor.putString("current_destination", destination);
            editor.apply();
            progressDialog.dismiss();
        }, 500);
    }

    private void reload_image() {
        buttonHome.setImageResource(R.drawable.ic_baseline_home_24);
        buttonHistory.setImageResource(R.drawable.ic_baseline_history_24);
        buttonSelfMe.setImageResource(R.drawable.ic_baseline_account_circle_24);
    }
}