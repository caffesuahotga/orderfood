package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.orderfood.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private FirebaseFirestore database;
    private CollectionReference table_user;
    private EditText edtusername, edtpassword, confirmPassword;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtusername = findViewById(R.id.edtusername);
        edtpassword = findViewById(R.id.edtpassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        database = FirebaseFirestore.getInstance();
        table_user = database.collection("account");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Vui lòng đợi...");
                mDialog.show();


                final String inputUsername = edtusername.getText().toString().trim();
                final String inputPassword = edtpassword.getText().toString().trim();
                final String inputConfirmPassword = confirmPassword.getText().toString().trim();

                // Kiểm tra dữ liệu đầu vào
                if (inputUsername.isEmpty() || inputPassword.isEmpty() || inputConfirmPassword.isEmpty()) {
                    mDialog.dismiss();
                    Toast.makeText(SignUp.this, "Vui lòng không để trống", Toast.LENGTH_SHORT).show();
                } else if (!inputPassword.equals(inputConfirmPassword)) {
                    mDialog.dismiss();
                    Toast.makeText(SignUp.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                } else if (inputPassword.length() < 6) {
                    mDialog.dismiss();
                    Toast.makeText(SignUp.this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                } else {
                    table_user.whereEqualTo("username", inputUsername).get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    boolean usernameExists = false;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Nếu có tài liệu nào trả về, tên đăng nhập đã tồn tại
                                        usernameExists = true;
                                        break;
                                    }
                                    if (usernameExists) {
                                        mDialog.dismiss();
                                        // Nếu tên đăng nhập đã tồn tại, thông báo lỗi
                                        Toast.makeText(SignUp.this, "Tên người dùng đã tồn tại. Vui lòng chọn một cái khác.", Toast.LENGTH_LONG).show();
                                    } else {
                                        // Tìm ID cao nhất trong Firestore
                                        table_user.orderBy("id", Query.Direction.DESCENDING).limit(1).get()
                                                .addOnCompleteListener(idTask -> {
                                                    if (idTask.isSuccessful() && !idTask.getResult().isEmpty()) {
                                                        // Lấy ID cao nhất hiện tại
                                                        QueryDocumentSnapshot highestIdDoc = (QueryDocumentSnapshot) idTask.getResult().getDocuments().get(0);
                                                        long highestId = highestIdDoc.getLong("id");

                                                        // Tạo ID mới bằng cách tăng giá trị lên 1
                                                        long newId = highestId + 1;

                                                        // Tạo thông tin người dùng để lưu vào Firestore
                                                        Map<String, Object> userData = new HashMap<>();
                                                        userData.put("id", newId); // Lưu ID số tự động
                                                        userData.put("username", inputUsername);
                                                        userData.put("password", inputPassword);

                                                        // Lưu thông tin vào Firestore
                                                        table_user.add(userData)
                                                                .addOnSuccessListener(documentReference -> {
                                                                    mDialog.dismiss();
                                                                    Toast.makeText(SignUp.this, "Người dùng đã đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                                                    // Chuyển hướng sang trang login
                                                                    Intent intent = new Intent(SignUp.this, login.class);
                                                                    startActivity(intent);
                                                                    // Kết thúc SignUp để không quay lại được bằng nút back
                                                                    finish();
                                                                })
                                                                .addOnFailureListener(e ->
                                                                        Toast.makeText(SignUp.this, "Không thể lưu người dùng: " + e.getMessage(), Toast.LENGTH_LONG).show());
                                                    } else {
                                                        // Nếu không có tài liệu nào, bắt đầu từ ID 1
                                                        long newId = 1;

                                                        // Tạo thông tin người dùng để lưu vào Firestore
                                                        Map<String, Object> userData = new HashMap<>();
                                                        userData.put("id", newId); // Lưu ID số tự động
                                                        userData.put("username", inputUsername);
                                                        userData.put("password", inputPassword);

                                                        mDialog.dismiss();
                                                        // Lưu thông tin vào Firestore
                                                        table_user.add(userData)
                                                                .addOnSuccessListener(documentReference -> {
                                                                    Toast.makeText(SignUp.this, "Người dùng đã đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                                                    // Chuyển hướng sang trang login
                                                                    Intent intent = new Intent(SignUp.this, login.class);
                                                                    startActivity(intent);
                                                                    // Kết thúc SignUp để không quay lại được bằng nút back
                                                                    finish();
                                                                })
                                                                .addOnFailureListener(e ->
                                                                        Toast.makeText(SignUp.this, "Không thể lưu người dùng: " + e.getMessage(), Toast.LENGTH_LONG).show());
                                                    }
                                                });
                                    }
                                } else {
                                    mDialog.dismiss();
                                    // Nếu có lỗi trong quá trình truy vấn
                                    Toast.makeText(SignUp.this, "Không thể lưu người dùng: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }
}