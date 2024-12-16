package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orderfood.R;
import com.example.orderfood.component.ProductCartAdapter;
import com.example.orderfood.component.ProductOrderAdapter;
import com.example.orderfood.data.CurrentUser;
import com.example.orderfood.data.NotiUtil;
import com.example.orderfood.data.OrderUtil;
import com.example.orderfood.models.Order;
import com.example.orderfood.models.dto.CartDTO;
import com.example.orderfood.models.dto.OrderDTO;
import com.example.orderfood.models.dto.OrderProductDTO;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderActivity extends BaseNoBottomActivity {
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_order, findViewById(R.id.content_frame));
        CurrentUser.init(OrderActivity.this);

        Intent intent = getIntent();
        int orderId = intent.getIntExtra("orderId", 0); // 0 là giá trị mặc định nếu không có `orderId`

        if (orderId == 0) // => có nghĩa là đặt hàng
        {
            ArrayList<CartDTO> productList = (ArrayList<CartDTO>) getIntent().getSerializableExtra("product_list");
            bindData(productList);

        } else // có nghĩa là xem lại dơn hàng
        {
            GetInfoOrder(orderId);
        }

        // load kéo
        swipeRefreshLayout = findViewById(R.id.order_page_refresh);

        // Xử lý refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Show loading spinner
            swipeRefreshLayout.setRefreshing(true);

            new Thread(() -> {

                loadOrder(orderId);

                runOnUiThread(() -> {
                    // Kết thúc hiệu ứng refresh
                    swipeRefreshLayout.setRefreshing(false);
                });
            }).start();
        });

    }

    private void bindData(ArrayList<CartDTO> productList) {
        RecyclerView order_product_view = findViewById(R.id.product_order_container);
        order_product_view.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        ProductOrderAdapter productCartAdapter = new ProductOrderAdapter(this, productList);
        order_product_view.setAdapter(productCartAdapter);


        bindInforUser();

        order(productList);
    }

    private void bindInforUser() {
        EditText name = findViewById(R.id.order_user_name);
        EditText phone = findViewById(R.id.order_user_phone);

        name.setText(CurrentUser.getName());
        phone.setText(CurrentUser.getPhone());
    }

    // hàm order
    private void order(ArrayList<CartDTO> productList) {
        EditText name = findViewById(R.id.order_user_name);
        EditText phone = findViewById(R.id.order_user_phone);
        EditText address = findViewById(R.id.order_user_address);
        TextView order = findViewById(R.id.btn_order);
        TextView totalPay = findViewById(R.id.order_total_pay);

        double total = productList.stream().mapToDouble(product -> product.getPrice() * product.getQuantity()).sum();
        totalPay.setText("Tổng: " + NumberFormat.getInstance(Locale.getDefault()).format(total) + " VNĐ");

        ProgressDialog progressDialog = new ProgressDialog(OrderActivity.this);
        progressDialog.setMessage(OrderActivity.this.getString(R.string.loading_message)); // Đặt thông điệp loading từ strings.xml
        progressDialog.setCancelable(false); // Không cho phép hủy bằng cách chạm ra ngoài


        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy giá trị từ các EditText
                String nameValue = name.getText().toString().trim();
                String phoneValue = phone.getText().toString().trim();
                String addressValue = address.getText().toString().trim();

                // Kiểm tra các trường
                if (nameValue.isEmpty()) {
                    Toast.makeText(OrderActivity.this, "Vui lòng nhập tên!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phoneValue.isEmpty()) {
                    Toast.makeText(OrderActivity.this, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (addressValue.isEmpty()) {
                    Toast.makeText(OrderActivity.this, "Vui lòng nhập địa chỉ nhận hàng!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OrderDTO orderDTO = CreateOrderDTO(productList);
                        Order od = OrderUtil.CreateOrder(orderDTO);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();

                                NotiUtil.SendNotiToRole(0,
                                        "Có đơn mới",
                                        "Bạn có đơn mới kìa: #" + od.getId(),
                                        new Date(),od.getId());

                                Intent successIntent = new Intent(OrderActivity.this, OrderSuccessActivity.class);
                                startActivity(successIntent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private OrderDTO CreateOrderDTO(ArrayList<CartDTO> productList) {
        EditText name = findViewById(R.id.order_user_name);
        EditText phone = findViewById(R.id.order_user_phone);
        EditText address = findViewById(R.id.order_user_address);
        EditText note = findViewById(R.id.order_note);


        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setNameUserOrder(name.getText().toString().trim());
        orderDTO.setPhone(phone.getText().toString().trim());
        orderDTO.setAddress(address.getText().toString().trim());
        orderDTO.setNote(note.getText().toString().trim());
        orderDTO.setAddressId(0); // tạm thời chưa
        orderDTO.setShipperId(0);
        orderDTO.setCustomerId(CurrentUser.getId());
        orderDTO.setShipLatitude(0);
        orderDTO.setShipLongtitude(0);

        // danh sách món ăn
        ArrayList<OrderProductDTO> proList = new ArrayList<OrderProductDTO>();
        for (CartDTO product : productList) {

            OrderProductDTO item = new OrderProductDTO();
            item.setProductId(product.getProductID());
            item.setQuantity(product.getQuantity());
            item.setPrice(product.getPrice());

            proList.add(item);
        }

        orderDTO.setProducts(proList);

        return orderDTO;
    }

    // cập nhật data khi kéo
    private void loadOrder(int odId) {
        runOnUiThread(() -> {

            if (odId == 0) {
                ArrayList<CartDTO> productList = (ArrayList<CartDTO>) getIntent().getSerializableExtra("product_list");
                bindData(productList);
            } else {
                GetInfoOrder(odId);
            }
        });
    }

    /////////// xem thông tin đơn hàng
    private void GetInfoOrder(int od) {
        OrderDTO data = OrderUtil.GetOrderInfo(od);

        bindDataOrderInfo(data);
    }

    private void bindDataOrderInfo(OrderDTO orderDTO) {
        // convert qua CartDTO
        ArrayList<CartDTO> cartDTOList = new ArrayList<>();

        for (OrderProductDTO product : orderDTO.getProducts()) {
            CartDTO cartDTO = new CartDTO(
                    product.getProductId(),
                    product.getName(),
                    product.getQuantity(),
                    product.getImage(),
                    product.getPrice()
            );
            cartDTOList.add(cartDTO);
        }

        RecyclerView order_product_view = findViewById(R.id.product_order_container);
        order_product_view.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        ProductOrderAdapter productCartAdapter = new ProductOrderAdapter(this, cartDTOList);
        order_product_view.setAdapter(productCartAdapter);


        EditText name = findViewById(R.id.order_user_name);
        EditText phone = findViewById(R.id.order_user_phone);
        EditText Address = findViewById(R.id.order_user_address);
        EditText Note = findViewById(R.id.order_note);

        name.setText(orderDTO.getNameUserOrder());
        phone.setText(orderDTO.getPhone());
        Address.setText(orderDTO.getAddress());
        Note.setText(orderDTO.getNote());
        TextView totalPay = findViewById(R.id.order_total_pay);

        double total = cartDTOList.stream().mapToDouble(product -> product.getPrice() * product.getQuantity()).sum();
        totalPay.setText("Tổng: " + NumberFormat.getInstance(Locale.getDefault()).format(total) + " VNĐ");

        TextView order = findViewById(R.id.btn_order);
        order.setVisibility(View.GONE);
    }
}