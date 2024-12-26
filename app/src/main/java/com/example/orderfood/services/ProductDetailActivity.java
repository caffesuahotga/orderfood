package com.example.orderfood.services;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.component.FeedbackProductDetailAdapter;
import com.example.orderfood.component.ImageProductDetailAdapter;
import com.example.orderfood.data.CurrentUser;
import com.example.orderfood.data.ProductDetailUtil;
import com.example.orderfood.models.Account;
import com.example.orderfood.models.dto.CartDTO;
import com.example.orderfood.models.dto.FeedBackDTO;
import com.example.orderfood.models.dto.ProductDetailDTO;
import com.example.orderfood.sqlLite.dao.CartDAO;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductDetailActivity extends BaseNoBottomActivity {

    TextView selectedRating = null;
    LinearLayout emptyFeedbackPlaceholder;
    private SwipeRefreshLayout swipeRefreshLayout;
    CartDAO cartDAO = new CartDAO(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_product_detail, findViewById(R.id.content_frame));

        emptyFeedbackPlaceholder = findViewById(R.id.product_detail_feedback_empt);
        swipeRefreshLayout = findViewById(R.id.product_detail_refresh);


        // get product có id là 1
        int productId = getIntent().getIntExtra("productId", -1);
        if(productId != -1)
        {
            loadProductData(productId);

            // Xử lý refresh
            swipeRefreshLayout.setOnRefreshListener(() -> {
                // Show loading spinner
                swipeRefreshLayout.setRefreshing(true);

                // Call lại API hoặc logic tải dữ liệu trong background thread
                new Thread(() -> {
                    loadProductData(productId); // Load dữ liệu

                    // Sau khi dữ liệu được tải, thực hiện thao tác trên UI thread
                    runOnUiThread(() -> {
                        // Kết thúc hiệu ứng refresh
                        swipeRefreshLayout.setRefreshing(false);
                    });
                }).start();

            });
        }else
        {
            Toast.makeText(this,R.string.không_tìm_thấy,Toast.LENGTH_LONG);
            finish();
        }


    }

    private void loadProductData(int productID) {
        // Lấy dữ liệu product và hiển thị
        ProductDetailDTO productDetail = ProductDetailUtil.getProductById(productID);

        // Đảm bảo rằng việc cập nhật UI phải thực hiện trên main thread
        runOnUiThread(() -> {
            BindData(productDetail); // Cập nhật UI với dữ liệu mới
        });
    }

    // hàm này dùng để bind data lên giao diện
    private void BindData(ProductDetailDTO proDetail) {
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
        re_imagePro.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

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
        String star_min = " ★ " + proDetail.getStar() + " - " + proDetail.getMin() + " Mins ";
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
        gv_rating.removeAllViews();
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
        comment_view.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        FeedbackProductDetailAdapter feedbackAdaper = new FeedbackProductDetailAdapter(this, proDetail.getListFeedBack());
        comment_view.setAdapter(feedbackAdaper);


        // bind data 2 nút thêm vào giỏ hàng
        BindDataCart(proDetail);

        BindDataOrder(proDetail);
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

    // dùng để bind data cho nút thêm vào cart
    private void BindDataCart(ProductDetailDTO proDetail)
    {
        Button addCart = findViewById(R.id.product_detail_cart);
        Account currentUser = CurrentUser.getCurrentUser();
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shakeAndRotateButton(view);

                cartDAO.addProduct(proDetail.getPID(),proDetail.getName(),1,proDetail.getListImage().get(0),currentUser.getId());
                Toast.makeText(view.getContext(), "Thành công! thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void shakeAndRotateButton(View view) {
        // Lấy vị trí trung tâm của nút
        float centerX = view.getWidth() / 2;
        float centerY = view.getHeight() / 2;

        // Di chuyển từ vị trí trung tâm sang trái/phải và lên/xuống
        TranslateAnimation shake = new TranslateAnimation(
                -centerX / 5, centerX / 5, // Di chuyển theo chiều ngang (trái/phải)
                -centerY / 5, centerY / 5  // Di chuyển theo chiều dọc (lên/xuống)
        );

        shake.setDuration(100); // Thời gian di chuyển một lần
        shake.setRepeatCount(5); // Lặp lại 5 lần
        shake.setRepeatMode(TranslateAnimation.REVERSE); // Lặp lại kiểu đối xứng (di chuyển theo chiều ngược lại)

        // Xoay nút theo chiều kim đồng hồ (clockwise)
        RotateAnimation rotateClockwise = new RotateAnimation(
                0, 10,   // Góc ban đầu và góc kết thúc
                Animation.RELATIVE_TO_SELF, 0.5f, // Trục xoay (giữa nút)
                Animation.RELATIVE_TO_SELF, 0.5f  // Trục xoay (giữa nút)
        );
        rotateClockwise.setDuration(100); // Thời gian xoay một lần
        rotateClockwise.setRepeatCount(5); // Lặp lại 5 lần
        rotateClockwise.setRepeatMode(TranslateAnimation.REVERSE); // Lặp lại kiểu đối xứng

        // Kết hợp cả hai hiệu ứng (lắc và xoay)
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(shake);      // Thêm hiệu ứng lắc
        animationSet.addAnimation(rotateClockwise); // Thêm hiệu ứng xoay

        // Áp dụng hiệu ứng
        view.startAnimation(animationSet);
    }

    /// đặt hàng luôn

    private void BindDataOrder(ProductDetailDTO proDetail)
    {
        Button addorder = findViewById(R.id.product_detail_order);
        addorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shakeAndRotateButton(view);

                ArrayList<CartDTO> productSelect = new ArrayList<>();
                CartDTO item = new CartDTO();

                item.setProductID(proDetail.getPID());
                item.setName(proDetail.getName());
                item.setQuantity(1);
                item.setImage(proDetail.getListImage().get(0));
                item.setPrice(proDetail.getPrice());

                productSelect.add(item);

                Intent intent = new Intent(ProductDetailActivity.this, OrderActivity.class);
                intent.putExtra("product_list", productSelect);
                startActivity(intent);
            }
        });
    }

}