package com.example.orderfood.component;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.models.Product;

import java.util.List;

public class product_adapter_nomal extends RecyclerView.Adapter<product_adapter_nomal.ProductViewHolder> {

    private List<Product> productList;

    public product_adapter_nomal(List<Product> productList) {
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

        // Set số lượt đánh giá
        holder.productCountRate.setText(product.getStoreID() + " lượt");

        // Set điểm đánh giá
        holder.productRatePoint.setText(String.valueOf(product.getRate()));

        // Hiển thị ngôi sao đánh giá (có thể thêm logic để thay đổi ảnh theo điểm đánh giá)
        setStarRating(holder.starImage, product.getRate());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productCountRate, productRatePoint;
        ImageView productImage, starImage;
        CardView cardView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productImage = itemView.findViewById(R.id.product_image);
            productCountRate = itemView.findViewById(R.id.product_count_rate);
            productRatePoint = itemView.findViewById(R.id.product_rate_point);
            starImage = itemView.findViewById(R.id.starImage);
        }
    }
        private void setStarRating(ImageView starRatingView, double rate) {
        LayerDrawable layerDrawable = (LayerDrawable) starRatingView.getDrawable();
        ClipDrawable starFilled = (ClipDrawable) layerDrawable.findDrawableByLayerId(R.id.star_filled);
        int level = (int) (rate / 5.0 * 10000);  // Quy đổi giá trị `rate` thành mức độ đổ màu
        starFilled.setLevel(level);
    }

    private String formatVND(Double price) {
        return price + " VND";
    }
}
