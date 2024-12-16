package com.example.orderfood.services;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.orderfood.R;
import com.example.orderfood.data.CurrentUser;
import com.example.orderfood.models.Account;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends  BaseTopBottomViewActivity {

    private String documentId;
    private ImageView imgAvatar;
    private EditText etUsername, etName, etPassword, etPhone,etOldPassword;
    private TextView tvUsername,tvName, tvPhone,tvTransparent;
    private Button btnSave, btnLogOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_profile, findViewById(R.id.content_frame_top_bot));
        CurrentUser.init(this);

        // Ánh xạ các view
        imgAvatar = findViewById(R.id.imgAvatar);
        etUsername = findViewById(R.id.etUsername);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        tvUsername = findViewById(R.id.tvUsername);
        tvName = findViewById(R.id.tvName);
        etOldPassword = findViewById(R.id.etOldPassword);
        tvPhone = findViewById(R.id.tvPhone);
        tvTransparent = findViewById(R.id.tvTransparent);
        btnSave = findViewById(R.id.btnSave);
        btnLogOut = findViewById(R.id.btnLogOut);

        // Tải thông tin người dùng
        loadUserData();

        // Bắt sự kiện nút Lưu
        btnSave.setOnClickListener(v -> saveUserData());

        // Xử lý đăng xuất
        btnLogOut.setOnClickListener(view -> logout());
    }

    private void loadUserData() {
            Account user = CurrentUser.getCurrentUser();
            tvUsername.setText(user.getUsername());
            tvName.setText(user.getName());
            tvPhone.setText(user.getPhone());

            // Load ảnh đại diện
            Glide.with(this)
                    .load(user.getImage())
                    .apply(RequestOptions.circleCropTransform()) // Bo tròn hình ảnh
                    .placeholder(R.drawable.ic_default_avatar)
                    .into(imgAvatar);
    }

    private void saveUserData() {
        final String inputUsername = etUsername.getText().toString().trim();
        final String inputName= etName.getText().toString().trim();
        final String inputPhone = etPhone.getText().toString().trim();
        final String inputOldPassword = etOldPassword.getText().toString().trim();
        final String inputNewPassword = etPassword.getText().toString().trim();
        final ProgressDialog mDialog = new ProgressDialog(ProfileActivity.this);

        Account user = CurrentUser.getCurrentUser();

        if (inputUsername.isEmpty() && inputName.isEmpty() && inputPhone.isEmpty() && inputOldPassword.isEmpty() && inputNewPassword.isEmpty()) {
            mDialog.dismiss();
            Toast.makeText(ProfileActivity.this, "Vui lòng không để trống cả 4 chỗ", Toast.LENGTH_SHORT).show();
        }else if(inputName.isEmpty() && inputPhone.isEmpty() && inputOldPassword.isEmpty() && inputNewPassword.isEmpty()) {
            if(user.getUsername().equals(inputUsername)){
                mDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "Tên người dùng đã tồn tại. Vui lòng chọn một cái khác.", Toast.LENGTH_SHORT).show();
            }
            user.setUsername(inputUsername);
        }else if(inputUsername.isEmpty() && inputName.isEmpty() && inputPhone.isEmpty() ){
            if(!user.getPassword().equals(inputOldPassword)){
                mDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "Mật khẩu không khớp với mật khẩu cũ", Toast.LENGTH_SHORT).show();
            }else if(inputNewPassword.length() < 6){
                mDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            }else {
                user.setPassword(inputNewPassword);
            }
        } else if (inputUsername.isEmpty() && inputPhone.isEmpty() && inputOldPassword.isEmpty() && inputNewPassword.isEmpty()) {
            user.setName(inputName);
        } else if (inputUsername.isEmpty() && inputName.isEmpty() && inputOldPassword.isEmpty() && inputNewPassword.isEmpty()) {
           user.setPhone(inputPhone);
        } else{
            if(user.getUsername().equals(inputUsername)){
                mDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "Tên người dùng đã tồn tại. Vui lòng chọn một cái khác.", Toast.LENGTH_SHORT).show();
            }else if(!user.getPassword().equals(inputOldPassword)){
                mDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "Mật khẩu không khớp với mật khẩu cũ", Toast.LENGTH_SHORT).show();
            }else if(inputNewPassword.length() < 6){
                mDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            }else{
                user.setUsername(inputUsername);
                user.setName(inputName);
                user.setPassword(inputNewPassword);
                user.setPhone(inputPhone);

            }
        }

        updateUserInFirebase(user);
    }

    private void updateUserInFirebase(Account user) {
        FirebaseFirestore.getInstance().collection("account")
                .document(String.valueOf(user.getId()))
                .set(user)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    CurrentUser.saveCurrentUser(user);
                    loadUserData();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                });
    }

    private void logout() {
        // Xóa thông tin người dùng trong CurrentUser
        CurrentUser.clearCurrentUser();
        // Chuyển hướng về màn hình đăng nhập
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        // Hiển thị thông báo
        Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
    }

}