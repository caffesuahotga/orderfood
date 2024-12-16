package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.example.orderfood.R;
import com.example.orderfood.component.HistoryOrderAdapter;
import com.example.orderfood.component.NotiAdapter;
import com.example.orderfood.data.CurrentUser;
import com.example.orderfood.data.NotiUtil;
import com.example.orderfood.data.OrderUtil;
import com.example.orderfood.models.Noti;
import com.example.orderfood.models.Order;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class NotiActivity extends BaseTopBottomViewActivity {
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_noti, findViewById(R.id.content_frame_top_bot));
        CurrentUser.init(this);
        BindData();

        // Xử lý refresh
        swipeRefreshLayout = findViewById(R.id.noti_page_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Show loading spinner
            swipeRefreshLayout.setRefreshing(true);

            // Call lại API hoặc logic tải dữ liệu trong background thread
            new Thread(() -> {

                loadHistoryOrder();

                // Sau khi dữ liệu được tải, thực hiện thao tác trên UI thread
                runOnUiThread(() -> {
                    // Kết thúc hiệu ứng refresh
                    swipeRefreshLayout.setRefreshing(false);
                });
            }).start();
        });
    }

    private void BindData()
    {
        int id = CurrentUser.getId();
        // lấy danh sách order của account hiện tại
        ArrayList<Noti> notiList = NotiUtil.GetNotiByAccId(CurrentUser.getId());
        ArrayList<Noti> sortedList = (ArrayList<Noti>) notiList.stream()
                .sorted(Comparator.comparing(Noti::getDate).reversed())
                .collect(Collectors.toList());

        RecyclerView noti_rec = findViewById(R.id.noti_container);
        noti_rec.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        if (notiList == null) {
            notiList = new ArrayList<>(); // Nếu danh sách trả về null, khởi tạo một ArrayList rỗng
        }

        NotiAdapter notiAdapter = new NotiAdapter(this, sortedList);
        noti_rec.setAdapter(notiAdapter);
    }

    // cập nhật data khi kéo
    private void loadHistoryOrder() {
        runOnUiThread(() -> {
            BindData();
        });
    }
}