package com.example.orderfood.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.orderfood.models.Account;

public class CurrentUser {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static final String PREF_NAME = "current_user_pref";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ROLE = "role";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_STORE_ID = "storeId";

    // Khởi tạo SharedPreferences
    public static void init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }

    public static void saveCurrentUser(Account account) {
        editor.putInt(KEY_ID, account.getId());
        editor.putString(KEY_NAME, account.getName());
        editor.putString(KEY_USERNAME, account.getUsername());
        editor.putString(KEY_PASSWORD, account.getPassword());
        editor.putString(KEY_PHONE, account.getPhone());
        editor.putInt(KEY_ROLE, account.getRole());
        editor.putString(KEY_IMAGE, account.getImage());
        editor.putInt(KEY_STORE_ID, account.getStoreId());


        editor.apply();
    }

    public static Account getCurrentUser() {
        Account user = new Account();
        user.setId(getId());
        user.setName(getName());
        user.setUsername(getUsername());
        user.setPassword(getPassword());
        user.setPhone(getPhone());
        user.setRole(getRole());
        user.setImage(getImage());
        user.setStoreId(getStoreId());
        return user;
    }

    public static void showCurrentUserInfo() {
        Account user = getCurrentUser();
        Log.d("CurrentUser", "ID: " + user.getId());
        Log.d("CurrentUser", "Name: " + user.getName());
        Log.d("CurrentUser", "Username: " + user.getUsername());
        Log.d("CurrentUser", "Password: " + user.getPassword());
        Log.d("CurrentUser", "Phone: " + user.getPhone());
        Log.d("CurrentUser", "Role: " + user.getRole());
        Log.d("CurrentUser", "Image: " + user.getImage());
        Log.d("CurrentUser","StoreId"+ user.getStoreId());
    }

    // Phương thức để xóa thông tin người dùng hiện tại
    public static void clearCurrentUser() {
        editor.clear();
        editor.apply();
    }

    // Getter và Setter cho từng thuộc tính
    public static void setId(int id) {
        editor.putInt(KEY_ID, id);
        editor.apply();
    }

    public static int getId() {
        return sharedPreferences.getInt(KEY_ID, -1);
    }

    public static void setName(String name) {
        editor.putString(KEY_NAME, name);
        editor.apply();
    }

    public static String getName() {
        return sharedPreferences.getString(KEY_NAME, "No Name");
    }

    public static void setUsername(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public static String getUsername() {return sharedPreferences.getString(KEY_USERNAME, "No Username");}

    public static void setPassword(String password) {
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public static String getPassword() {return sharedPreferences.getString(KEY_PASSWORD, "No Password");}

    public static void setPhone(String phone) {
        editor.putString(KEY_PHONE, phone);
        editor.apply();
    }

    public static String getPhone() {
        return sharedPreferences.getString(KEY_PHONE, "No Phone");
    }

    public static void setRole(int role) {
        editor.putInt(KEY_ROLE, role);
        editor.apply();
    }

    public static int getRole() {
        return sharedPreferences.getInt(KEY_ROLE, -1);
    }

    public static void setImage(String image) {
        editor.putString(KEY_IMAGE, image);
        editor.apply();
    }

    public static String getImage() {
        return sharedPreferences.getString(KEY_IMAGE, "No Image");
    }


    public static void setKeyStoreId(int storeId) {
        editor.putInt(KEY_STORE_ID, storeId);
        editor.apply();
    }

    public static int getStoreId() {
        return sharedPreferences.getInt(KEY_STORE_ID, -1);
    }
}
