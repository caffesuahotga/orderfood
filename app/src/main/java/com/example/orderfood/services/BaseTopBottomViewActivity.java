package com.example.orderfood.services;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.orderfood.R;

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

        // Khôi phục trạng thái các biểu tượng
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
    }

    // Phương thức điều hướng (hoặc xử lý logic)
    private void navigateTo(String destination) {
        reload_image();
        SharedPreferences preferences = getSharedPreferences("icon_state", MODE_PRIVATE);
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
                    Intent home = new Intent(this, HomeActivity.class);
                    this.startActivity(home);
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
                    Intent historyOrder = new Intent(this, HistoryOrderActivity.class);
                    this.startActivity(historyOrder);
                    break;
                case "Profile":
                    // Điều hướng đến ProfileActivity
                    buttonSelfMe.setImageResource(R.drawable.ic_baseline_account_circle_click_24);
                    editor.putString("active_icon", "Profile");
                    break;
                default:
                    break;
            }
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
