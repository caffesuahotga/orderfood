package com.example.orderfood.component;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderfood.R;
import com.example.orderfood.models.Noti;
import com.example.orderfood.models.Order;
import com.example.orderfood.services.OrderActivity;
import com.example.orderfood.services.ProductDetailActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.NotiViewHolder> {

    private Context context;
    private ArrayList<Noti> notiList;

    public NotiAdapter(Context context, ArrayList<Noti> notiList) {
        this.context = context;
        this.notiList = notiList;
    }

    @NonNull
    @Override
    public NotiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_noti, parent, false);
        return new NotiAdapter.NotiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotiViewHolder holder, int position) {
        Noti item = notiList.get(position);
        holder.noti_title.setText(item.getTitle());
        holder.noti_title.setOnClickListener(new View.OnClickListener() {
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
                                    gotoDetail.putExtra("orderId", item.getOrderId());
                                    context.startActivity(gotoDetail);

                                    // Ẩn ProgressDialog sau khi chuyển trang
                                    progressDialog.dismiss();
                                }, 500); // Thời gian delay 0.5 giây trước khi chuyển trang
                            }
                        })
                        .start();
            }
        });


        holder.noti_content.setText(item.getContent());
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
        holder.noti_date.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return notiList.size();
    }

    public class NotiViewHolder extends RecyclerView.ViewHolder{
        public TextView noti_title;
        public TextView noti_content;
        public TextView noti_date;

        public NotiViewHolder(@NonNull View itemView) {
            super(itemView);
            noti_title = itemView.findViewById(R.id.noti_title);
            noti_content = itemView.findViewById(R.id.noti_content);
            noti_date = itemView.findViewById(R.id.noti_date);
        }
    }
}
