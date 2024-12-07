package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.orderfood.R;
import com.example.orderfood.component.HistoryOrderAdapter;
import com.example.orderfood.component.ProductCartAdapter;
import com.example.orderfood.data.HandleData;
import com.example.orderfood.data.OrderUtil;
import com.example.orderfood.models.Order;
import com.example.orderfood.models.dto.CartDTO;

import java.util.ArrayList;
import java.util.List;

public class HistoryOrderActivity  extends BaseTopBottomViewActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getLayoutInflater().inflate(R.layout.activity_history_order, findViewById(R.id.content_frame_top_bot));

        BindData();
    }

    private void BindData()
    {
        // lấy danh sách order của account hiện tại
        ArrayList<Order> odList = OrderUtil.getAllOrdersByAccountId(this);

        RecyclerView history_order = findViewById(R.id.history_order_container);
        history_order.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        if (odList == null) {
            odList = new ArrayList<>(); // Nếu danh sách trả về null, khởi tạo một ArrayList rỗng
        }

        HistoryOrderAdapter historyOrderAdapter = new HistoryOrderAdapter(this, odList);
        history_order.setAdapter(historyOrderAdapter);
    }
}