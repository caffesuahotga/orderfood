package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.example.orderfood.R;
import com.example.orderfood.component.HistoryOrderAdapter;
import com.example.orderfood.component.ProductCartAdapter;
import com.example.orderfood.data.HandleData;
import com.example.orderfood.data.OrderUtil;
import com.example.orderfood.models.Order;
import com.example.orderfood.models.dto.CartDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryOrderActivity  extends BaseTopBottomViewActivity  {
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_history_order, findViewById(R.id.content_frame_top_bot));

        BindData();

        // Xử lý refresh
        swipeRefreshLayout = findViewById(R.id.history_order_page_refresh);
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
        // lấy danh sách order của account hiện tại
        ArrayList<Order> odList = OrderUtil.getAllOrdersByAccountId(this);
        ArrayList<Order> sortedList = (ArrayList<Order>) odList.stream()
                .sorted(Comparator.comparingInt(Order::getId).reversed())
                .collect(Collectors.toList());

        RecyclerView history_order = findViewById(R.id.history_order_container);
        history_order.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        if (odList == null) {
            odList = new ArrayList<>(); // Nếu danh sách trả về null, khởi tạo một ArrayList rỗng
        }

        HistoryOrderAdapter historyOrderAdapter = new HistoryOrderAdapter(this, sortedList);
        history_order.setAdapter(historyOrderAdapter);
    }

    // cập nhật data khi kéo
    private void loadHistoryOrder() {
        runOnUiThread(() -> {
            BindData();
        });
    }
}