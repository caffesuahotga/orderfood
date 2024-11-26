package com.example.orderfood.sqlLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "OrderFood.db";
    private static final int DATABASE_VERSION = 2;  // Incremented version to reflect schema change

    // Table Name
    public static final String TABLE_CART = "cart";

    // Columns
    public static final String COLUMN_ID = "ID"; // Primary key
    public static final String COLUMN_PRODUCT_ID = "productID";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_IMAGE = "image";

    // Create Table SQL
    private static final String CREATE_TABLE_CART =
            "CREATE TABLE " + TABLE_CART + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +  // Changed to autoincremented primary key
                    COLUMN_PRODUCT_ID + " INTEGER, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                    COLUMN_IMAGE + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Table
        db.execSQL(CREATE_TABLE_CART);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // For version 2, update the table to make ID auto-increment
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
            onCreate(db);
        }
    }
}
