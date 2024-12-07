package com.example.orderfood.services;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orderfood.R;
import com.example.orderfood.component.ProductCartAdapter;
import com.example.orderfood.data.HandleData;
import com.example.orderfood.models.Product;
import com.example.orderfood.models.dto.CartDTO;
import com.example.orderfood.sqlLite.dao.CartDAO;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CartActivity extends BaseNoBottomActivity {
    CartDAO cartDAO = new CartDAO(this);
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_cart, findViewById(R.id.content_frame));

        swipeRefreshLayout = findViewById(R.id.cart_page_refresh);

        // thêm tạm sản phẩm
        CartDAO cartDAO2 = new CartDAO(this);
        cartDAO2.deleteAll();
        List<Product> products = HandleData.getAllProducts();
        for (Product product : products) {
            cartDAO2.addProduct(product.getId(), product.getName(), 1, product.getImage().get(0));
        }

        //////////////////// -- code
        BindData();

        // Xử lý refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Show loading spinner
            swipeRefreshLayout.setRefreshing(true);

            // Call lại API hoặc logic tải dữ liệu trong background thread
            new Thread(() -> {

                loadCart(); // Load dữ liệu

                // Sau khi dữ liệu được tải, thực hiện thao tác trên UI thread
                runOnUiThread(() -> {
                    // Kết thúc hiệu ứng refresh
                    swipeRefreshLayout.setRefreshing(false);
                });
            }).start();
        });
    }

    // nạp data render giao diện
    private void BindData() {

        RecyclerView cart_view = findViewById(R.id.product_cart_container);
        cart_view.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        ArrayList<CartDTO> carts = cartDAO.getAllProducts();

        if (carts == null) {
            carts = new ArrayList<>(); // Nếu danh sách trả về null, khởi tạo một ArrayList rỗng
        }

        ProductCartAdapter productCartAdapter = new ProductCartAdapter(this, carts);
        cart_view.setAdapter(productCartAdapter);

        BindBottom(carts, productCartAdapter);
    }

    private void BindBottom(ArrayList<CartDTO> carts, ProductCartAdapter productCartAdapter) {
        updateTotalPrice(carts);
        checkAll(carts, productCartAdapter);
        order(carts);
    }

    public void updateTotalPrice(ArrayList<CartDTO> carts) {
        double total = 0;

        for (CartDTO item : carts) {
            if (item.isSelected()) {
                total += item.getPrice() * item.getQuantity(); // Tính tổng tiền cho sản phẩm đã chọn
            }
        }

        // Cập nhật TextView hiển thị tổng tiền
        TextView totalPriceTextView = findViewById(R.id.cart_total_pay); // Giả sử bạn có TextView này
        totalPriceTextView.setText("Tổng: " + NumberFormat.getInstance(Locale.getDefault()).format(total) + " VNĐ");
    }

    public void checkAll(ArrayList<CartDTO> carts, ProductCartAdapter productCartAdapter) {
        CheckBox checkBoxAll = findViewById(R.id.cart_pro_check_all); // Giả sử bạn có TextView này
        checkBoxAll.setOnCheckedChangeListener((compoundButton, b) -> {
            for (CartDTO cartDTO : carts) {
                cartDTO.setSelected(b);
            }
            updateTotalPrice(carts);

            productCartAdapter.notifyDataSetChanged();
        });

        if (carts.stream().anyMatch(cartDTO -> cartDTO.isSelected()) == false) {
            checkBoxAll.setChecked(false);
        }
    }

    public void order(ArrayList<CartDTO> carts) {
        TextView order = findViewById(R.id.cart_order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog progressDialog = new ProgressDialog(CartActivity.this);
                progressDialog.setMessage(CartActivity.this.getString(R.string.loading_message)); // Đặt thông điệp loading từ strings.xml
                progressDialog.setCancelable(false); // Không cho phép hủy bằng cách chạm ra ngoài
                progressDialog.show();

                ArrayList<CartDTO> productSelect = (ArrayList<CartDTO>) carts.stream()
                        .filter(CartDTO::isSelected)
                        .collect(Collectors.toList());

                if (productSelect.isEmpty()) {

                    Toast.makeText(CartActivity.this, "Bạn chưa chọn sản phẩm nào!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {

                    Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                    intent.putExtra("product_list", productSelect);
                    startActivity(intent);

                    new Handler().postDelayed(() -> {
                        progressDialog.dismiss();
                    }, 1000);
                }


            }
        });
    }

    // cập nhật data khi kéo
    private void loadCart() {
        runOnUiThread(() -> {
            BindData();
        });
    }
}