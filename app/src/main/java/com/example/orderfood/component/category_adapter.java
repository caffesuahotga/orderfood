package com.example.orderfood.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.models.Category;


import java.util.List;

public class category_adapter extends RecyclerView.Adapter<category_adapter.CategoryViewHolder> {

    private List<Category> categoryList;

    public category_adapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category categoryItem = categoryList.get(position);

        // Sử dụng Glide để tải ảnh từ URL
        Glide.with(holder.itemView.getContext())
                .load(categoryItem.getImage()) // URL từ thuộc tính Image
                .placeholder(R.drawable.image_loading) // Ảnh hiển thị khi đang tải (nên có trong drawable)
                .error(R.drawable.image_error) // Ảnh hiển thị nếu có lỗi
                .into(holder.categoryImage);

        // Thiết lập tên danh mục
        holder.categoryName.setText(categoryItem.getName());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.category_image);
            categoryName = itemView.findViewById(R.id.category_name);
        }
    }
}
