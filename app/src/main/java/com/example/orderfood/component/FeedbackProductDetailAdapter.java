package com.example.orderfood.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.models.dto.FeedBackDTO;

import java.util.ArrayList;

public class FeedbackProductDetailAdapter extends RecyclerView.Adapter<FeedbackProductDetailAdapter.FeedbackProductDetailViewHolder> {
    private Context context;
    private ArrayList<FeedBackDTO> feedBackDTOS;

    public FeedbackProductDetailAdapter(Context context, ArrayList<FeedBackDTO> feedBackDTOS) {
        this.context = context;
        this.feedBackDTOS = feedBackDTOS;
    }

    @NonNull
    @Override
    public FeedbackProductDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate giao diện, từ xml =>  giao diện
        View view = LayoutInflater.from(context).inflate(R.layout.item_feedback_product_detail, parent, false);
        return new FeedbackProductDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackProductDetailViewHolder holder, int position) {
        // bind data từ list sang giao diện
        //lấy phần từ hiện tại
        FeedBackDTO feedBackDTO = feedBackDTOS.get(position);

        // nạp ảnh người feedback
        Glide.with(holder.itemView.getContext())
                .load(feedBackDTO.getImageUser())
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .into(holder.FeedbackAvt);

        // tên
        holder.FeedbackUserName.setText(feedBackDTO.getNameUser());

        // star
        StringBuilder starString = new StringBuilder();
        for (int i = 0; i < feedBackDTO.getStar(); i++) {
            starString.append("★");
        }
        holder.FeedbackUserStar.setText(starString.toString());
        holder.FeedbackContent.setText(feedBackDTO.getContent());
    }

    @Override
    public int getItemCount() {
        return feedBackDTOS.size();
    }

    public class FeedbackProductDetailViewHolder extends RecyclerView.ViewHolder {
        public ImageView FeedbackAvt;
        public TextView FeedbackUserName;
        public  TextView FeedbackUserStar;
        public  TextView FeedbackContent;

        public FeedbackProductDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            FeedbackAvt  = itemView.findViewById(R.id.feedback_user_avt);
            FeedbackUserName = itemView.findViewById(R.id.feedback_user_name);
            FeedbackUserStar = itemView.findViewById(R.id.feedback_star);
            FeedbackContent = itemView.findViewById(R.id.feedback_content);
        }
    }
}
