package com.example.orderfood.component;

import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ClipDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.models.Product;
import java.util.List;

public class product_adapter extends RecyclerView.Adapter<product_adapter.ProductViewHolder> {

    private List<Product> productList;

    public product_adapter(List<Product> productList) {this.productList = productList;}

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
                .load(product.getImage().get(0)) // URL từ thuộc tính Image
                .placeholder(R.drawable.image_loading) // Ảnh hiển thị khi đang tải (nên có trong drawable)
                .error(R.drawable.image_error) // Ảnh hiển thị nếu có lỗi
                .into(holder.productImage);

        // Thiết lập tên danh mục
        holder.productPrice.setText(formatVND(( product.getPrice() )));


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice;
        ImageView productImage, starRating;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productImage = itemView.findViewById(R.id.product_image);

        }
    }

    private String formatVND(Double price) {
        return price + " VND";
    }

//    private void setStarRating(ImageView starRatingView, double rate) {
//        LayerDrawable layerDrawable = (LayerDrawable) starRatingView.getDrawable();
//        ClipDrawable starFilled = (ClipDrawable) layerDrawable.findDrawableByLayerId(R.id.star_filled);
//        int level = (int) (rate / 5.0 * 10000);  // Quy đổi giá trị `rate` thành mức độ đổ màu
//        starFilled.setLevel(level);
//    }
}
