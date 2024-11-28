package com.example.orderfood.test;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.orderfood.R;
import com.example.orderfood.models.Product;
import com.example.orderfood.data.HandleData;
import com.example.orderfood.sqlLite.DatabaseHelper;
import com.example.orderfood.sqlLite.dao.CartDAO;
import com.example.orderfood.sqlLite.model.Cart;

import java.util.List;

public class TestUtil extends AppCompatActivity {

    private static final String TAG = "ProductTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.test_util_activity);

        HandleData handleData = new HandleData();

        new Thread(() -> { // Chạy trong background thread
            List<Product> products = handleData.getAllProducts();
            if (products != null && !products.isEmpty()) {
                for (Product product : products) {
                    Log.d(TAG, "Product Found: ID: " + product.getId() +
                            ", Name: " + product.getName() +
                            ", Image Source: " + product.getImage_source() +
                            ", Price: " + product.getPrice() +
                            ", Rate: " + product.getRate() +
                            ", Minutes: " + product.getMinutes() +
                            ", Description: " + product.getDescription() +
                            ", StoreID: " + product.getStoreID() +
                            ", CategoryID: " + product.getCategoryID());
                }
            } else {
                Log.d(TAG, "No products found or error occurred.");
            }
        }).start();

// Trong activity hoặc bất kỳ lớp nào khác có context
        CartDAO cartDAO = new CartDAO(this);  // Khởi tạo CartDAO với context

// Thêm sản phẩm vào giỏ hàng
        cartDAO.addProduct(1, "Pizza", 2, "pizza_image_url");
        cartDAO.addProduct(2, "Burger", 1, "burger_image_url");

        // Get all products from the cart (List<Cart>)
        List<Cart> cartList = cartDAO.getAllProducts();

// Iterate over the cart list using a for-each loop
//        for (Cart cart : cartList) {
//            // Access each product's details
//            int productID = cart.getProductID();
//            String name = cart.getName();
//            int quantity = cart.getQuantity();
//            String image = cart.getImage();
//
//            // Print product details or perform other actions
//            Log.d("Cart", "Product ID: " + productID);
//            Log.d("Cart", "Name: " + name);
//            Log.d("Cart", "Quantity: " + quantity);
//            Log.d("Cart", "Image: " + image);
//        }

        CartDAO cartDAO2 = new CartDAO(this);
        cartDAO2.addProduct(1, "Pizza", 2, "pizza_image_url");
        cartDAO2.addProduct(2, "Burger", 1, "burger_image_url");

        for (Cart cart : cartList) {
            // Access each product's details
            int Id = cart.getID();
            int productID = cart.getProductID();
            String name = cart.getName();
            int quantity = cart.getQuantity();
            String image = cart.getImage();

            // Print product details or perform other actions
            Log.d("Cart", "ID: " + Id);

            Log.d("Cart", "Product ID: " + productID);
            Log.d("Cart", "Name: " + name);
            Log.d("Cart", "Quantity: " + quantity);
            Log.d("Cart", "Image: " + image);
        }

        // Lấy đường dẫn đến file database
        String dbPath = getDatabasePath("OrderFood.db").getAbsolutePath();
        Log.d(TAG, "Database path: " + dbPath);

    }
}
