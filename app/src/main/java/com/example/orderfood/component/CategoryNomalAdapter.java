package com.example.orderfood.component;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.data.HandleData;
import com.example.orderfood.models.Category;
import com.example.orderfood.models.Product;
import com.example.orderfood.services.ProductActivity;

import java.util.ArrayList;
import java.util.List;

public class CategoryNomalAdapter extends RecyclerView.Adapter<CategoryNomalAdapter.CategoryViewHolder>  {
    private List<Category> categoryList;
    private List<Product> productList = new HandleData().getAllProducts();
    private int selectedPosition = -1;

    public CategoryNomalAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public CategoryNomalAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_nomal, parent, false);
        return new CategoryNomalAdapter.CategoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CategoryNomalAdapter.CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Category categoryItem = categoryList.get(position);
        HandleData handleData = new HandleData();


        // Sử dụng Glide để tải ảnh từ URL
        Glide.with(holder.itemView.getContext())
                .load(categoryItem.getImage()) // URL từ thuộc tính Image
                .placeholder(R.drawable.image_loading) // Ảnh hiển thị khi đang tải (nên có trong drawable)
                .error(R.drawable.image_error) // Ảnh hiển thị nếu có lỗi
                .into(holder.categoryImage);

        // Thiết lập tên danh mục
        holder.categoryName.setText(categoryItem.getName());
        if (position == selectedPosition) {
            holder.viewCard.setBackgroundResource(R.drawable.selected_border); // Nền khi được chọn
        } else {
            holder.viewCard.setBackgroundResource(R.drawable.default_border); // Nền mặc định
        }

        // Xử lý sự kiện click
        holder.viewCard.setOnClickListener(view -> {
            // Cập nhật trạng thái được chọn
            int previousPosition = selectedPosition;
            selectedPosition = position;

            // Thông báo thay đổi để cập nhật lại danh sách
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);

            // Chuyển sang ProductActivity
            Intent intent = new Intent(view.getContext(), ProductActivity.class);
            intent.putExtra("categoryID", categoryItem.getId());
            view.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
        CardView viewCard;


        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.category_image);
            categoryName = itemView.findViewById(R.id.category_name);
            viewCard =  itemView.findViewById(R.id.item_view);
        }
    }
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

}
