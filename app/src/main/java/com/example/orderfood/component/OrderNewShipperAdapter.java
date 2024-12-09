package com.example.orderfood.component;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderfood.R;
import com.example.orderfood.data.OrderUtil;
import com.example.orderfood.models.Order;
import com.example.orderfood.services.CartActivity;
import com.example.orderfood.services.OrderActivity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrderNewShipperAdapter extends RecyclerView.Adapter<OrderNewShipperAdapter.OrderNewShipperViewHolder> {
    private Context context;
    private ArrayList<Order> orders;

    public OrderNewShipperAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderNewShipperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ship_order_new, parent, false);
        return new OrderNewShipperAdapter.OrderNewShipperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderNewShipperViewHolder holder, int position) {
        Order item = orders.get(position);

        holder.shipOrderCode.setText("Mã Đơn Hàng: #" + item.getId());
        holder.shipOrderCode.setOnClickListener(new View.OnClickListener() {
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

        holder.shipOrderPrice.setText("Tổng giá: " + NumberFormat.getInstance(Locale.getDefault()).format(item.getTotalPrice()) + " VNĐ");

        int status = item.getStatus();
        Log.d("OrderStatus", "Status: " + status);
        switch (status) { // case 2 vì store đã xác nhân => shiper vô nhận
            case 2: {
                holder.shipOrderStatus.setText("Nhận Đơn");
                holder.shipOrderStatus.setBackgroundResource(R.color.yellow);
                holder.shipOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.black));
                break;
            }
            case 3: {
                holder.shipOrderStatus.setText("Đã Nhận");
                holder.shipOrderStatus.setBackgroundResource(R.color.grean);
                holder.shipOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.white));
                break;
            }
            case 4: {
                holder.shipOrderStatus.setText("Đã Hoàn Thành");
                holder.shipOrderStatus.setBackgroundResource(R.color.teal_700);
                holder.shipOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.white));
                break;
            }
            default:
                holder.shipOrderStatus.setText("Lỗi Status");
                holder.shipOrderStatus.setBackgroundResource(R.color.black);
                holder.shipOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.white));
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
        holder.shipOrderDate.setText(formattedDate);
        String address = item.getAddress();
        if (address.length() > 15) {
            address = address.substring(0, 12) + "...";
        }
        holder.shipOrderAddress.setText("Địa chỉ: " + address);


        holder.shipOrderStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // done hoặc hủy
                if(item.getStatus() == 4 ||item.getStatus() == 5 )
                {
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customTitleView = inflater.inflate(R.layout.custom_dialog_icon, null);

                // Tạo hiệu ứng scale
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(holder.shipOrderStatus, "scaleX", 1f, 1.5f, 1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(holder.shipOrderStatus, "scaleY", 1f, 1.5f, 1f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(scaleX, scaleY);
                animatorSet.setDuration(300); // Thời gian hiệu ứng

                builder.setCustomTitle(customTitleView)
                        .setMessage("Xác nhận đơn hàng?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Hiển thị ProgressDialog
                                ProgressDialog progressDialog = new ProgressDialog(context);
                                progressDialog.setMessage("Đang xử lý, vui lòng chờ...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                new Handler().postDelayed(() -> {

                                    // shiper nhận hàng
                                    if (item.getStatus() == 2) {
                                        // Thực hiện cập nhật trạng thái đơn hàng
                                        boolean success = OrderUtil.ChangeStatusOrder(item.getId(), 3);

                                        // Ẩn ProgressDialog sau khi xử lý xong
                                        progressDialog.dismiss();

                                        // Hiển thị Toast thông báo
                                        if (success) {
                                            Toast.makeText(context, "Đã nhận đơn hàng!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Có lỗi xảy ra, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                                        }

                                        // Thiết lập lắng nghe khi hiệu ứng kết thúc
                                        AnimatorSet animatorSet = new AnimatorSet();
                                        animatorSet.addListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                int position = holder.getAdapterPosition(); // Lấy vị trí của item trong adapter

                                                // Kiểm tra xem vị trí có hợp lệ không
                                                if (position != RecyclerView.NO_POSITION) {
                                                    orders.remove(position);

                                                    // Thông báo Adapter rằng dữ liệu đã thay đổi tại vị trí này
                                                    notifyItemRemoved(position);
                                                    notifyItemRangeChanged(position, orders.size()); // Cập nhật lại danh sách
                                                }
                                            }
                                        });

                                        // Bắt đầu hiệu ứng
                                        animatorSet.start();
                                    }

                                    // shipper hoàn thành đơn hàng
                                    if (item.getStatus() == 3) {
                                        // Thực hiện cập nhật trạng thái đơn hàng
                                        boolean success = OrderUtil.ChangeStatusOrder(item.getId(), 4);

                                        // Ẩn ProgressDialog sau khi xử lý xong
                                        progressDialog.dismiss();

                                        // Hiển thị Toast thông báo
                                        if (success) {
                                            Toast.makeText(context, "Đã hoàn thành đơn hàng!", Toast.LENGTH_SHORT).show();
                                            item.setStatus(4);
                                            notifyItemChanged(holder.getAdapterPosition());

                                        } else {
                                            Toast.makeText(context, "Có lỗi xảy ra, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                                        }
                                    }


                                }, 1000); // Thời gian delay 1 giây để giả lập quá trình xử lý
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderNewShipperViewHolder extends RecyclerView.ViewHolder {
        public TextView shipOrderCode;
        public TextView shipOrderPrice;
        public TextView shipOrderStatus;
        public TextView shipOrderDate;
        public TextView shipOrderAddress;

        public OrderNewShipperViewHolder(@NonNull View itemView) {
            super(itemView);
            shipOrderCode = itemView.findViewById(R.id.ship_order_code);
            shipOrderPrice = itemView.findViewById(R.id.ship_order_price);
            shipOrderStatus = itemView.findViewById(R.id.ship_order_status);
            shipOrderDate = itemView.findViewById(R.id.ship_order_date);
            shipOrderAddress = itemView.findViewById(R.id.ship_order_address);
        }
    }
}
