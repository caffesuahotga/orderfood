package com.example.orderfood.component;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderfood.R;
import com.example.orderfood.models.Order;
import com.example.orderfood.models.dto.CartDTO;
import com.example.orderfood.services.OrderActivity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

        holder.orderCode.setText("Mã Đơn Hàng: #" + item.getId());
        holder.orderCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hiệu ứng scale nhỏ
                view.animate()
                        .scaleX(0.9f)
                        .scaleY(0.9f)
                        .setDuration(100)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                // Hiệu ứng scale lớn
                                view.animate()
                                        .scaleX(1.0f)
                                        .scaleY(1.0f)
                                        .setDuration(100)
                                        .start();

                                // Hiển thị ProgressDialog
                                ProgressDialog progressDialog = new ProgressDialog(context);
                                progressDialog.setMessage("Đang chuyển trang, vui lòng chờ...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                new Handler().postDelayed(() -> {
                                    Intent gotoDetail = new Intent(context, OrderActivity.class);
                                    gotoDetail.putExtra("orderId", item.getId());
                                    context.startActivity(gotoDetail);

                                    // Ẩn ProgressDialog sau khi chuyển trang
                                    progressDialog.dismiss();
                                }, 500); // Thời gian delay 0.5 giây trước khi chuyển trang
                            }
                        })
                        .start();
            }
        });

        holder.orderPrice.setText("Tổng giá: " + NumberFormat.getInstance(Locale.getDefault()).format(item.getTotalPrice()) + " VNĐ");

        int status = item.getStatus();
        Log.d("OrderStatus", "Status: " + status);
        switch (status) {
            case 1: {
                holder.orderStatus.setText("Chờ xác nhận");
                holder.orderStatus.setBackgroundResource(R.color.yellow);
                holder.orderStatus.setTextColor(ContextCompat.getColor(context, R.color.black));
                break;
            }
            default:
                holder.orderStatus.setText("Lỗi Status");
                holder.orderStatus.setBackgroundResource(R.color.black);
                holder.orderStatus.setTextColor(ContextCompat.getColor(context, R.color.white));
                break;
        }


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
