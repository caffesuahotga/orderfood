package com.example.orderfood.component;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentProductDetailAdapter extends RecyclerView.Adapter<CommentProductDetailAdapter.CommentProductDetailViewHolder> {




    @NonNull
    @Override
    public CommentProductDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentProductDetailViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CommentProductDetailViewHolder extends RecyclerView.ViewHolder {

        public CommentProductDetailViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
