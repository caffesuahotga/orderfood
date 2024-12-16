package com.example.orderfood.services;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.orderfood.R;

import java.util.Locale;

public class BaseTopBottomViewActivity extends  BaseNoBottomActivity{
    private ImageView buttonHome, buttonFavorite, notice, buttonHistory, buttonSelfMe;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_top_bottom_view, findViewById(R.id.content_frame));

        swipeRefreshLayout = findViewById(R.id.cart_page_refresh);

        // Lấy các ImageView bằng id
        buttonHome = findViewById(R.id.button_home);
        buttonFavorite = findViewById(R.id.button_favorite);
        notice = findViewById(R.id.notice);
        buttonHistory = findViewById(R.id.button_history);
        buttonSelfMe = findViewById(R.id.button_self_me);

        SharedPreferences preferences = getSharedPreferences("icon_state", MODE_PRIVATE);
        String activeIcon = preferences.getString("active_icon", "Home");
        switch (activeIcon) {
            case "Home":
                buttonHome.setImageResource(R.drawable.ic_baseline_home_click_24);
                break;
            case "Favorite":
                buttonFavorite.setImageResource(R.drawable.ic_baseline_favorite_click_24);
                break;
            case "Notice":
                notice.setImageResource(R.drawable.ic_baseline_notifications_click_24);
                break;
            case "History":
                buttonHistory.setImageResource(R.drawable.ic_baseline_history_click_24);
                break;
            case "Profile":
                buttonSelfMe.setImageResource(R.drawable.ic_baseline_account_circle_click_24);
                break;
            default:
                buttonHome.setImageResource(R.drawable.ic_baseline_home_click_24);
                break;
        }

        // Đặt sự kiện onClick cho các ImageView
        buttonHome.setOnClickListener(v -> navigateTo("Home"));
        buttonFavorite.setOnClickListener(v -> navigateTo("Favorite"));
        notice.setOnClickListener(v -> navigateTo("Notice"));
        buttonHistory.setOnClickListener(v -> navigateTo("History"));
        buttonSelfMe.setOnClickListener(v -> navigateTo("Profile"));

        // Mặc định hiển thị Home
        if (savedInstanceState == null) {
            String currentDestination = preferences.getString("current_destination", "Home");
            navigateTo(currentDestination);
        }
    }

    // Phương thức điều hướng (hoặc xử lý logic)
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
                case "Home":
                    // Điều hướng đến HomeActivity
                    buttonHome.setImageResource(R.drawable.ic_baseline_home_click_24);
                    editor.putString("active_icon", "Home");
                    intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                    break;
                case "Favorite":
                    // Điều hướng đến FavoriteActivity
                    buttonFavorite.setImageResource(R.drawable.ic_baseline_favorite_click_24);
                    editor.putString("active_icon", "Favorite");
                    break;
                case "Notice":
                    // Điều hướng đến NoticeActivity
                    notice.setImageResource(R.drawable.ic_baseline_notifications_click_24);
                    editor.putString("active_icon", "Notice");
                    break;
                case "History":
                    // Điều hướng đến HistoryActivity
                    buttonHistory.setImageResource(R.drawable.ic_baseline_history_click_24);
                    editor.putString("active_icon", "History");
                    intent = new Intent(this, HistoryOrderActivity.class);
                    startActivity(intent);
                    break;
                case "Profile":
                    // Điều hướng đến ProfileActivity
                    buttonSelfMe.setImageResource(R.drawable.ic_baseline_account_circle_click_24);
                    editor.putString("active_icon", "Profile");
                    intent = new Intent(this, ProfileActivity.class);
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

    private void  reload_image(){
        buttonHome.setImageResource(R.drawable.ic_baseline_home_24);
        buttonFavorite.setImageResource(R.drawable.ic_baseline_favorite_24);
        notice.setImageResource(R.drawable.ic_baseline_notifications_24);
        buttonHistory.setImageResource(R.drawable.ic_baseline_history_24);
        buttonSelfMe.setImageResource(R.drawable.ic_baseline_account_circle_24);
    }
}
