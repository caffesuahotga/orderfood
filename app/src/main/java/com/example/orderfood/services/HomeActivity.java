package com.example.orderfood.services;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;  // Thêm GridLayoutManager để tạo lưới

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.orderfood.R;
import com.example.orderfood.component.category_adapter;

import com.example.orderfood.component.product_adapter;
import com.example.orderfood.data.NotiUtil;
import com.example.orderfood.models.Category;
import com.example.orderfood.data.HandleData;
import com.example.orderfood.models.Product;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends BaseTopBottomViewActivity {

    private RecyclerView recyclerView;
    private category_adapter categoryAdapter;
    private List<Category> categoryList;
    private RecyclerView recyclerView1;
    private product_adapter productAdapter;
    private List<Product> productList;

    private static final String TAG = "HomeActivity";
    private static final int REQUEST_CODE_ACCESS_NOTIFICATION_POLICY = 1;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static boolean isFirstRun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getLayoutInflater().inflate(R.layout.activity_home, findViewById(R.id.content_frame));

        // Khởi tạo ImageSlider và thêm các slide hình ảnh
        ImageSlider imageSlider = findViewById(R.id.image_slider);
        ArrayList<SlideModel> slideModelArrayList = new ArrayList<>();

        slideModelArrayList.add(new SlideModel(R.drawable.image_slider_1, ScaleTypes.FIT));
        slideModelArrayList.add(new SlideModel(R.drawable.image_slider_2, ScaleTypes.FIT));
        slideModelArrayList.add(new SlideModel(R.drawable.image_slider_3, ScaleTypes.FIT));
        slideModelArrayList.add(new SlideModel(R.drawable.image_slider_4, ScaleTypes.FIT));

        imageSlider.setImageList(slideModelArrayList, ScaleTypes.FIT);


        recyclerView = findViewById(R.id.view_item);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);  // Áp dụng LayoutManager

        // Khởi tạo danh sách category
        HandleData handleData = new HandleData();
        categoryList = handleData.getAllCategories();
        categoryAdapter = new category_adapter(categoryList);
        recyclerView.setAdapter(categoryAdapter);
        // Set adapter cho RecyclerView


        recyclerView1 = findViewById(R.id.view_product);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 2);
        recyclerView1.setLayoutManager(gridLayoutManager1);

        HandleData handleData1 = new HandleData();
        productList = handleData1.getAllProducts();
        productAdapter = new product_adapter(productList);
        recyclerView1.setAdapter(productAdapter);

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
                    NotiUtil.saveTokenFCMAccount(HomeActivity.this);
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
}

