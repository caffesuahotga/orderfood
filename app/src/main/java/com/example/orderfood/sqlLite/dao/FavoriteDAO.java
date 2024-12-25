package com.example.orderfood.sqlLite.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.orderfood.data.HandleData;
import com.example.orderfood.models.Product;
import com.example.orderfood.models.dto.FavoriteDTO;
import com.example.orderfood.sqlLite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO {
    private static DatabaseHelper dbHelper = null;
    public FavoriteDAO(Context context){
         dbHelper = new DatabaseHelper(context);
    }





    public long addFavorite(int productID, String name, String image, String description ){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_FAVORITE,
                new String[]{DatabaseHelper.COLUMN_PRODUCT_ID_FAVORITE},
                DatabaseHelper.COLUMN_PRODUCT_ID_FAVORITE +" = ?",
                new String[]{String.valueOf(productID)},
                null,null,null
        );
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return -1; // Trả về -1 để thông báo rằng sản phẩm đã tồn tại
        }
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PRODUCT_ID_FAVORITE,productID);
        values.put(DatabaseHelper.COLUMN_NAME_FAVORITE, name);
        values.put(DatabaseHelper.COLUMN_IMAGE_FAVORITE, image);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION_FAVORITE,description);
        long result = db.insert(DatabaseHelper.TABLE_FAVORITE, null, values);
        db.close();
        return result;
    }

    public int updateDescription(int productID, String description){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DESCRIPTION_FAVORITE, description);
        int rows = db.update(DatabaseHelper.TABLE_FAVORITE,values,
                DatabaseHelper.COLUMN_PRODUCT_ID_FAVORITE + " = ?", new String[]{String.valueOf(productID)});
        db.close();
        return rows;
    }

    public int deleteProduct(int productID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(DatabaseHelper.TABLE_FAVORITE,
                DatabaseHelper.COLUMN_PRODUCT_ID_FAVORITE + " = ?", new String[]{String.valueOf(productID)});
        db.close();
        return rows;
    }
    public ArrayList<FavoriteDTO> getAllProducts(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Product> products = HandleData.getAllProducts(); // keo product từ mạng về để tránh price bị đổi
        Cursor cursor= db.query(DatabaseHelper.TABLE_FAVORITE,null, null, null, null, null, null);
        ArrayList<FavoriteDTO> favoriteList = new ArrayList<>();
        if(cursor != null && cursor.moveToFirst()){
            int IDIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_FAVORITE);
            int productIDIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_ID_FAVORITE);
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_FAVORITE);
            int imageIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE_FAVORITE);
            int descriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION_FAVORITE);

            if(productIDIndex >= 0 && nameIndex >= 0 && nameIndex >= 0 && descriptionIndex >= 0){
                do{
                    int ID = cursor.getInt(IDIndex);
                    int productID = cursor.getInt(productIDIndex);
                    String name = cursor.getString(nameIndex);
                    String image = cursor.getString(imageIndex);
                    String description = cursor.getString(descriptionIndex);

                    Product proItem = products.stream()
                            .filter(product -> product.getId() == productID)
                            .findFirst()
                            .orElse(null);
                    if (proItem == null) {
                        continue; // Nếu không tìm thấy sản phẩm, bỏ qua
                    }
                    double price = proItem.getPrice() ;
                    double rate = proItem.getRate();
                    FavoriteDTO  favoriteDTO  = new FavoriteDTO(ID,productID, name, price, image, rate, description);
                    favoriteList.add(favoriteDTO);

                }while (cursor.moveToNext());

            }else {
                // Handle the case when columns are not found
                Log.e("CartDAO", "One or more columns are missing in the database.");
            }
        }
        cursor.close();
        db.close();
        return favoriteList;
    }
    public  boolean check_favorite(int productID) {


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_FAVORITE, null, null, null, null, null, null);
        boolean isFavorite = false;

        try {
            // Truy vấn kiểm tra sản phẩm có trong bảng Favorite hay không
            cursor = db.query(
                    DatabaseHelper.TABLE_FAVORITE,
                    new String[]{DatabaseHelper.COLUMN_PRODUCT_ID_FAVORITE}, // Chỉ cần lấy cột productID
                    DatabaseHelper.COLUMN_PRODUCT_ID_FAVORITE + " = ?",      // Điều kiện WHERE
                    new String[]{String.valueOf(productID)},                 // Tham số cho điều kiện
                    null, null, null
            );

            // Nếu tìm thấy ít nhất một bản ghi thì sản phẩm đã được thêm vào danh sách yêu thích
            if (cursor != null && cursor.getCount() > 0) {
                isFavorite = true;
            }
        } finally {
            // Đảm bảo đóng con trỏ và cơ sở dữ liệu
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return isFavorite;
    }

    public FavoriteDTO getProductById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Product> products = HandleData.getAllProducts(); // Kéo sản phẩm từ mạng về để tránh giá bị đổi
        FavoriteDTO favoriteDTO = null;

        // Truy vấn với điều kiện WHERE
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_FAVORITE,
                null,
                DatabaseHelper.COLUMN_ID_FAVORITE + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int productIDIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_ID_FAVORITE);
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_FAVORITE);
            int imageIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE_FAVORITE);
            int descriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION_FAVORITE);

            if (productIDIndex >= 0 && nameIndex >= 0 && imageIndex >= 0 && descriptionIndex >= 0) {
                int productID = cursor.getInt(productIDIndex);
                String name = cursor.getString(nameIndex);
                String image = cursor.getString(imageIndex);
                String description = cursor.getString(descriptionIndex);

                Product proItem = products.stream()
                        .filter(product -> product.getId() == productID)
                        .findFirst()
                        .orElse(null);
                if (proItem != null) {
                    double price = proItem.getPrice();
                    double rate = proItem.getRate();
                    favoriteDTO = new FavoriteDTO(id, productID, name, price, image, rate, description);
                }
            } else {
                // Xử lý trường hợp không tìm thấy cột
                Log.e("FavoriteDAO", "One or more columns are missing in the database.");
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return favoriteDTO;
    }



}
