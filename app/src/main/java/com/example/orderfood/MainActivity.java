package com.example.orderfood;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.orderfood.services.login;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

        auth.signOut();

        if (auth.getCurrentUser() == null) {
            // Nếu người dùng chưa đăng nhập, chuyển hướng tới trang đăng nhập
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
            finish(); // Đóng MainActivity để tránh quay lại mà không đăng nhập
        } else {
            Log.d("GoogleSignIn", "hello da vao trong");
        }


//        db.collection("user")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            // Hiển thị dữ liệu của từng document trong log
//                            Log.d("Firestore", document.getId() + " => " + document.getData());
//                        }
//                    } else {
//                        Log.w("Firestore", "Lỗi khi lấy dữ liệu", task.getException());
//                    }
//                });
//    }
    }

    // login gooogle

}