package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.orderfood.R;
import com.example.orderfood.data.CurrentUser;

public class BaseNoBottomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_no_bottom);

        BindHead();
    }

    private void BindHead() {
        CurrentUser.init(this);
        if(CurrentUser.getRole() != 1) // role 1 là shiper
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
        }
        else
        {
            Button cartHead = findViewById(R.id.product_detail_icon_cart);
            cartHead.setVisibility(View.GONE);
        }
    }
}