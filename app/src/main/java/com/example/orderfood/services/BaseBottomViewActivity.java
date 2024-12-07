package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.orderfood.R;

public class BaseBottomViewActivity extends AppCompatActivity {
    private ImageView buttonHome, buttonFavorite, notice, buttonHistory, buttonSelfMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_view);

        // Lấy các ImageView bằng id
         buttonHome = findViewById(R.id.button_home);
         buttonFavorite = findViewById(R.id.button_favorite);
         notice = findViewById(R.id.notice);
         buttonHistory = findViewById(R.id.button_history);
         buttonSelfMe = findViewById(R.id.button_self_me);



        buttonHome.setImageResource(R.drawable.ic_baseline_home_click_24);


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
        Intent intent;
        switch (destination) {
            case "Home":
                // Điều hướng đến HomeActivity

                buttonHome.setImageResource(R.drawable.ic_baseline_home_click_24);
                break;
            case "Favorite":
                // Điều hướng đến FavoriteActivity
                buttonFavorite.setImageResource(R.drawable.ic_baseline_favorite_click_24);
                break;
            case "Notice":
                // Điều hướng đến NoticeActivity
                notice.setImageResource(R.drawable.ic_baseline_notifications_click_24);
                break;
            case "History":
                // Điều hướng đến HistoryActivity
                buttonHistory.setImageResource(R.drawable.ic_baseline_history_click_24);
                break;
            case "Profile":
                // Điều hướng đến ProfileActivity
                buttonSelfMe.setImageResource(R.drawable.ic_baseline_account_circle_click_24);
                break;
            default:
                break;
        }
    }
    private void  reload_image(){
        buttonHome.setImageResource(R.drawable.ic_baseline_home_24);
        buttonFavorite.setImageResource(R.drawable.ic_baseline_favorite_24);
        notice.setImageResource(R.drawable.ic_baseline_notifications_24);
        buttonHistory.setImageResource(R.drawable.ic_baseline_history_24);
        buttonSelfMe.setImageResource(R.drawable.ic_baseline_account_circle_24);
    }

}
