package com.example.orderfood.component;

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
import java.util.Locale;
import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.models.Product;
import com.example.orderfood.models.dto.CartDTO;
import com.example.orderfood.services.CartActivity;
import com.example.orderfood.services.ProductDetailActivity;
import com.example.orderfood.sqlLite.dao.CartDAO;
import com.example.orderfood.sqlLite.model.Cart;

import java.text.NumberFormat;
import java.util.List;

public class product_adapter_nomal extends RecyclerView.Adapter<product_adapter_nomal.ProductViewHolder> {


    private Context context;
    private List<Product> productList;

    public product_adapter_nomal(List<Product> productList) {
        this.productList = productList;
    }
    public product_adapter_nomal(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
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


        // Set hình ảnh sản phẩm
        Glide.with(holder.itemView.getContext())
                .load(product.getImage().get(0)) // URL hình ảnh từ thuộc tính Image
                .placeholder(R.drawable.image_loading) // Ảnh hiển thị khi đang tải
                .error(R.drawable.image_error) // Ảnh hiển thị nếu có lỗi
                .into(holder.productImage);
        holder.productImage.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);

            // Truyền dữ liệu productId cho Activity (ở đây là số 5, bạn có thể thay đổi theo nhu cầu)
            intent.putExtra("productId", product.getId());

            // Mở Activity (chuyển trang)
            v.getContext().startActivity(intent);
        });

        // Set số lượt đánh giá
        holder.productCountRate.setText(product.getStoreID() + " lượt");



        // Set điểm đánh giá
        holder.productRatePoint.setText(String.valueOf(product.getRate()));

        // Hiển thị ngôi sao đánh giá (có thể thêm logic để thay đổi ảnh theo điểm đánh giá)
        setStarRating(holder.starImage, product.getRate());
        holder.productCountFavorite.setText(Integer.toString(product.getStoreID()));
        holder.btnAddCart.setOnClickListener(v -> {
            // Thực hiện hành động thêm vào giỏ hàng
            // Ví dụ: Hiển thị thông báo

            CartDAO cartDAO = new CartDAO(holder.itemView.getContext());
            cartDAO.deleteAll();
            cartDAO.addProduct(product.getId(),product.getName(),1,product.getImage().get(0));
            Toast.makeText(v.getContext(), "Thêm vào giỏ hàng: " + product.getName(), Toast.LENGTH_SHORT).show();



        });

        // Sự kiện onClick cho btnAddFavorite
        holder.btnAddFavorite.setOnClickListener(v -> {


            
            // Thực hiện hành động thêm vào yêu thích
            Toast.makeText(v.getContext(), "Thêm vào yêu thích: " + product.getName(), Toast.LENGTH_SHORT).show();
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
    }
