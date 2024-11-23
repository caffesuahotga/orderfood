package com.example.orderfood.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HandleData {


    private static final String TAG = "FirestoreData";
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    public Account getAccountByID(int id) {
        try {
            Task<QuerySnapshot> task = db.collection("account")
                    .whereEqualTo("Id", id)
                    .get();

            // Chờ Task hoàn thành
            while (!task.isComplete()) {
                Thread.sleep(10); // Đợi Task hoàn thành
            }

            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                Account account = new Account();
                account.setId(document.getLong("Id").intValue());
                account.setName(document.getString("Name"));
                account.setUsername(document.getString("Username"));
                account.setPassword(document.getString("Password"));
                account.setPhone(document.getString("Phone"));
                account.setRole(document.getLong("Role").intValue());
                account.setStoreId(document.getLong("StoreId").intValue());
                return account; // Trả về Account
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting account by ID", e);
        }
        return null; // Trả về null nếu có lỗi hoặc không tìm thấy
    }
    public List<Account> getAllAccounts() {
        List<Account> accountList = new ArrayList<>();
        try {
            Task<QuerySnapshot> task = db.collection("account").get();

            // Chờ Task hoàn thành
            while (!task.isComplete()) {
                Thread.sleep(10); // Đợi Task hoàn thành
            }

            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Account account = new Account();
                    account.setId(document.getLong("Id").intValue());
                    account.setName(document.getString("Name"));
                    account.setUsername(document.getString("Username"));
                    account.setPassword(document.getString("Password"));
                    account.setPhone(document.getString("Phone"));
                    account.setRole(document.getLong("Role").intValue());
                    account.setStoreId(document.getLong("StoreId").intValue());
                    accountList.add(account);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting all accounts", e);
        }
        return accountList;
    }

    public Address getAddressByID(int id) {
        try {
            Task<QuerySnapshot> task = db.collection("address")
                    .whereEqualTo("Id", id)
                    .get();

            // Chờ Task hoàn thành
            while (!task.isComplete()) {
                Thread.sleep(10); // Đợi Task hoàn thành
            }

            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                Address address = new Address();
                address.setId(document.getLong("Id").intValue());
                address.setFullAddress(document.getString("FullAddress"));
                address.setLatitude(document.getString("Latitude"));
                address.setLongtitude(document.getString("Longtitude"));
                address.setAccountId(document.getLong("AccountId").intValue());
                return address; // Trả về Address
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting address by ID", e);
        }
        return null; // Trả về null nếu có lỗi hoặc không tìm thấy
    }
    public List<Address> getAllAddresses() {
        List<Address> addressList = new ArrayList<>();
        try {
            Task<QuerySnapshot> task = db.collection("address").get();

            // Chờ Task hoàn thành
            while (!task.isComplete()) {
                Thread.sleep(10); // Đợi Task hoàn thành
            }

            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Address address = new Address();
                    address.setId(document.getLong("Id").intValue());
                    address.setFullAddress(document.getString("FullAddress"));
                    address.setLatitude(document.getString("Latitude"));
                    address.setLongtitude(document.getString("Longtitude"));
                    address.setAccountId(document.getLong("AccountId").intValue());
                    addressList.add(address);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting all addresses", e);
        }
        return addressList;
    }

    public Category getCategoryByID(int id) {
        try {
            Task<QuerySnapshot> task = db.collection("category")
                    .whereEqualTo("id", id)
                    .get();

            // Chờ Task hoàn thành
            while (!task.isComplete()) {
                Thread.sleep(10); // Đợi Task hoàn thành
            }

            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                Category category = new Category();
                category.setId(document.getLong("id").intValue());
                category.setName(document.getString("name"));
                category.setImage(document.getString("image"));
                return category; // Trả về Category
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting category by ID", e);
        }
        return null; // Trả về null nếu có lỗi hoặc không tìm thấy
    }
    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        try {
            Task<QuerySnapshot> task = db.collection("category").get();

            // Chờ Task hoàn thành
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
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting all categories", e);
        }
        return categoryList;
    }

    public FeedBack getFeedbackByID(int id) {
        try {
            Task<QuerySnapshot> task = db.collection("feedback")
                    .whereEqualTo("Id", id)
                    .get();

            // Chờ Task hoàn thành
            while (!task.isComplete()) {
                Thread.sleep(10); // Đợi Task hoàn thành
            }

            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                FeedBack feedback = new FeedBack();
                feedback.setId(document.getLong("Id").intValue());
                feedback.setOrderDetailId(document.getLong("OrderDetailId").intValue());
                feedback.setContent(document.getString("Content"));
                feedback.setStar(document.getLong("Star").intValue());
                return feedback; // Trả về FeedBack
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting feedback by ID", e);
        }
        return null; // Trả về null nếu có lỗi hoặc không tìm thấy
    }
    public List<FeedBack> getAllFeedbacks() {
        List<FeedBack> feedbackList = new ArrayList<>();
        try {
            Task<QuerySnapshot> task = db.collection("feedback").get();

            // Chờ Task hoàn thành
            while (!task.isComplete()) {
                Thread.sleep(10); // Đợi Task hoàn thành
            }

            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    FeedBack feedback = new FeedBack();
                    feedback.setId(document.getLong("Id").intValue());
                    feedback.setOrderDetailId(document.getLong("OrderDetailId").intValue());
                    feedback.setContent(document.getString("Content"));
                    feedback.setStar(document.getLong("Star").intValue());
                    feedbackList.add(feedback);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting all feedbacks", e);
        }
        return feedbackList;
    }

    public Order getOrderById(int id) {
        try {
            Task<QuerySnapshot> task = db.collection("orders") // Thay "orders" bằng tên collection của bạn
                    .whereEqualTo("Id", id)
                    .get();

            // Chờ Task hoàn thành
            while (!task.isComplete()) {
                Thread.sleep(10); // Đợi Task hoàn thành
            }

            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                Order order = new Order();
                order.setId(document.getLong("Id").intValue());
                order.setAddressId(document.getLong("AddressId").intValue());
                order.setShipperId(document.getLong("ShipperId").intValue());
                order.setCustomerId(document.getLong("CustomerId").intValue());
                order.setShipLatitude(document.getDouble("ShipLatitude"));
                order.setShipLongtitude(document.getDouble("ShipLongtitude"));
                return order; // Trả về Order
            }
        } catch (Exception e) {
            System.err.println("Error getting order by ID: " + e.getMessage());
        }
        return null;
    }
    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        try {
            Task<QuerySnapshot> task = db.collection("orders").get(); // Thay "orders" bằng tên collection của bạn

            // Chờ Task hoàn thành
            while (!task.isComplete()) {
                Thread.sleep(10); // Đợi Task hoàn thành
            }

            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Order order = new Order();
                    order.setId(document.getLong("Id").intValue());
                    order.setAddressId(document.getLong("AddressId").intValue());
                    order.setShipperId(document.getLong("ShipperId").intValue());
                    order.setCustomerId(document.getLong("CustomerId").intValue());
                    order.setShipLatitude(document.getDouble("ShipLatitude"));
                    order.setShipLongtitude(document.getDouble("ShipLongtitude"));
                    orderList.add(order);
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting all orders: " + e.getMessage());
        }
        return orderList; // Trả về danh sách Order
    }

    public OrderDetail getOrderDetailById(int id) {
        try {
            Task<QuerySnapshot> task = db.collection("order_details") // Thay "order_details" bằng tên collection của bạn
                    .whereEqualTo("Id", id)
                    .get();

            // Chờ Task hoàn thành
            while (!task.isComplete()) {
                Thread.sleep(10); // Đợi Task hoàn thành
            }

            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setId(document.getLong("Id").intValue());
                orderDetail.setOrderId(document.getLong("OrderId").intValue());
                orderDetail.setProductId(document.getLong("ProductId").intValue());
                orderDetail.setPrice(document.getLong("Price").intValue());
                orderDetail.setAmount(document.getLong("Amount").intValue());
                return orderDetail; // Trả về OrderDetail
            }
        } catch (Exception e) {
            System.err.println("Error getting order detail by ID: " + e.getMessage());
        }
        return null; // Trả về null nếu không tìm thấy hoặc có lỗi
    }
    public List<OrderDetail> getAllOrderDetails() {
        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
            Task<QuerySnapshot> task = db.collection("order_details").get(); // Thay "order_details" bằng tên collection của bạn

            // Chờ Task hoàn thành
            while (!task.isComplete()) {
                Thread.sleep(10); // Đợi Task hoàn thành
            }

            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setId(document.getLong("Id").intValue());
                    orderDetail.setOrderId(document.getLong("OrderId").intValue());
                    orderDetail.setProductId(document.getLong("ProductId").intValue());
                    orderDetail.setPrice(document.getLong("Price").intValue());
                    orderDetail.setAmount(document.getLong("Amount").intValue());
                    orderDetailList.add(orderDetail);
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting all order details: " + e.getMessage());
        }
        return orderDetailList; // Trả về danh sách OrderDetail
    }

    public Product getProductById(int id) {
        try {
            Task<QuerySnapshot> task = db.collection("products") // Thay "products" bằng tên collection của bạn
                    .whereEqualTo("Id", id)
                    .get();

            // Chờ Task hoàn thành
            while (!task.isComplete()) {
                Thread.sleep(10); // Đợi Task hoàn thành
            }

            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                Product product = new Product();
                product.setId(document.getLong("Id").intValue());
                product.setName(document.getString("name"));
                product.setImage_source(document.getLong("image_source").intValue());
                product.setImage((ArrayList<String>) document.get("Image"));
                product.setPrice(document.getString("price"));
                product.setRate(document.getDouble("rate"));
                product.setMinutes(document.getLong("minutes").intValue());
                product.setDescription(document.getString("Description"));
                product.setStoreID(document.getLong("StoreID").intValue());
                product.setCategoryID(document.getLong("CategoryID").intValue());
                return product; // Trả về Product
            }
        } catch (Exception e) {
            System.err.println("Error getting product by ID: " + e.getMessage());
        }
        return null; // Trả về null nếu không tìm thấy hoặc có lỗi
    }
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        try {
            Task<QuerySnapshot> task = db.collection("products").get(); // Thay "products" bằng tên collection của bạn

            // Chờ Task hoàn thành
            while (!task.isComplete()) {
                Thread.sleep(10); // Đợi Task hoàn thành
            }

            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Product product = new Product();
                    product.setId(document.getLong("Id").intValue());
                    product.setName(document.getString("name"));
                    product.setImage_source(document.getLong("image_source").intValue());
                    product.setImage((ArrayList<String>) document.get("Image"));
                    product.setPrice(document.getString("price"));
                    product.setRate(document.getDouble("rate"));
                    product.setMinutes(document.getLong("minutes").intValue());
                    product.setDescription(document.getString("Description"));
                    product.setStoreID(document.getLong("StoreID").intValue());
                    product.setCategoryID(document.getLong("CategoryID").intValue());
                    productList.add(product);
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting all products: " + e.getMessage());
        }
        return productList; // Trả về danh sách Product
    }

    public Store getStoreById(int id) {
        try {
            Task<QuerySnapshot> task = db.collection("stores") // Thay "stores" bằng tên collection của bạn
                    .whereEqualTo("Id", id)
                    .get();

            while (!task.isComplete()) {
                Thread.sleep(10); // Đợi Task hoàn thành
            }

            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                Store store = new Store();
                store.setId(document.getLong("Id").intValue());
                store.setName(document.getString("Name"));
                store.setLatitude(document.getString("Latitude"));
                store.setLongtitude(document.getString("Longtitude"));
                return store; // Trả về Store
            }
        } catch (Exception e) {
            System.err.println("Error getting store by ID: " + e.getMessage());
        }
        return null; // Trả về null nếu không tìm thấy hoặc có lỗi
    }
    public List<Store> getAllStores() {
        List<Store> storeList = new ArrayList<>();
        try {
            Task<QuerySnapshot> task = db.collection("stores").get();
            while (!task.isComplete()) {
                Thread.sleep(10);
            }

            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Store store = new Store();
                    store.setId(document.getLong("Id").intValue());
                    store.setName(document.getString("Name"));
                    store.setLatitude(document.getString("Latitude"));
                    store.setLongtitude(document.getString("Longtitude"));
                    storeList.add(store);
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting all stores: " + e.getMessage());
        }
        return storeList;
    }




































                public void getCategories(final FirestoreCallback callback) {
        db.collection("category")       
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Category> categoryList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Lấy dữ liệu từ Firestore và tạo đối tượng Category
                            Category category = new Category();
                            category.setId(document.getLong("id").intValue()); // Chuyển kiểu Long sang int
                            category.setName(document.getString("name"));
                            category.setImage(document.getString("image"));

                            categoryList.add(category); // Thêm vào danh sách
                        }
                        callback.onCallback(categoryList); // Trả kết quả qua callback
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                        callback.onCallback(null); // Trả về null nếu có lỗi
                    }
                });
    }

    public interface FirestoreCallback {
        void onCallback(List<Category> categoryList);
    }







}
