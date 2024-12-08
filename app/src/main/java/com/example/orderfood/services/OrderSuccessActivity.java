package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

import com.example.orderfood.R;

public class OrderSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        Button orderSuccessBtnHome = findViewById(R.id.order_success_btn_home);

        // Tạo hoạt ảnh nhấp nhô cho nút
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.0f, 1.2f, // Start and end values for the X axis scaling
                1.0f, 1.2f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f // Pivot point of Y scaling
        );
        scaleAnimation.setDuration(700);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        scaleAnimation.setRepeatMode(Animation.REVERSE);

        // Bắt đầu hoạt ảnh
        orderSuccessBtnHome.startAnimation(scaleAnimation);

        orderSuccessBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(OrderSuccessActivity.this);
                progressDialog.setMessage(getString(R.string.loading_message)); // Đặt thông điệp loading từ strings.xml
                progressDialog.setCancelable(false); // Không cho phép hủy bằng cách chạm ra ngoài
                progressDialog.show();

                new Handler().postDelayed(() -> {
                    progressDialog.dismiss();
                    Intent backHome = new Intent(OrderSuccessActivity.this, HomeActivity.class);
                    startActivity(backHome);
                    // Áp dụng hiệu ứng chuyển cảnh
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }, 1000);

            }
        });
    }
}