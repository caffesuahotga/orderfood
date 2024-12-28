package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.example.orderfood.R;
import com.example.orderfood.component.OrderNewAdapter;
import com.example.orderfood.data.OrderUtil;
import com.example.orderfood.models.Order;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ListOrderActivity extends BaseTopBottomViewActivity {
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_list_order, findViewById(R.id.content_frame_top_bot));

        BindData();

        // Xử lý refresh
        swipeRefreshLayout = findViewById(R.id.side_bar_list_order_page_refresh);
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
        //

    }

    private void BindData() {
        // lấy danh sách order của account hiện tại
        ArrayList<Order> odList = OrderUtil.getAllListOrdersForManager();
        ArrayList<Order> sortedList = (ArrayList<Order>) odList.stream()
                .sorted(Comparator.comparingInt(Order::getId).reversed())
                .collect(Collectors.toList());

        RecyclerView order_new = findViewById(R.id.side_bar_list_order_container);
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