package com.example.orderfood.component;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.data.CurrentUser;
import com.example.orderfood.models.dto.CartDTO;
import com.example.orderfood.sqlLite.dao.CartDAO;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductOrderAdapter extends RecyclerView.Adapter<ProductOrderAdapter.ProductOrderViewHolder> {

    private Context context;
    private ArrayList<CartDTO> CartDTOs;
    private CartDAO cart_dao;
    private boolean isFeedback;

    public ProductOrderAdapter(Context context, ArrayList<CartDTO> cartDTOs) {
        this.context = context;
        CartDTOs = cartDTOs;
        cart_dao = new CartDAO(context);
    }

    public ProductOrderAdapter(Context context, ArrayList<CartDTO> cartDTOs, boolean fb) {
        this.context = context;
        CartDTOs = cartDTOs;
        cart_dao = new CartDAO(context);
        isFeedback = fb;
    }

    @NonNull
    @Override
    public ProductOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_order, parent, false);
        return new ProductOrderAdapter.ProductOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductOrderViewHolder holder, int position) {
        CartDTO item = CartDTOs.get(position);

        // nạp ảnh product
        Glide.with(holder.itemView.getContext())
                .load(item.getImage())
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .into(holder.CartProImage);

        holder.cartProName.setText(item.getName());
        holder.cartProQuantity.setText(item.getQuantity() + "");
        holder.cartProPrice.setText("Giá: " + NumberFormat.getInstance(Locale.getDefault()).format(item.getPrice() * item.getQuantity()) + " VNĐ");

        // feed back
        // Đặt giá trị số sao hiện tại
        holder.product_order_rating.setRating((float) item.getStar());

        // Lắng nghe thay đổi từ RatingBar
        holder.product_order_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    item.setStar((int) rating); // Cập nhật số sao vào item
                }
            }
        });

        if((CurrentUser.getRole() == 2 || CurrentUser.getRole() == 0) && isFeedback) // customer , admin mới hiện
        {
            holder.ProductOrderFeedback.setText(item.getFeedback());
            holder.ProductOrderFeedback.setVisibility(View.VISIBLE);

            // Lắng nghe thay đổi từ EditText
            holder.ProductOrderFeedback.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    item.setFeedback(s.toString()); // Cập nhật feedback vào item
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        }else
        {
            holder.product_item_card_feedback.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return CartDTOs.size();
    }

    public class ProductOrderViewHolder  extends RecyclerView.ViewHolder
    {
        public ImageView CartProImage;
        public TextView cartProName;
        public TextView cartProQuantity;
        public TextView cartProPrice;
        public EditText ProductOrderFeedback;
        public RatingBar product_order_rating;
        public LinearLayout product_item_card_feedback;

        public ProductOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            CartProImage = itemView.findViewById(R.id.order_product_icon);
            cartProName = itemView.findViewById(R.id.order_pro_name);
            cartProQuantity = itemView.findViewById(R.id.order_pro_quantity);
            cartProPrice = itemView.findViewById(R.id.order_pro_price);
            ProductOrderFeedback = itemView.findViewById(R.id.product_order_feedback);
            product_order_rating  = itemView.findViewById(R.id.product_order_rating);
            product_item_card_feedback = itemView.findViewById(R.id.product_item_card_feedback);
        }
    }
}
