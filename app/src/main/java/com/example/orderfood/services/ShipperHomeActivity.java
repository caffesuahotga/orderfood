package com.example.orderfood.services;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.example.orderfood.R;
import com.example.orderfood.component.OrderNewAdapter;
import com.example.orderfood.data.NotiUtil;
import com.example.orderfood.data.OrderUtil;
import com.example.orderfood.models.Order;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ShipperHomeActivity extends BaseBottomShipperActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final int REQUEST_CODE_ACCESS_NOTIFICATION_POLICY = 1;
    private static final String TAG = "ShipperHomeActivity";
    private static boolean isFirstRun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_shipper_home, findViewById(R.id.ship_content_frame));

        BindData();

        // Xử lý refresh
        swipeRefreshLayout = findViewById(R.id.ship_order_page_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Show loading spinner
            swipeRefreshLayout.setRefreshing(true);

            // Call lại API hoặc logic tải dữ liệu trong background thread
            new Thread(() -> {

                loadOrder();

                // Sau khi dữ liệu được tải, thực hiện thao tác trên UI thread
                runOnUiThread(() -> {
                    // Kết thúc hiệu ứng refresh
                    swipeRefreshLayout.setRefreshing(false);
                });
            }).start();
        });


        // Kiểm tra và yêu cầu quyền ACCESS_NOTIFICATION_POLICY
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null && !notificationManager.isNotificationPolicyAccessGranted()) {
            Log.d(TAG, "Notification policy access not granted. Requesting permission.");

            // Hiển thị pop-up hướng dẫn người dùng vào cài đặt để cấp quyền
            new AlertDialog.Builder(this)
                    .setTitle("Yêu cầu quyền thông báo")
                    .setMessage("Ứng dụng cần quyền truy cập để gửi thông báo. Vui lòng cấp quyền trong cài đặt.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                            startActivityForResult(intent, REQUEST_CODE_ACCESS_NOTIFICATION_POLICY);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Hủy bỏ và xử lý nếu cần
                            Log.e(TAG, "User denied notification policy access.");
                        }
                    })
                    .show();
        } else {
            // Nếu quyền đã được cấp, thực thi tác vụ
            Log.d(TAG, "Notification policy access already granted.");
            runTokenSaveTask();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_ACCESS_NOTIFICATION_POLICY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission granted for ACCESS_NOTIFICATION_POLICY.");

                runTokenSaveTask();
            } else {
                // Quyền bị từ chối, xử lý nếu cần
                Log.e(TAG, "Permission denied for ACCESS_NOTIFICATION_POLICY");
            }
        }
    }

    private void runTokenSaveTask() {
        if (isFirstRun) {
            Log.d(TAG, "Running token save task.");
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            // Thực thi tác vụ trong ExecutorService
            executorService.execute(() -> {
                try {
                    // Lưu token FCM cho tài khoản
                    NotiUtil.saveTokenFCMAccount(ShipperHomeActivity.this);
                    // Đánh dấu ứng dụng đã chạy lần đầu tiên
                    isFirstRun = false;
                } catch (Exception e) {
                    Log.e(TAG, "Error saving token FCM account", e);
                }
            });

            // Tắt ExecutorService sau khi hoàn thành tác vụ
            executorService.shutdown();
        }
    }

    private void BindData() {
        // lấy danh sách order của account hiện tại
        ArrayList<Order> odList = OrderUtil.getAllOrdersNewForShipper();
        ArrayList<Order> sortedList = (ArrayList<Order>) odList.stream()
                .sorted(Comparator.comparingInt(Order::getId).reversed())
                .collect(Collectors.toList());

        RecyclerView order_new = findViewById(R.id.ship_order_container);
        order_new.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        if (odList == null) {
            odList = new ArrayList<>(); // Nếu danh sách trả về null, khởi tạo một ArrayList rỗng
        }

        OrderNewAdapter order_new_adap = new OrderNewAdapter(this, sortedList);
        order_new.setAdapter(order_new_adap);
    }

    // cập nhật data khi kéo
    private void loadOrder() {
        runOnUiThread(() -> {
            BindData();
        });
    }
}