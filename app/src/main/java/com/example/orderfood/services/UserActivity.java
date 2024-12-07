package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.example.orderfood.Fragment.AccountFragment;
import com.example.orderfood.Fragment.OrderHistoryFragment;
import com.example.orderfood.R;
import com.example.orderfood.data.MenuAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserActivity extends AppCompatActivity {
    private ImageView imgAvatar;
    private ListView listMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        imgAvatar = findViewById(R.id.imgAvatar);
        listMenu = findViewById(R.id.listMenu);

        // Tải ảnh đại diện từ Firebase (hoặc URL khác)
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("account").document("id")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String avatarUrl = documentSnapshot.getString("image");
                    if (avatarUrl != null) {
                        Glide.with(this).load(avatarUrl).into(imgAvatar);
                    }
                });

        //Tạo danh sách menu
        String[] menuItems = {"Thông tin account", "Lịch sử đơn hàng", "Đăng xuất"};
        int[] icons = {R.drawable.user, R.drawable.calendar, R.drawable.logout};

        MenuAdapter adapter = new MenuAdapter(this, menuItems, icons);
        listMenu.setAdapter(adapter);
        // Xử lý khi chọn menu
        listMenu.setOnItemClickListener((adapterView, view, position, id) -> {
            switch (position) {
                case 0: // Thông tin account
                    openFragment(new AccountFragment());
                    break;
                case 1: // Lịch sử đơn hàng
                    openFragment(new OrderHistoryFragment());
                    break;
                case 2: // Đăng xuất
                    handleLogout();
                    break;
            }
        });

    }
    private void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void handleLogout() {
        // Xóa thông tin SharedPreferences (nếu có)
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        // Quay lại trang Login
        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}