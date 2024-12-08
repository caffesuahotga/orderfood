package com.example.orderfood.services;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.data.CurrentUser;
import com.example.orderfood.models.Account;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    private ImageView imgAvatar;
    private EditText etdUsername, edtname, edtPassword, edtPhone, edtStore;
    private Button btnEdit, btnSave, btnLogOut;

    private Uri selectedImageUri;
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgAvatar = findViewById(R.id.imgAvatar);
        etdUsername = findViewById(R.id.etUsername);
        edtname = findViewById(R.id.etName);
        edtPassword = findViewById(R.id.etPassword);
        edtPhone = findViewById(R.id.etPhone);
        edtStore = findViewById(R.id.etStore);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);
        btnLogOut = findViewById(R.id.btnLogin);

        // Tải thông tin người dùng
        loadUserData();

        // Bắt sự kiện nút Sửa
        btnEdit.setOnClickListener(v -> enableEditing());

        // Bắt sự kiện nút Lưu
        btnSave.setOnClickListener(v -> saveUserData());

        // Bắt sự kiện nhấn vào ảnh đại diện để chọn ảnh mới
        imgAvatar.setOnClickListener(v -> selectAvatar());

        // Xử lý đăng xuất
        btnLogOut.setOnClickListener(view -> logout());
    }
    private void loadUserData() {
        Account user = CurrentUser.getCurrentUser();

        etdUsername.setText(user.getUsername());
        edtname.setText(user.getName());
        edtPassword.setText(user.getPassword());
        edtPhone.setText(user.getPhone());
        edtStore.setText("Loading...");

        // Load ảnh đại diện
        Glide.with(this)
                .load(user.getImage())
                .placeholder(R.drawable.ic_default_avatar)
                .into(imgAvatar);

        // Truy vấn tên cửa hàng dựa trên StoreId
        FirebaseFirestore.getInstance().collection("store")
                .document(String.valueOf(user.getRole()))  // Giả sử role là StoreId
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String storeName = documentSnapshot.getString("name");
                    edtStore.setText(storeName != null ? storeName : "Unknown");
                });
    }

    private void enableEditing() {
        isEditing = true;
        btnEdit.setVisibility(View.GONE);
        btnSave.setVisibility(View.VISIBLE);

        etdUsername.setEnabled(true);
        edtname.setEnabled(true);
        edtPassword.setEnabled(true);
        edtPhone.setEnabled(true);
        edtStore.setEnabled(false); // Store không cho sửa
    }

    private void saveUserData() {
        if (!isEditing) return;

        // Xác nhận mật khẩu cũ
        String currentPassword = edtPassword.getText().toString();
        if (!currentPassword.equals(CurrentUser.getCurrentUser().getPassword())) {
            Toast.makeText(this, "Mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật thông tin người dùng
        Account user = CurrentUser.getCurrentUser();
        user.setUsername(etdUsername.getText().toString());
        user.setName(edtname.getText().toString());
        user.setPassword(edtPassword.getText().toString());
        user.setPhone(edtPhone.getText().toString());

        // Tải ảnh lên Cloudinary nếu có
        if (selectedImageUri != null) {
            uploadImageToCloudinary(selectedImageUri, user);
        } else {
            updateUserInFirebase(user);
        }
    }

    private void selectAvatar() {
        // Mở bộ chọn ảnh từ thư viện
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imgAvatar.setImageURI(selectedImageUri);  // Cập nhật ảnh ngay lập tức
        }
    }

    private void uploadImageToCloudinary(Uri imageUri, Account user) {
        // Giả sử bạn đã cấu hình Cloudinary
        String imageUrl = "https://dummy.cloudinary.url/avatar.jpg"; // Thay bằng URL thực

        user.setImage(imageUrl);
        updateUserInFirebase(user);
    }

    private void updateUserInFirebase(Account user) {
        FirebaseFirestore.getInstance().collection("users")
                .document(String.valueOf(user.getId()))
                .set(user)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    CurrentUser.saveCurrentUser(user);
                    btnEdit.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.GONE);

                    etdUsername.setEnabled(false);
                    edtname.setEnabled(false);
                    edtPassword.setEnabled(false);
                    edtPhone.setEnabled(false);
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