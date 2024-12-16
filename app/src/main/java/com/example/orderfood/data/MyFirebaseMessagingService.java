package com.example.orderfood.data;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.orderfood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String BACKEND_URL = "https://threeguy.click/api/send";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("FCM Token", "New token: " + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Kiểm tra nếu thông báo có chứa dữ liệu hoặc thông điệp thông báo
        if (remoteMessage.getData().size() > 0) {
            // Xử lý dữ liệu, ví dụ như tải về thông tin từ server
        }

        if (remoteMessage.getNotification() != null) {
            // Tạo và hiển thị thông báo
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(String messageBody) {
        // Code để tạo và hiển thị thông báo trên thiết bị
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "default_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("New Notification")
                .setContentText(messageBody)
                .setSmallIcon(R.drawable.icon_order)
                .build();

        notificationManager.notify(0, notification);
    }

    public static String getTokenAndSave() {
        try {
            // Lấy token FCM một cách đồng bộ
            Task<String> tokenTask = FirebaseMessaging.getInstance().getToken();
            String fcmToken = Tasks.await(tokenTask); // Chờ task hoàn thành và lấy kết quả
            if (fcmToken != null) {
                Log.d("FCMHelper", "FCM Token: " + fcmToken);
                return fcmToken;
            } else {
                Log.w("FCMHelper", "Token is null");
                return null;
            }
        } catch (Exception e) {
            Log.e("FCMHelper", "Error fetching FCM token", e);
            return null; // Trả về null nếu có lỗi xảy ra
        }
    }

    public static void sendNotificationToServer(String token, String title, String body) {
        new Thread(() -> {
            try {
                // Tạo URL kết nối tới backend server
                URL url = new URL(BACKEND_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Tạo payload JSON
                String payload = "{"
                        + "\"targetTokens\":[\"" + token + "\"],"
                        + "\"title\":\"" + title + "\","
                        + "\"body\":\"" + body + "\""
                        + "}";

                // Gửi payload tới backend server
                OutputStream os = conn.getOutputStream();
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
                os.flush();
                os.close();

                // Kiểm tra mã phản hồi từ backend server
                int responseCode = conn.getResponseCode();
                Log.d("message", "Backend Response Code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("message", "Notification sent successfully.");
                } else {
                    Log.w("message", "Failed to send notification.");
                }
            } catch (Exception e) {
                Log.e("message", "Error sending notification to server", e);
            }
        }).start();
    }
}

