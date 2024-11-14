package com.example.orderfood.models;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.orderfood.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

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
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .build();

        notificationManager.notify(0, notification);
    }
}

