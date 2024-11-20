package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.component.ImageProductDetailAdapter;
import com.example.orderfood.models.dto.ProductDetailDTO;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {

    TextView selectedRating = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);


        // giả sử nhận đc data 1 object product detail đủ thông tin

        ArrayList<String> images = new ArrayList<>();
        images.add("https://tamnhuhoa.com/datafiles/37/2023-08/31848625-com-tam-suon-bi-ba-chi.png");
        images.add("https://cdn.tgdd.vn/Files/2021/08/16/1375565/cach-nau-com-tam-suon-bi-cha-tai-nha-ngon-nhu-ngoai-tiem-202108162216045436.jpg");
        images.add("https://cdn.tgdd.vn/Files/2021/08/09/1373996/tu-lam-com-tam-suon-trung-don-gian-thom-ngon-nhu-ngoai-hang-202201151416543367.jpg");
        images.add("https://tamnhuhoa.com/datafiles/37/2023-08/31848625-com-tam-suon-bi-ba-chi.png");
        images.add("https://cdn.tgdd.vn/Files/2021/08/16/1375565/cach-nau-com-tam-suon-bi-cha-tai-nha-ngon-nhu-ngoai-tiem-202108162216045436.jpg");
        images.add("https://cdn.tgdd.vn/Files/2021/08/09/1373996/tu-lam-com-tam-suon-trung-don-gian-thom-ngon-nhu-ngoai-hang-202201151416543367.jpg");


        ProductDetailDTO proDetail = new ProductDetailDTO();
        proDetail.setPID(1);
        proDetail.setName("Cơm Sường Nướng Mật Ong Thơm Ngon");
        proDetail.setDescription("Điểm nhấn của món ăn là phần sườn cốt lết nướng thơm ngon, được ướp kỹ lưỡng cho thấm gia vị vào từng thớ thịt. Sườn được nướng chín đều, tỏa hương thơm nức. Khi thưởng thức, bạn sẽ cảm nhận được vị đậm đà và độ mềm mại của từng miếng thịt.\n\nTại Tường Hân, phần xương đã được bỏ đi, chỉ giữ lại phần thịt, giúp khách dễ dàng thưởng thức món ăn mà không phải lo lắng về xương.");
        proDetail.setStar(5);
        proDetail.setMin(10);
        proDetail.setListImage(images);

        // Hoặc sử dụng constructor có tham số
        ProductDetailDTO proDetail2 = new ProductDetailDTO(
                2,
                "Another Product",
                "Description for another product.",
                4,
                5,
                images
        );

        BindData(proDetail);
    }

    // hàm này dùng để bind data lên giao diện
    private void BindData(ProductDetailDTO proDetail)
    {
        // ảnh chính
        ImageView imageView = findViewById(R.id.pd_main_pic);

        Glide.with(this)
                .load(proDetail.getListImage().get(0))  // URL hình ảnh của bạn
                .centerCrop()
                .into(imageView);  // ImageView để hiển thị hình ảnh

        //tạo các ảnh con
        // 1 lấy recy
        RecyclerView re_imagePro = findViewById(R.id.r_product_detail_mini);
        //2 cáu hình layout cho re đó
        re_imagePro.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        ImageProductDetailAdapter imgDapter = new ImageProductDetailAdapter(this, proDetail.getListImage(), new ImageProductDetailAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(String imageUrl) {
                // Update the main image when a child image is clicked
                Glide.with(ProductDetailActivity.this)
                        .load(imageUrl)  // Load the clicked image URL into the main ImageView
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

        //set mô tả sản phẩm
        TextView proDetail_des = findViewById(R.id.proDetail_des);
        proDetail_des.setText(proDetail.getDescription());

        // bind số lượng sao.
        GridLayout gv_rating = findViewById(R.id.product_detail_rating);
        ArrayList<String> ratingList = new ArrayList<String>();
        ratingList.add("Tất cả 4.9");
        ratingList.add("5 Sao(12)");
        ratingList.add("4 Sao(1)");
        ratingList.add("3 Sao(2)");
        ratingList.add("2 Sao(1)");
        ratingList.add("1 Sao(0)");

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
                }
            });

            // Thêm item vào container layout
            gv_rating.addView(ratingView);
        }
    }





}