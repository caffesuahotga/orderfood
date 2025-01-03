package com.example.orderfood.sqlLite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.orderfood.data.HandleData;
import com.example.orderfood.models.Product;
import com.example.orderfood.models.dto.CartDTO;
import com.example.orderfood.sqlLite.DatabaseHelper;
import com.example.orderfood.sqlLite.model.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    private static DatabaseHelper dbHelper = null;

    public CartDAO(Context context) {

        dbHelper = new DatabaseHelper(context);
    }

    // Add product to cart ( không add trùng id product )
    public long addProduct(int productID, String name, int quantity, String image, int accountID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Kiểm tra nếu productID đã tồn tại trong giỏ hàng của accountID cụ thể
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_CART,
                new String[]{DatabaseHelper.COLUMN_PRODUCT_ID},
                DatabaseHelper.COLUMN_PRODUCT_ID + " = ? AND " + DatabaseHelper.COLUMN_ID_ACCOUNT_CART + " = ?",
                new String[]{String.valueOf(productID), String.valueOf(accountID)},
                null, null, null
        );

        // Nếu tìm thấy sản phẩm, không thêm vào giỏ hàng
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return -1; // Trả về -1 để thông báo rằng sản phẩm đã tồn tại trong giỏ hàng của tài khoản
        }

        // Nếu sản phẩm chưa tồn tại, tiến hành thêm vào giỏ hàng
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PRODUCT_ID, productID);
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_QUANTITY, quantity);
        values.put(DatabaseHelper.COLUMN_IMAGE, image);
        values.put(DatabaseHelper.COLUMN_ID_ACCOUNT_CART, accountID); // Gắn accountID vào bản ghi

        long result = db.insert(DatabaseHelper.TABLE_CART, null, values);
        db.close();
        return result; // Trả về id của bản ghi đã được thêm vào, hoặc -1 nếu thất bại
    }


    // Update product quantity
    public int updateProductQuantity(int productID, int quantity, int accountID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_QUANTITY, quantity);

        // Thêm điều kiện accountID vào câu lệnh WHERE
        int rows = db.update(
                DatabaseHelper.TABLE_CART,
                values,
                DatabaseHelper.COLUMN_PRODUCT_ID + " = ? AND " + DatabaseHelper.COLUMN_ID_ACCOUNT_CART + " = ?",
                new String[]{String.valueOf(productID), String.valueOf(accountID)}
        );

        db.close();
        return rows; // Trả về số dòng bị ảnh hưởng
    }


    // Delete product from cart
    public int deleteProduct(int accountID, int productID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(
                DatabaseHelper.TABLE_CART,
                DatabaseHelper.COLUMN_PRODUCT_ID + " = ? AND " + DatabaseHelper.COLUMN_ID_ACCOUNT_CART + " = ?",
                new String[]{String.valueOf(productID), String.valueOf(accountID)}
        );
        db.close();
        return rows;
    }


    public int deleteAll(int accountID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Thêm điều kiện WHERE để chỉ xóa các dòng thuộc AccountID
        int rowsDeleted = db.delete(DatabaseHelper.TABLE_CART,
                DatabaseHelper.COLUMN_ID_ACCOUNT_CART + " = ?", // Điều kiện WHERE
                new String[]{String.valueOf(accountID)}); // Giá trị điều kiện
        db.close();
        return rowsDeleted;
    }


    // Get all products from cart and return as a list of Cart objects
    public  ArrayList<CartDTO> getAllProducts(int accountID) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Product> products = HandleData.getAllProducts(); // keo product từ mạng về để tránh price bị đổi

        String selection = DatabaseHelper.COLUMN_ID_ACCOUNT_CART + " = ?";
        String[] selectionArgs = {String.valueOf(accountID)};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_CART, // Tên bảng
                null,                      // Lấy tất cả các cột
                selection,                 // Điều kiện WHERE
                selectionArgs,             // Tham số cho điều kiện WHERE
                null,                      // groupBy
                null,                      // having
                null                       // orderBy
        );

        ArrayList<CartDTO> cartList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            // Get column indices
            int IDIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
            int productIDIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_ID);
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
            int quantityIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY);
            int imageIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE);

            // Ensure indices are valid
            if (productIDIndex >= 0 && nameIndex >= 0 && quantityIndex >= 0 && imageIndex >= 0) {
                do {
                    // Lấy thông tin từng sản phẩm từ cursor
                    int ID = cursor.getInt(IDIndex);
                    int productID = cursor.getInt(productIDIndex);
                    String name = cursor.getString(nameIndex);
                    int quantity = cursor.getInt(quantityIndex);
                    String image = cursor.getString(imageIndex);

                    Product proItem = products.stream()
                            .filter(product -> product.getId() == productID)
                            .findFirst()
                            .orElse(null);

                    if (proItem == null) {
                        continue; // Nếu không tìm thấy sản phẩm, bỏ qua
                    }

                    double price = proItem.getPrice() * quantity;

                    CartDTO cart = new CartDTO(ID,productID, name, quantity, image, price, accountID);
                    cartList.add(cart);
                } while (cursor.moveToNext());
            } else {
                // Handle the case when columns are not found
                Log.e("CartDAO", "One or more columns are missing in the database.");
            }
        }
        cursor.close();
        db.close();
        return cartList;
    }

    public  void showAllProducts(int accountID) {
        // Lấy tất cả sản phẩm từ giỏ hàng
        List<CartDTO> cartList = getAllProducts(accountID);

        // Kiểm tra xem có sản phẩm không
        if (cartList != null && !cartList.isEmpty()) {
            // Nếu có sản phẩm, bạn có thể hiển thị sản phẩm ở đây, ví dụ sử dụng ListView hoặc RecyclerView
            for (CartDTO cart : cartList) {
                // Bạn có thể in thông tin sản phẩm ra Log hoặc hiển thị trong giao diện người dùng
                Log.d("ProductInfo", "Product ID: " + cart.getProductID() + ", Name: " + cart.getName() + ", Quantity: " + cart.getQuantity());
            }
        } else {
            // Nếu không có sản phẩm, bạn có thể thông báo người dùng
            Log.d("ProductInfo", "No products in the cart.");
        }
    }
}

