package com.example.orderfood.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderfood.R;
import com.example.orderfood.models.Order;
import com.example.orderfood.models.dto.CartDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryOrderAdapter extends RecyclerView.Adapter<HistoryOrderAdapter.HistoryOrderViewHolder> {
    private Context context;
    private ArrayList<Order> orders;

    public HistoryOrderAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public HistoryOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_order, parent, false);
        return new HistoryOrderAdapter.HistoryOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryOrderViewHolder holder, int position) {
        Order item = orders.get(position);

        holder.orderCode.setText(" Mã Đơn Hàng: #" + item.getId());
        holder.orderPrice.setText(item.getTotalPrice() + "");
        holder.orderStatus.setText(item.getStatus() + "");

        Date date = item.getDate();
        String formattedDate = "";

        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            formattedDate = dateFormat.format(date);
        } else {
            // Bạn có thể hiển thị thông báo mặc định hoặc để trống
            formattedDate = "N/A";
        }

        // Gán giá trị định dạng vào TextView
        holder.orderDate.setText(formattedDate);


    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class HistoryOrderViewHolder extends RecyclerView.ViewHolder {
        public TextView orderCode;
        public TextView orderPrice;
        public TextView orderStatus;
        public TextView orderDate;

        public HistoryOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderCode = itemView.findViewById(R.id.order_code);
            orderPrice = itemView.findViewById(R.id.order_price);
            orderStatus = itemView.findViewById(R.id.order_status);
            orderDate = itemView.findViewById(R.id.order_date);
        }
    }
}
