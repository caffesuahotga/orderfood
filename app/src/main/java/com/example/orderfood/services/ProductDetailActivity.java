package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.component.FeedbackProductDetailAdapter;
import com.example.orderfood.component.ImageProductDetailAdapter;
import com.example.orderfood.data.ProductDetailUtil;
import com.example.orderfood.models.dto.FeedBackDTO;
import com.example.orderfood.models.dto.ProductDetailDTO;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ProductDetailActivity extends AppCompatActivity {

    TextView selectedRating = null;
    private  int ProductID = 1; // giả xử nhận được id là 1
    LinearLayout emptyFeedbackPlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        emptyFeedbackPlaceholder = findViewById(R.id.product_detail_feedback_empt);

        // get product có id là 1
        ProductDetailDTO productDetail = ProductDetailUtil.getProductById(1);
        BindData(productDetail);
    }

    // hàm này dùng để bind data lên giao diện
    private void BindData(ProductDetailDTO proDetail)
    {
        // ảnh chính
        ImageView imageView = findViewById(R.id.pd_main_pic);

        Glide.with(this)
                .load(proDetail.getListImage().get(0))  // URL hình ảnh của bạn
                .placeholder(R.drawable.image_loading)
                .centerCrop()
                .into(imageView);  // ImageView để hiển thị hình ảnh

        //tạo các ảnh con
        // 1 lấy recy
        RecyclerView re_imagePro = findViewById(R.id.r_product_detail_mini);
        //2 cái hình layout cho recy đó
        re_imagePro.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        ImageProductDetailAdapter imgDapter = new ImageProductDetailAdapter(this, proDetail.getListImage(), new ImageProductDetailAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(String imageUrl) {
                // Update the main image when a child image is clicked
                Glide.with(ProductDetailActivity.this)
                        .load(imageUrl)  // Load the clicked image URL into the main ImageView
                        .placeholder(R.drawable.image_loading)
                        .centerCrop()
                        .into(imageView);
            }
        });
        re_imagePro.setAdapter(imgDapter);

        // set tên
        TextView proDetail_name = findViewById(R.id.proDetail_name);
        proDetail_name.setText(proDetail.getName());

        // set sao & min
        String star_min = " ★ "+ proDetail.getStar() + " - " + proDetail.getMin() + " Mins ";
        TextView proDetail_star_min = findViewById(R.id.proDetail_star_min);
        proDetail_star_min.setText(star_min);

        // giá
        String price = "Giá: " + NumberFormat.getInstance(Locale.getDefault()).format(proDetail.getPrice()) + " VNĐ";
        TextView productDetail_price = findViewById(R.id.proDetail_price);
        productDetail_price.setText(price);

        //set mô tả sản phẩm
        TextView proDetail_des = findViewById(R.id.proDetail_des);
        String description = proDetail.getDescription().replace("\\n", "\n");
        proDetail_des.setText(description);

        // bind số lượng sao.
        GridLayout gv_rating = findViewById(R.id.product_detail_rating);
        ArrayList<String> ratingList = new ArrayList<String>();

        double totalFeedback = proDetail.getListFeedBack().size(); // Dùng double để dễ tính toán
        double totalStars = 0; // Cũng dùng double để đảm bảo phép chia chính xác

        for (int star = 1; star <= 5; star++) {
            int finalStar = star;
            double count = proDetail.getListFeedBack().stream()
                    .filter(feedBackDTO -> feedBackDTO.getStar() == finalStar)
                    .count();
            totalStars += count * star; // Tính tổng điểm sao
            ratingList.add(star + " Sao (" + (int) count + ")"); // Ép count về int khi hiển thị
        }

        double averageStars = totalFeedback > 0 ? totalStars / totalFeedback : 0;
        ratingList.add(0, "Tất cả (" + String.format("%.1f", averageStars) + ")");

        LayoutInflater ratingInflate = LayoutInflater.from(this);

        for (int i = 0; i < ratingList.size(); i++) {
            String rating = ratingList.get(i);
            View ratingView = ratingInflate.inflate(R.layout.item_rating, gv_rating, false);
            TextView ratingItemView = ratingView.findViewById(R.id.pd_rating);
            ratingItemView.setText(rating);

            // Đặt màu chữ mặc định
            ratingItemView.setTextColor(getResources().getColor(R.color.selected_rating_color));

            // Kiểm tra nếu đây là phần tử đầu tiên, áp dụng kiểu active
            if (i == 0) {
                selectedRating = ratingItemView;
                // Đổi nền và màu chữ cho phần tử đầu tiên khi khởi tạo
                ratingItemView.setBackgroundColor(getResources().getColor(R.color.selected_rating_color)); // Màu nền khi được chọn
                ratingItemView.setTextColor(getResources().getColor(android.R.color.white)); // Màu chữ trắng khi chọn
            }

            // Đặt sự kiện OnClick
            ratingItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Kiểm tra nếu có TextView nào đã được chọn trước đó
                    if (selectedRating != null) {
                        selectedRating.setBackgroundColor(getResources().getColor(R.color.white)); // Trở lại màu nền trắng
                        selectedRating.setTextColor(getResources().getColor(R.color.selected_rating_color)); // Màu chữ mặc định
                        selectedRating.setBackgroundResource(R.drawable.button_border_red); // Trở lại viền đỏ
                    }

                    // Cập nhật phần tử được chọn
                    selectedRating = ratingItemView;

                    // Thay đổi màu nền khi được chọn
                    ratingItemView.setBackgroundColor(getResources().getColor(R.color.selected_rating_color)); // Màu nền khi được chọn
                    ratingItemView.setTextColor(getResources().getColor(android.R.color.white)); // Màu chữ trắng khi chọn
                    ratingItemView.setBackgroundResource(R.drawable.bg_rating_product_detail_active); // Đổi sang kiểu mới khi được chọn
                    ratingItemView.setScaleX(1.2f);
                    ratingItemView.setScaleY(1.2f);
                    ratingItemView.animate().scaleX(1f).scaleY(1f).setDuration(200).start();

                    // Lọc danh sách bình luận theo sao đã chọn
                    String ratingText = ratingItemView.getText().toString();
                    int selectedStar = -1;

                    // Kiểm tra nếu là "Tất cả", thì không cần chuyển đổi thành số
                    if (ratingText.startsWith("Tất cả")) {
                        selectedStar = -1;  // -1 sẽ biểu thị cho tất cả bình luận
                    } else {
                        try {
                            selectedStar = Integer.parseInt(ratingText.split(" ")[0]); // Lấy mức sao từ chuỗi "X Sao"
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            return;  // Nếu không thể chuyển đổi thành số, thoát ra
                        }
                    }

                    filterCommentsByRating(selectedStar, proDetail);
                }
            });

            // Thêm item vào container layout
            gv_rating.addView(ratingView);
        }

