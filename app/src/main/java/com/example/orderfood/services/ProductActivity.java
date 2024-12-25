package com.example.orderfood.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.orderfood.R;
import com.example.orderfood.component.CategoryNomalAdapter;
import com.example.orderfood.component.category_adapter;
import com.example.orderfood.component.product_adapter_nomal;
import com.example.orderfood.data.HandleData;
import com.example.orderfood.models.Category;
import com.example.orderfood.models.Product;
import com.example.orderfood.models.dto.FavoriteDTO;
import com.example.orderfood.sqlLite.dao.FavoriteDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductActivity extends BaseTopBottomViewActivity {
    private List<Category> categoryList;
    private RecyclerView recyclerView1;
    private product_adapter_nomal productAdapter;
    private List<Product> productList = new HandleData().getAllProducts();
    private RecyclerView recyclerView2;
    private CategoryNomalAdapter categoryNomalAdapter;
    private Context context;
    private  List<FavoriteDTO> favoriteDTOList;
    private FavoriteDAO favoriteDAO = new FavoriteDAO(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_product, findViewById(R.id.content_frame_top_bot));
        HandleData handleData = new HandleData();
        categoryList = handleData.getAllCategories();
        recyclerView2 = findViewById(R.id.categorytest);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager);
        categoryNomalAdapter = new CategoryNomalAdapter(categoryList);
        recyclerView2.setAdapter(categoryNomalAdapter);

        int categoryID = getIntent().getIntExtra("categoryID", -1);


        if (categoryID != -1) {
            // Tìm vị trí của danh mục có ID tương ứng
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).getId() == categoryID) {
                    categoryNomalAdapter.setSelectedPosition(i);
                    break;
                }
            }
            renderFood(searchProductByIDCategory(categoryID));
        } else {
            renderFood(productList);
            Toast.makeText(this, "Invalid category ID", Toast.LENGTH_SHORT).show();
        }



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
        productAdapter = new product_adapter_nomal(this,filteredProducts);
        recyclerView1.setAdapter(productAdapter);

    }

    public List<Product> searchProduct(String ex) {
        List<Product> filteredProducts = new ArrayList<>();
        if (ex != null && !ex.trim().isEmpty()) {
            for (Product item : productList) {
                if (item.getName().toLowerCase().contains(ex.toLowerCase())) {
                    filteredProducts.add(item);
                }
            }
            Toast.makeText(this, "Category ID: " + 566, Toast.LENGTH_SHORT).show();

        }
        return filteredProducts;
    }

    public List<Product> searchProductByIDCategory(int id) {
        List<Product> products = new ArrayList<>();
        for (Product item : productList) {
            if (item.getCategoryID() == id)
                products.add(item);
        }
        return products;
    }
}
