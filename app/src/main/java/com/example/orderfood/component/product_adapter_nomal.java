package com.example.orderfood.component;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.security.Provider;
import java.util.ArrayList;
import java.util.Locale;
import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.models.Product;
import com.example.orderfood.models.dto.CartDTO;
import com.example.orderfood.models.dto.FavoriteDTO;
import com.example.orderfood.services.CartActivity;
import com.example.orderfood.services.ProductDetailActivity;
import com.example.orderfood.sqlLite.dao.CartDAO;
import com.example.orderfood.sqlLite.dao.FavoriteDAO;
import com.example.orderfood.sqlLite.model.Cart;

import java.text.NumberFormat;
import java.util.List;

public class product_adapter_nomal extends RecyclerView.Adapter<product_adapter_nomal.ProductViewHolder> {


    private Context context;
    private List<Product> productList;
    private List<FavoriteDTO> favoriteDTOS;
    FavoriteDAO favoriteDAO;


    public product_adapter_nomal(Context context, List<Product> productList ) {
        this.context = context;
        this.productList = productList;
        favoriteDAO = new FavoriteDAO(context) ;
    }



    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_nomal, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);


        // Set tên sản phẩm
        holder.productName.setText(product.getName());


        // Set giá sản phẩm
        holder.productPrice.setText(formatVND(product.getPrice()));


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
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    // Gửi Intent sang ProductDetailActivity
                    Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
                    intent.putExtra("productId", product.getId());
                    v.getContext().startActivity(intent);
                }
            });
            animatorSet.start();
        });


        //  gửi id  cho trang product detail


        holder.productPrice.setText(formatVND((product.getPrice())));


        // Set số lượt đánh giá
        holder.productCountRate.setText(product.getStoreID() + " lượt");



        // Set điểm đánh giá
        holder.productRatePoint.setText(String.valueOf(product.getRate()));

        // Hiển thị ngôi sao đánh giá (có thể thêm logic để thay đổi ảnh theo điểm đánh giá)
        setStarRating(holder.starImage, product.getRate());
        holder.productCountFavorite.setText(Integer.toString(product.getStoreID()));
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
        if(favoriteDAO.check_favorite(product.getId())){
            holder.btnAddFavorite.setImageResource(R.drawable.ic_baseline_favorite_click_24);
        }
        else {
            holder.btnAddFavorite.setImageResource(R.drawable.ic_baseline_favorite_24);
        }

        holder.btnAddFavorite.setOnClickListener(v -> {

            // Tạo hiệu ứng scale
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1f, 1.5f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1f, 1.5f, 1f);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(scaleX, scaleY);
            animatorSet.setDuration(300); // Thời gian hiệu ứng
            addFavorite(product.getId(), product.getName(),product.getImage().get(0),"", holder.btnAddFavorite);
            animatorSet.start();

        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productCountRate, productRatePoint;
        TextView productCountFavorite;
        ImageView productImage, starImage;
        ImageView btnAddCart, btnAddFavorite;
        CardView cardView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productImage = itemView.findViewById(R.id.product_image);
            productCountRate = itemView.findViewById(R.id.product_count_rate);
            productRatePoint = itemView.findViewById(R.id.product_rate_point);
            starImage = itemView.findViewById(R.id.starImage);
            productCountFavorite = itemView.findViewById(R.id.count_favorite);
            btnAddCart = itemView.findViewById(R.id.btnAddCart);
            btnAddFavorite = itemView.findViewById(R.id.btnAddFavorite);
        }
    }
        private void setStarRating(ImageView starRatingView, double rate) {
        LayerDrawable layerDrawable = (LayerDrawable) starRatingView.getDrawable();
        ClipDrawable starFilled = (ClipDrawable) layerDrawable.findDrawableByLayerId(R.id.star_filled);
        int level = (int) (rate / 5.0 * 10000);
        starFilled.setLevel(level);
    }

    private String formatVND(Double price) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(price) + " VND";
    }
    public void  addFavorite(int id , String name, String image, String description, ImageView view) {
        if (!favoriteDAO.check_favorite(id)) {
            view.setImageResource(R.drawable.ic_baseline_favorite_click_24);
            favoriteDAO.addFavorite(id, name, image, description);
            Toast.makeText(view.getContext(), "Thêm vào yêu thích: " +name, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(view.getContext(), "Đã có : " +name+" trong yêu thích của bạn", Toast.LENGTH_SHORT).show();
        }
    }


}