//         bind bình luận
        RecyclerView comment_view = findViewById(R.id.product_detail_comment_view);
        comment_view.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

        FeedbackProductDetailAdapter feedbackAdaper = new FeedbackProductDetailAdapter(this,proDetail.getListFeedBack());
        comment_view.setAdapter(feedbackAdaper);
    }

    private void filterCommentsByRating(int selectedStar, ProductDetailDTO proDetail) {
        List<FeedBackDTO> filteredComments;

        if (selectedStar == -1) {
            // Hiển thị tất cả bình luận
            filteredComments = proDetail.getListFeedBack();
        } else {
            // Lọc bình luận theo sao
            filteredComments = new ArrayList<>();
            for (FeedBackDTO feedback : proDetail.getListFeedBack()) {
                if (feedback.getStar() == selectedStar) {
                    filteredComments.add(feedback);
                }
            }
        }

        // Cập nhật adapter của RecyclerView với danh sách bình luận đã lọc
        FeedbackProductDetailAdapter feedbackAdapter = new FeedbackProductDetailAdapter(this, (ArrayList<FeedBackDTO>) filteredComments);
        RecyclerView commentView = findViewById(R.id.product_detail_comment_view);
        commentView.setAdapter(feedbackAdapter);

        // Kiểm tra nếu danh sách feedback trống
        if (filteredComments.stream().count() == 0) {
            emptyFeedbackPlaceholder.setVisibility(View.VISIBLE);  // Hiển thị thông báo khi không có feedback
        } else {
            emptyFeedbackPlaceholder.setVisibility(View.GONE);  // Ẩn thông báo
        }
    }
}