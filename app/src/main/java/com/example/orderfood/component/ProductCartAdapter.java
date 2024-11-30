package com.example.orderfood.component;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.models.dto.CartDTO;
import com.example.orderfood.models.dto.FeedBackDTO;
import com.example.orderfood.services.CartActivity;
import com.example.orderfood.services.ProductDetailActivity;
import com.example.orderfood.sqlLite.dao.CartDAO;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductCartAdapter extends RecyclerView.Adapter<ProductCartAdapter.ProductCartViewHolder> {
    private Context context;
    private ArrayList<CartDTO> CartDTOs;
    CartDAO cartDAO2;

    public ProductCartAdapter(Context context, ArrayList<CartDTO> cartDTOs) {
        this.context = context;
        CartDTOs = cartDTOs;
        cartDAO2 = new CartDAO(context);
    }

    @NonNull
    @Override
    public ProductCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate giao diện, từ xml =>  giao diện
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_cart, parent, false);
        return new ProductCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCartViewHolder holder, int position) {
        CartDTO item = CartDTOs.get(position);

        // nạp ảnh product
        Glide.with(holder.itemView.getContext())
                .load(item.getImage())
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .into(holder.CartProImage);

        holder.cartProName.setText(item.getName());

        // ẩn ảnh & tên đi vào detail
        holder.CartProImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hiển thị ProgressDialog với style tùy chỉnh
                ProgressDialog progressDialog = new ProgressDialog(context, R.style.CustomProgressDialog);
                progressDialog.setMessage(context.getString(R.string.loading_message)); // Đặt thông điệp loading từ strings.xml
                progressDialog.setCancelable(false); // Không cho phép hủy bằng cách chạm ra ngoài
                progressDialog.show();

                // Giả lập quá trình load dữ liệu
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss(); // Đóng ProgressDialog

                        // Chuyển tới ProductDetailActivity
                        Intent intent = new Intent(context, ProductDetailActivity.class);
                        intent.putExtra("productId", item.getProductID()); // Truyền productId vào intent
                        context.startActivity(intent);
                    }
                }, 3000); // Thời gian giả lập loading, có thể điều chỉnh tùy theo nhu cầu
            }
        });

        holder.cartProName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hiển thị ProgressDialog với style tùy chỉnh
                ProgressDialog progressDialog = new ProgressDialog(context, R.style.CustomProgressDialog);
                progressDialog.setMessage(context.getString(R.string.loading_message)); // Đặt thông điệp loading từ strings.xml
                progressDialog.setCancelable(false); // Không cho phép hủy bằng cách chạm ra ngoài
                progressDialog.show();

                // Giả lập quá trình load dữ liệu
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss(); // Đóng ProgressDialog

                        // Chuyển tới ProductDetailActivity
                        Intent intent = new Intent(context, ProductDetailActivity.class);
                        intent.putExtra("productId", item.getProductID()); // Truyền productId vào intent
                        context.startActivity(intent);
                    }
                }, 3000); // Thời gian giả lập loading, có thể điều chỉnh tùy theo nhu cầu
            }
        });



        holder.cartProQuantity.setText(item.getQuantity() + "");
        holder.cartProPrice.setText("Giá: " + NumberFormat.getInstance(Locale.getDefault()).format(item.getPrice() * item.getQuantity()) + " VNĐ");

        holder.checkPro.setChecked(item.isSelected());
        holder.checkPro.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setSelected(isChecked);

            if (context instanceof CartActivity) {
                ((CartActivity) context).updateTotalPrice(CartDTOs);
            }
        });

        // tăng giảm số lượng
        // Trong onClickListener:
        holder.cartQuantityReduce.setOnClickListener(view -> {
            animateButton(holder.cartQuantityReduce); // Thêm hiệu ứng
            int currentQuantity = item.getQuantity();
            if (currentQuantity > 1) {
                item.setQuantity(currentQuantity - 1);
                holder.cartProQuantity.setText(item.getQuantity() + "");
                holder.cartProPrice.setText("Giá: " + NumberFormat.getInstance(Locale.getDefault()).format(item.getPrice() * item.getQuantity()) + " VNĐ");
                if (context instanceof CartActivity) {
                    ((CartActivity) context).updateTotalPrice(CartDTOs);
                }
            }
        });

        holder.cartQuantityInc.setOnClickListener(view -> {
            animateButton(holder.cartQuantityInc); // Thêm hiệu ứng
            int currentQuantity = item.getQuantity();
            item.setQuantity(currentQuantity + 1);
            holder.cartProPrice.setText("Giá: " + NumberFormat.getInstance(Locale.getDefault()).format(item.getPrice() * item.getQuantity()) + " VNĐ");
            holder.cartProQuantity.setText(item.getQuantity() + "");
            if (context instanceof CartActivity) {
                ((CartActivity) context).updateTotalPrice(CartDTOs);
            }
        });

        //delete
        holder.cartDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo hiệu ứng scale
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(holder.cartDelete, "scaleX", 1f, 1.5f, 1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(holder.cartDelete, "scaleY", 1f, 1.5f, 1f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(scaleX, scaleY);
                animatorSet.setDuration(300); // Thời gian hiệu ứng

                // Thiết lập lắng nghe khi hiệu ứng kết thúc
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        int position = holder.getAdapterPosition(); // Lấy vị trí của item trong adapter

                        // Kiểm tra xem vị trí có hợp lệ không
                        if (position != RecyclerView.NO_POSITION) {
                            CartDTOs.remove(position);
                            cartDAO2.deleteProduct(item.getProductID()); // xóa khỏi db

                            // Thông báo Adapter rằng dữ liệu đã thay đổi tại vị trí này
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, CartDTOs.size()); // Cập nhật lại danh sách
                            if (context instanceof CartActivity) {
                                ((CartActivity) context).updateTotalPrice(CartDTOs);
                            }
                        }
                    }
                });

                // Bắt đầu hiệu ứng
                animatorSet.start();
            }
        });


    }

    @Override
    public int getItemCount() {
        return CartDTOs.size();
    }

    public class ProductCartViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkPro;

        public ImageView CartProImage;
        public TextView cartProName;
        public TextView cartProQuantity;
        public TextView cartProPrice;

        public Button cartQuantityReduce;
        public Button cartQuantityInc;
        public ImageView cartDelete;

        public ProductCartViewHolder(@NonNull View itemView) {
            super(itemView);
            checkPro = itemView.findViewById(R.id.cart_pro_check);
            CartProImage = itemView.findViewById(R.id.cart_product_icon);
            cartProName = itemView.findViewById(R.id.cart_pro_name);
            cartProQuantity = itemView.findViewById(R.id.cart_pro_quantity);
            cartProPrice = itemView.findViewById(R.id.cart_pro_price);
            cartQuantityReduce = itemView.findViewById(R.id.cart_quantity_reduce);
            cartQuantityInc = itemView.findViewById(R.id.cart_quantity_inc);
            cartDelete = itemView.findViewById(R.id.cart_delete_product);
        }
    }

    private void animateButton(View button) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 0.9f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 0.9f, 1f);
        scaleX.setDuration(200);
        scaleY.setDuration(200);
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleX.start();
        scaleY.start();
    }
}
