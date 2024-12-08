package com.example.orderfood.component;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ClipDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.models.Product;
import com.example.orderfood.services.BaseNoBottomActivity;
import com.example.orderfood.services.CartActivity;
import com.example.orderfood.services.ProductDetailActivity;
import com.example.orderfood.sqlLite.dao.CartDAO;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class product_adapter extends RecyclerView.Adapter<product_adapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    public  int  n =1;

    public product_adapter(List<Product> productList) {
        this.productList = productList;
    }
    public product_adapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_special, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        Glide.with(holder.itemView.getContext())
                .load(product.getImage().get(0))
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .into(holder.productImage);
        // sử dụng Glide để set ảnh lên giao diện

        holder.productImage.setOnClickListener(v -> {

            // Tạo hiệu ứng scale
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1f, 1.5f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1f, 1.5f, 1f);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(scaleX, scaleY);
            animatorSet.setDuration(600); // Thời gian hiệu ứng

            // Lắng nghe sự kiện kết thúc hiệu ứng
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    // Gửi Intent sang ProductDetailActivity
                    Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
                    intent.putExtra("productId", product.getId());
                    v.getContext().startActivity(intent);
                }
            });

            // Bắt đầu hiệu ứng
            animatorSet.start();
        });
        //  gửi id  cho trang product detail

        holder.productPrice.setText(formatVND((product.getPrice())));
        holder.btnAddFavorite.setOnClickListener(v -> {

            // Tạo hiệu ứng scale
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1f, 1.5f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1f, 1.5f, 1f);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(scaleX, scaleY);
            animatorSet.setDuration(300); // Thời gian hiệu ứng

            change_favorite(holder.btnAddFavorite);
            Toast.makeText(v.getContext(), "Thêm vào yêu thích: " + product.getName(), Toast.LENGTH_SHORT).show();
            animatorSet.start();
        });
        holder.btnAddCart.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view){
                CartDAO cartDAO = new CartDAO(holder.itemView.getContext());
                cartDAO.deleteAll();
                cartDAO.addProduct(product.getId(),product.getName(),1,product.getImage().get(0));
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f, 1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.5f, 1f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(scaleX, scaleY);
                animatorSet.setDuration(300); // Thời gian hiệu ứng
                Toast.makeText(view.getContext(), "Thêm vào giỏ hàng: " + product.getName(), Toast.LENGTH_SHORT).show();
                animatorSet.start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice;
        ImageView productImage;
        ImageView btnAddCart, btnAddFavorite;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productImage = itemView.findViewById(R.id.product_image);
            btnAddFavorite = itemView.findViewById(R.id.btnAddFavorite);
            btnAddCart = itemView.findViewById(R.id.btnAddCart);

        }

    }

    private String formatVND(Double price) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(price) + " VND";
    }
    private   void change_favorite(ImageView imageView) {
        if (n == 1) {
            imageView.setImageResource(R.drawable.ic_baseline_favorite_24);
            n = 2;
        } else {
            n = 1;
            imageView.setImageResource(R.drawable.ic_baseline_favorite_click_24);
        }


    }

}
