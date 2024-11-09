package com.example.orderfood.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.orderfood.R;
import com.example.orderfood.models.category;

import java.util.List;

public class category_adapter extends RecyclerView.Adapter<category_adapter.CategoryViewHolder> {

    private List<category> categoryList;

    public category_adapter(List<category> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        category categoryItem = categoryList.get(position);


        holder.categoryImage.setImageResource(categoryItem.getImageResource());
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
            categoryImage = itemView.findViewById(R.id.category_image);  // ID trong item_category.xml
            categoryName = itemView.findViewById(R.id.category_name);    // ID trong item_category.xml
        }
    }
}
