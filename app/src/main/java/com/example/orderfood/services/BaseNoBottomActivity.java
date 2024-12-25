package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.orderfood.R;
import com.example.orderfood.data.CurrentUser;
import com.google.android.material.navigation.NavigationView;

public class BaseNoBottomActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Button showSidebarMenu;
    LinearLayout itemNewOrder;
    LinearLayout itemHistoryOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_no_bottom);
        CurrentUser.init(this);

        BindHead();
        BindSideBar();
    }

    private void BindSideBar() {
        // Ánh xạ các view
        drawerLayout = findViewById(R.id.drawer_layout);
        showSidebarMenu = findViewById(R.id.show_sidebar_menu);
        itemNewOrder = findViewById(R.id.side_bar_item_new_order);
        itemHistoryOrder = findViewById(R.id.side_bar_item_history_order);

        if(CurrentUser.getRole() == 0)
        {
            // Xử lý sự kiện mở/đóng menu
            showSidebarMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    } else {
                        drawerLayout.openDrawer(Gravity.LEFT);
                    }
                }
            });

        }else
        {
            showSidebarMenu.setVisibility(View.GONE);
        }

        itemNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị loading
                ProgressDialog progressDialog = new ProgressDialog(BaseNoBottomActivity.this);
                progressDialog.setMessage("Vui lòng đợi...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                // Thêm DrawerListener để lắng nghe sự kiện đóng drawer
                drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Chuyển sang màn hình OrderNewActivity
                        Intent goOrderNew = new Intent(BaseNoBottomActivity.this, OrderNewActivity.class);
                        BaseNoBottomActivity.this.startActivity(goOrderNew);

                        // Đóng loading khi drawer đã đóng
                        progressDialog.dismiss();

                        // Loại bỏ DrawerListener để tránh lắng nghe không cần thiết
                        drawerLayout.removeDrawerListener(this);
                    }
                });

                // Đóng drawer
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });

        itemHistoryOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị loading
                ProgressDialog progressDialog = new ProgressDialog(BaseNoBottomActivity.this);
                progressDialog.setMessage("Vui lòng đợi...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                // Thêm DrawerListener để lắng nghe sự kiện đóng drawer
                drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Chuyển sang màn hình OrderNewActivity
                        Intent goOrderNew = new Intent(BaseNoBottomActivity.this, ListOrderActivity.class);
                        BaseNoBottomActivity.this.startActivity(goOrderNew);

                        // Đóng loading khi drawer đã đóng
                        progressDialog.dismiss();

                        // Loại bỏ DrawerListener để tránh lắng nghe không cần thiết
                        drawerLayout.removeDrawerListener(this);
                    }
                });

                // Đóng drawer
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
    }

    private void BindHead() {
        CurrentUser.init(this);
        if (CurrentUser.getRole() != 1) // role 1 là shiper
        {
            Button cartHead = findViewById(R.id.product_detail_icon_cart);

            cartHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Hiển thị ProgressDialog với style tùy chỉnh
                    ProgressDialog progressDialog = new ProgressDialog(view.getContext(), R.style.CustomProgressDialog);
                    progressDialog.setMessage(view.getContext().getString(R.string.loading_message)); // Đặt thông điệp loading từ strings.xml
                    progressDialog.setCancelable(false); // Không cho phép hủy bằng cách chạm ra ngoài
                    progressDialog.show();

                    // Tạo hiệu ứng scale
                    ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f, 1f);
                    ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.5f, 1f);
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(scaleX, scaleY);
                    animatorSet.setDuration(300); // Thời gian hiệu ứng

                    // Thiết lập lắng nghe khi hiệu ứng kết thúc
                    animatorSet.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            Intent intent = new Intent(BaseNoBottomActivity.this, CartActivity.class);
                            BaseNoBottomActivity.this.startActivity(intent);
                            progressDialog.dismiss(); // Đóng ProgressDialog
                        }
                    });

                    // Bắt đầu hiệu ứng
                    animatorSet.start();
                }
            });
        } else {
            Button cartHead = findViewById(R.id.product_detail_icon_cart);
            cartHead.setVisibility(View.GONE);
        }
    }
}