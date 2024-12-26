package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
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
import com.example.orderfood.data.HandleData;
import com.example.orderfood.data.NotiUtil;
import com.example.orderfood.data.OrderUtil;
import com.example.orderfood.models.Order;
import com.example.orderfood.models.dto.CartDTO;
import com.example.orderfood.models.dto.OrderDTO;
import com.example.orderfood.models.dto.OrderProductDTO;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderActivity extends BaseNoBottomActivity implements OnMapReadyCallback {
    private SwipeRefreshLayout swipeRefreshLayout;
    private GoogleMap mMap;

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

        // Lấy SupportMapFragment và nhận thông báo khi bản đồ đã sẵn sàng
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.getUiSettings().setMapToolbarEnabled(true);

        mMap.getUiSettings().setRotateGesturesEnabled(true);

        mMap.getUiSettings().setScrollGesturesEnabled(true);

        // Thêm marker đầu tiên (màu đỏ)
        LatLng location1 = new LatLng(10.67528679747168, 106.69065846511778);  // 10.67528679747168, 106.69065846511778
        mMap.addMarker(new MarkerOptions().position(location1).title("Ho Chi Minh City Open University")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        // Thêm marker thứ hai (màu xanh lá)
        LatLng location2 = new LatLng(10.675584641302393, 106.69159723822689);  // 10.675584641302393, 106.69159723822689
        mMap.addMarker(new MarkerOptions().position(location2).title("Training and Competition House")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        // Di chuyển và zoom camera để hiển thị cả hai marker
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(location1);
        builder.include(location2);
        LatLngBounds bounds = builder.build();

        int padding = 100; // padding theo pixel
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }


    // bind data đặt hàng
    private void bindData(ArrayList<CartDTO> productList) {
        RecyclerView order_product_view = findViewById(R.id.product_order_container);
        order_product_view.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        ProductOrderAdapter productCartAdapter = new ProductOrderAdapter(this, productList);
        order_product_view.setAdapter(productCartAdapter);


        bindInforUser();

        order(productList);

        TextView feedback = findViewById(R.id.btn_order_feedback);
        feedback.setVisibility(View.GONE);
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
                                        new Date(), od.getId());

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
                    product.getPrice(),
                    product.getFeedback(),
                    product.getOrderDetailId(),
                    product.getStar()

            );
            cartDTOList.add(cartDTO);
        }

        RecyclerView order_product_view = findViewById(R.id.product_order_container);
        order_product_view.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        // trống thì cho.
        boolean allFeedbackEmpty = orderDTO.getProducts().stream().allMatch(product -> product.getFeedback() == null || product.getFeedback().isEmpty());
        ProductOrderAdapter productCartAdapter = new ProductOrderAdapter(this, cartDTOList,orderDTO.getStatus() == 4);
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

        TextView feedback = findViewById(R.id.btn_order_feedback);

        if (orderDTO.getStatus() == 4 && CurrentUser.getRole() == 2 && allFeedbackEmpty) {

            // gui feedback
            feedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Kiểm tra tất cả sản phẩm trong cartDTOList
                    boolean allFeedbackGiven = true;
                    for (CartDTO product : cartDTOList) {
                        if (product.getFeedback() == null || product.getFeedback().isEmpty()) {
                            allFeedbackGiven = false;
                            break;
                        }
                    }

                    if (!allFeedbackGiven) {
                        // Nếu có sản phẩm chưa được ghi phản hồi, hiển thị Toast
                        Toast.makeText(view.getContext(), "Please provide feedback for all products.", Toast.LENGTH_SHORT).show();
                        return; // Dừng lại, không thực hiện gửi feedback
                    }


                    // Hiển thị ProgressDialog
                    ProgressDialog progressDialog = new ProgressDialog(view.getContext());
                    progressDialog.setMessage("Sending feedback...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    // Chạy quá trình gửi phản hồi trên một luồng phụ
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //ArrayList<CartDTO> cartDTOList
                            boolean ck = OrderUtil.Feedback(cartDTOList);

                            // Cập nhật giao diện người dùng trên luồng chính
                            ((Activity) view.getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Đóng ProgressDialog sau khi hoàn thành
                                    progressDialog.dismiss();

                                    // Hiển thị thông báo kết quả
                                    if (ck) {
                                        Toast.makeText(view.getContext(), "Feedback sent successfully!", Toast.LENGTH_SHORT).show();
                                        ((Activity) view.getContext()).finish();
                                    } else {
                                        Toast.makeText(view.getContext(), "Failed to send feedback. Try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).start();
                }
            });
        } else {
            feedback.setVisibility(View.GONE);
        }
    }
}