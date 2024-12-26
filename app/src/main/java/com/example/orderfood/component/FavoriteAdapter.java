package com.example.orderfood.component;

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

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.orderfood.R;

import com.example.orderfood.models.dto.FavoriteDTO;
import com.example.orderfood.services.FavoriteActivity;
import com.example.orderfood.sqlLite.dao.CartDAO;
import com.example.orderfood.sqlLite.dao.FavoriteDAO;


import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private Context context;
    private List<FavoriteDTO> favoriteDTOList;
    private FragmentActivity fragmentActivity;
    private FavoriteDAO favoriteDAO;


    public FavoriteAdapter(Context context, FragmentActivity fragmentActivity, List<FavoriteDTO> favoriteDTOList) {
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.favoriteDTOList = favoriteDTOList;
        favoriteDAO = new FavoriteDAO(context);
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {

        FavoriteDTO favoriteDTOItem = favoriteDTOList.get(position);
        setStarRating(holder.starImage, favoriteDTOItem.getRate());
        holder.btnAddCart.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view){
                CartDAO cartDAO = new CartDAO(holder.itemView.getContext());
                cartDAO.deleteAll();
                cartDAO.addProduct(favoriteDTOItem.getProductID(),favoriteDTOItem.getName(),1,favoriteDTOItem.getImage());
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f, 1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.5f, 1f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(scaleX, scaleY);
                animatorSet.setDuration(300); // Thời gian hiệu ứng
                Toast.makeText(view.getContext(), "Thêm vào giỏ hàng: " + favoriteDTOItem.getName(), Toast.LENGTH_SHORT).show();
                animatorSet.start();
            }
        });

        Glide.with(holder.itemView.getContext())
                .load(favoriteDTOItem.getImage()) // URL từ thuộc tính Image
                .placeholder(R.drawable.image_loading) // Ảnh hiển thị khi đang tải (nên có trong drawable)
                .error(R.drawable.image_error) // Ảnh hiển thị nếu có lỗi
                .into(holder.favoriteProductIcon);
        holder.favoriteProName.setText(favoriteDTOItem.getName());
        holder.favoriteCountFavorite.setText(favoriteDTOItem.getPrice()+"");
        holder.favoriteProPrice.setText(formatVND(favoriteDTOItem.getPrice()));
        holder.favoriteCountFavorite.setText(favoriteDTOItem.getRate()+"");
        holder.btnDescription.setOnClickListener(v -> {

            ParentClass.NoteDialogFragment dialog = ParentClass.NoteDialogFragment.newInstance(favoriteDTOItem);
            dialog.show(fragmentActivity.getSupportFragmentManager(), "NoteDialog");
        });
        holder.btnDeleteFavorite.setOnClickListener(v -> {

            favoriteDAO.deleteProduct(favoriteDTOItem.getProductID());
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1f, 1.5f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1f, 1.5f, 1f);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(scaleX, scaleY);
            animatorSet.setDuration(300); // Thời gian hiệu ứng

            if (position != RecyclerView.NO_POSITION) {
                favoriteDTOList.remove(position); // Xóa khỏi danh sách
                notifyItemRemoved(position); // Thông báo RecyclerView
                notifyItemRangeChanged(position, favoriteDTOList.size()); // Cập nhật lại các mục sau vị trí đã xóa
            }
            Toast.makeText(context, "Đã xóa sản phẩm: " + favoriteDTOItem.getName(), Toast.LENGTH_SHORT).show();
            animatorSet.start();


        });
    }

    @Override
    public int getItemCount() {
        return (favoriteDTOList == null) ? 0 : favoriteDTOList.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView favoriteProductIcon, favoriteDescriptionIcon, starImage,btnAddCart, btnDescription,btnDeleteFavorite;
        TextView favoriteProName, favoriteProPrice, favoriteCountFavorite;


        public FavoriteViewHolder(View itemView) {
            super(itemView);
            favoriteProductIcon = itemView.findViewById(R.id.favorite_product_icon);
            favoriteDescriptionIcon = itemView.findViewById(R.id.favorite_description_icon);
            favoriteProName = itemView.findViewById(R.id.favorite_pro_name);
            favoriteProPrice = itemView.findViewById(R.id.favorite_pro_price);
            favoriteCountFavorite = itemView.findViewById(R.id.favorite_count_favorite);
            starImage = itemView.findViewById(R.id.starImage);
            btnAddCart = itemView.findViewById(R.id.btnAddCart);
            btnDescription = itemView.findViewById(R.id.favorite_description_icon);
            btnDeleteFavorite = itemView.findViewById(R.id.btnDeleteFavorite);

        }
    }

    private String formatVND(Double price) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(price) + " VND";
    }
    private void setStarRating(ImageView starRatingView, double rate) {
        LayerDrawable layerDrawable = (LayerDrawable) starRatingView.getDrawable();
        ClipDrawable starFilled = (ClipDrawable) layerDrawable.findDrawableByLayerId(R.id.star_filled);
        int level = (int) (rate / 5.0 * 10000);
        starFilled.setLevel(level);
    }
}
