package com.example.orderfood.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.models.dto.CartDTO;
import com.example.orderfood.sqlLite.dao.CartDAO;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductOrderAdapter extends RecyclerView.Adapter<ProductOrderAdapter.ProductOrderViewHolder> {

    private Context context;
    private ArrayList<CartDTO> CartDTOs;
    private CartDAO cart_dao;

    public ProductOrderAdapter(Context context, ArrayList<CartDTO> cartDTOs) {
        this.context = context;
        CartDTOs = cartDTOs;
        cart_dao = new CartDAO(context);
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

        public ProductOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            CartProImage = itemView.findViewById(R.id.order_product_icon);
            cartProName = itemView.findViewById(R.id.order_pro_name);
            cartProQuantity = itemView.findViewById(R.id.order_pro_quantity);
            cartProPrice = itemView.findViewById(R.id.order_pro_price);
        }
    }
}
