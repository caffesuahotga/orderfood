package com.example.orderfood.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.orderfood.R;

import java.util.ArrayList;

public class ImageProductDetailAdapter extends RecyclerView.Adapter<ImageProductDetailAdapter.ImageViewHolder> {
    private Context context;
    private ArrayList<String> imageUrls;
    private OnImageClickListener onImageClickListener;  // Listener for image clicks

    // Constructor
    public ImageProductDetailAdapter(Context context, ArrayList<String> imageUrls, OnImageClickListener onImageClickListener) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.onImageClickListener = onImageClickListener;  // Pass the listener from activity
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_product_detail, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        Glide.with(context).load(imageUrl).into(holder.imageView);

        // Set the click listener for each child image
        holder.imageView.setOnClickListener(v -> {
            // Thêm hiệu ứng phóng to cho ảnh khi click
            holder.imageView.animate()
                    .scaleX(1.5f) // Phóng to 1.5 lần theo chiều ngang
                    .scaleY(1.5f) // Phóng to 1.5 lần theo chiều dọc
                    .setDuration(200) // Thời gian hiệu ứng
                    .withEndAction(() -> {
                        // Sau khi hiệu ứng kết thúc, phục hồi kích thước
                        holder.imageView.animate()
                                .scaleX(1f) // Quay lại kích thước ban đầu
                                .scaleY(1f)
                                .setDuration(200)
                                .start();
                    })
                    .start();

            if (onImageClickListener != null) {
                onImageClickListener.onImageClick(imageUrl);  // Notify the listener with the clicked image URL
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    // ViewHolder class for RecyclerView
    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_pro_detail_mini);  // Assuming this is your child image ID
        }
    }

    // Define an interface for handling image click events
    public interface OnImageClickListener {
        void onImageClick(String imageUrl);  // Method to be triggered when an image is clicked
    }
}