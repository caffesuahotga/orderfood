package com.example.orderfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.orderfood.data.MyFirebaseMessagingService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth;
    private static final String TAG = "MainActivity";
    private String fcmToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendButton = findViewById(R.id.send_noti);

        // Lấy token FCM và hiển thị trong logcat
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("MainActivity", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                fcmToken = task.getResult();
                Log.d("MainActivity", "FCM Token: " + fcmToken);
            }
        });

        // Gửi thông báo khi nhấn nút
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fcmToken != null) {
                    MyFirebaseMessagingService.sendNotificationToServer(fcmToken, "vy vy khoi", "This is a test notification.");
                } else {
                    Toast.makeText(MainActivity.this, "FCM Token is not available yet!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}