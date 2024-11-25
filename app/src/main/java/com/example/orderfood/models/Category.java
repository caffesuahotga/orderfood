package com.example.orderfood.models;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private int Id;
    private String Name;
    private String Image;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public Category() {
    }

    public Category(int id, String name, String image) {
        Id = id;
        Name = name;
        Image = image;
    }



    private static final String TAG = "FirestoreData";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Category getCategoryByID(int id) {
        try {
            Task<QuerySnapshot> task = db.collection("category")
                    .whereEqualTo("id", id)
                    .get();

            while (!task.isComplete()) {
                Thread.sleep(10); // Đợi Task hoàn thành
            }

            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                Category category = new Category();
                category.setId(document.getLong("id").intValue());
                category.setName(document.getString("name"));
                category.setImage(document.getString("image"));
                return category;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting category by ID", e);
        }
        return null;
    }


    public List<Category> getAllCategory() {
        List<Category> categoryList = new ArrayList<>();
        try {
            Task<QuerySnapshot> task = db.collection("category").get();
            while (!task.isComplete()) {
                Thread.sleep(10); // Đợi Task hoàn thành
            }

            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Category category = new Category();
                    category.setId(document.getLong("id").intValue());
                    category.setName(document.getString("name"));
                    category.setImage(document.getString("image"));
                    categoryList.add(category);
                }
                return categoryList;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting all categories", e);
        }
        return null;
    }
}