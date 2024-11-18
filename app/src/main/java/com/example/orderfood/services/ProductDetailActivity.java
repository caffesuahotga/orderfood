package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.component.ImageProductDetailAdapter;
import com.example.orderfood.models.dto.ProductDetailDTO;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);


        // giả sử nhận đc data 1 object product detail đủ thông tin

        ArrayList<String> images = new ArrayList<>();
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
                .into(imageView);  // ImageView để hiển thị hình ảnh

        //tạo các ảnh con
        // 1 lấy recy
        RecyclerView re_imagePro = findViewById(R.id.r_product_detail_mini);
        //2 cáu hình layout cho re đó
        re_imagePro.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        ImageProductDetailAdapter imgDapter = new ImageProductDetailAdapter(this,proDetail.getListImage());
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

        // bind số  lượng sao.



    }



}