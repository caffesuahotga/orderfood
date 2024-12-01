package com.example.orderfood.services;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.orderfood.R;
import com.example.orderfood.component.product_adapter;
import com.example.orderfood.component.product_adapter_nomal;
import com.example.orderfood.data.HandleData;
import com.example.orderfood.models.Category;
import com.example.orderfood.models.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductActivity extends BaseNoBottomActivity {
    private List<Category> categoryList;
    private RecyclerView recyclerView1;
    private product_adapter_nomal productAdapter;
    private List<Product> productList = new HandleData().getAllProducts();
    private SwipeRefreshLayout swipeRefreshLayout;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_product, findViewById(R.id.content_frame));

        swipeRefreshLayout = findViewById(R.id.cart_page_refresh);

        LinearLayout buttonContainer = findViewById(R.id.category_parent);
        HandleData handleData = new HandleData();
        categoryList = handleData.getAllCategories();

// Chuyển đổi 30dp sang pixel
        int heightInPx = (int) (35 * getResources().getDisplayMetrics().density);
        int textInPx = (int) (5 * getResources().getDisplayMetrics().density);

        for (Category item : categoryList) {
            Button button = new Button(this);
            button.setId(item.getId());

            // Đặt chiều cao 30dp
            button.setHeight(heightInPx);
            button.setTextSize(textInPx);

            button.setText(item.getName());
            button.setBackgroundResource(R.drawable.button_product_category);

            // Thiết lập LayoutParams cho khoảng cách giữa các button
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    heightInPx // Đặt chiều cao từ giá trị đã chuyển đổi
            );
            params.setMargins(20, 0, 0, 0); // Khoảng cách phải
            button.setLayoutParams(params);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(), "Clicked: " + item.getName(), Toast.LENGTH_SHORT).show();
                    List<Product> filteredProducts = getProductByCategory(item.getId());
                    renderFood(filteredProducts);
                }
            });


            buttonContainer.addView(button);
        }
        renderFood(productList);
        EditText editText = findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    List<Product> filteredProducts = searchProduct(charSequence.toString());
                    renderFood(filteredProducts);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





    }
    public void renderFood(List<Product> filteredProducts){
        recyclerView1 = findViewById(R.id.view_product);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 2);
        recyclerView1.setLayoutManager(gridLayoutManager1);
        productAdapter = new product_adapter_nomal(filteredProducts);
        recyclerView1.setAdapter(productAdapter);
    }
    // viết vào hàm reder để xử lý có nhiều sự thay đổi data render
    public List<Product> getProductByCategory(int  categoryID){
        List<Product> filteredProducts = new ArrayList<>();
        for (Product item : productList){
            if(item.getCategoryID()==categoryID)
                filteredProducts.add(item);

        }
        return filteredProducts;
    }
    // xử lý tìm product theo categoryID với productList có sẵn

    public List<Product> searchProduct(String ex) {
        List<Product> filteredProducts = new ArrayList<>();

        // Kiểm tra nếu chuỗi tìm kiếm không rỗng hoặc null
        if (ex != null && !ex.trim().isEmpty()) {
            // Duyệt qua tất cả sản phẩm trong danh sách
            for (Product item : productList) {
                // Kiểm tra xem tên sản phẩm có chứa chuỗi tìm kiếm
                if (item.getName().toLowerCase().contains(ex.toLowerCase())) {
                    filteredProducts.add(item); // Thêm sản phẩm vào danh sách nếu tìm thấy
                }
            }
        }

        return filteredProducts; // Trả về danh sách các sản phẩm tìm được
    }
    // tìm  kiếm sản phẩm theo tên





}
