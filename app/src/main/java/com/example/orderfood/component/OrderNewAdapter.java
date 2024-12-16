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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderfood.R;
import com.example.orderfood.data.CurrentUser;
import com.example.orderfood.data.NotiUtil;
import com.example.orderfood.data.OrderUtil;
import com.example.orderfood.models.Order;
import com.example.orderfood.services.ListOrderActivity;
import com.example.orderfood.services.OrderActivity;
import com.example.orderfood.services.OrderNewActivity;
import com.example.orderfood.services.ShipperHistoryOrderActivity;
import com.example.orderfood.services.ShipperHomeActivity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrderNewAdapter extends RecyclerView.Adapter<OrderNewAdapter.OrderNewShipperViewHolder> {
    private Context context;
    private ArrayList<Order> orders;

    public OrderNewAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
        CurrentUser.init(context);
    }

    @NonNull
    @Override
    public OrderNewShipperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ship_order_new, parent, false);
        return new OrderNewAdapter.OrderNewShipperViewHolder(view);
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
        Date date = item.getDate();
        String formattedDate = "";

        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            formattedDate = dateFormat.format(date);
        } else {
            formattedDate = "N/A";
        }

        int status = item.getStatus();

        // đối với shipper thì status hiển thị như thế này
        if (context instanceof ShipperHomeActivity || context instanceof ShipperHistoryOrderActivity) {
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
        }

        // đối với side manager thì hiển thị
        if (context instanceof OrderNewActivity || context instanceof ListOrderActivity) {
            switch (status) { // case 2 vì store đã xác nhân => shiper vô nhận
                case 1: {
                    holder.shipOrderStatus.setText("Nhận Đơn");
                    holder.shipOrderStatus.setBackgroundResource(R.color.purple_500);
                    holder.shipOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.white));
                    break;
                }
                case 2: {
                    holder.shipOrderStatus.setText("Chờ Shipper");
                    holder.shipOrderStatus.setBackgroundResource(R.color.yellow);
                    holder.shipOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.black));
                    break;
                }
                case 3: {
                    holder.shipOrderStatus.setText("Shipper Đã Nhận");
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
                case 5: {
                    holder.shipOrderStatus.setText("Hủy");
                    holder.shipOrderStatus.setBackgroundResource(R.color.selected_rating_color);
                    holder.shipOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.white));
                    break;
                }
                default:
                    holder.shipOrderStatus.setText("Lỗi Status");
                    holder.shipOrderStatus.setBackgroundResource(R.color.black);
                    holder.shipOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.white));
                    break;
            }
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
                if (item.getStatus() == 4 || item.getStatus() == 5) {
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

                                    // đối với shipper thì là đổi status như này
                                    if (context instanceof ShipperHomeActivity || context instanceof ShipperHistoryOrderActivity) {

                                        // shiper nhận hàng
                                        if (item.getStatus() == 2) {
                                            // Thực hiện cập nhật trạng thái đơn hàng
                                            boolean success = OrderUtil.ChangeStatusOrder(item.getId(), 3);

                                            // Ẩn ProgressDialog sau khi xử lý xong
                                            progressDialog.dismiss();

                                            // Hiển thị Toast thông báo
                                            if (success) {
                                                Toast.makeText(context, "Đã nhận đơn hàng!", Toast.LENGTH_SHORT).show();

                                                // thông báo về store
                                                NotiUtil.SendNotiToRole(0,
                                                        "Shipper Đã Nhận",
                                                        "Shipper " + CurrentUser.getName() + " đã nhận đơn: #" + item.getId(),
                                                        new Date(), item.getId());

                                                // gửi thông báo đến customer
                                                NotiUtil.SendNotiToAccount(2,
                                                        "Shipper đã nhận",
                                                        "Đơn hàng: #" + item.getId() + " shipper đã nhận \uD83D\uDE1A ",
                                                        new Date(), item.getId(), item.getCustomerId());

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

                                                // thông báo về store
                                                NotiUtil.SendNotiToRole(0,
                                                        "Shipper Đã Hoàn Thành", "Shipper " + CurrentUser.getName() + " đã hoàn thành đơn: #" + item.getId(),
                                                        new Date(), item.getId());

                                                // gửi thông báo đến customer
                                                NotiUtil.SendNotiToAccount(2,
                                                        "Đơn hàng thành công!",
                                                        "Đơn hàng: #" + item.getId() + " đã thành công, cảm ơn bạn đã ủng hộ \uD83C\uDF86 ",
                                                        new Date(), item.getId(), item.getCustomerId());

                                                item.setStatus(4);
                                                int currentPosition = holder.getAdapterPosition();
                                                if (currentPosition != RecyclerView.NO_POSITION) {
                                                    notifyItemChanged(currentPosition); // Sử dụng currentPosition thay vì position
                                                }

                                            } else {
                                                Toast.makeText(context, "Có lỗi xảy ra, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    // Manager thì là đổi status như này
                                    if (context instanceof OrderNewActivity || context instanceof ListOrderActivity) {
                                        // manager nhận hàng
                                        if (item.getStatus() == 1) {

                                            // Thực hiện cập nhật trạng thái đơn hàng
                                            boolean success = false;
                                            int currentPosition = holder.getAdapterPosition();
                                            if (currentPosition != RecyclerView.NO_POSITION) {
                                                success = OrderUtil.ChangeStatusOrder(orders.get(currentPosition).getId(), 2);
                                                if (success) {
                                                    orders.get(currentPosition).setStatus(2);
                                                    notifyItemChanged(currentPosition); // Sử dụng currentPosition thay vì position
                                                }
                                            }

                                            // Ẩn ProgressDialog sau khi xử lý xong
                                            progressDialog.dismiss();

                                            // Hiển thị Toast thông báo
                                            if (success) {
                                                Toast.makeText(context, "Đã nhận đơn hàng!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "Có lỗi xảy ra, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                                            }

                                            // niếu là page nhận đơn thì nhận xong xóa item
                                            if (context instanceof OrderNewActivity) {

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

                                                animatorSet.start();
                                            }

                                            if (success) {
                                                // gửi thông báo đến tất cả shipper
                                                NotiUtil.SendNotiToRole(1,
                                                        "Cửa hàng thông báo cho shipper có đơn mới",
                                                        "Bạn có đơn mới kìa: #" + item.getId(),
                                                        new Date(), item.getId());

                                                // gửi thông báo đến customer
                                                NotiUtil.SendNotiToAccount(2,
                                                        "Store đã xác nhận đơn hàng",
                                                        "Đơn hàng: #" + item.getId() + " cửa hàng đã xác nhận, vui lòng đợi!",
                                                        new Date(), item.getId(), item.getCustomerId());
                                            }

                                            notifyDataSetChanged();
                                        }

                                        progressDialog.dismiss();
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
