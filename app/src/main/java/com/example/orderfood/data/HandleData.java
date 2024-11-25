package com.example.orderfood.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.orderfood.models.Account;
import com.example.orderfood.models.Address;
import com.example.orderfood.models.Category;
import com.example.orderfood.models.FeedBack;
import com.example.orderfood.models.Order;
import com.example.orderfood.models.OrderDetail;
import com.example.orderfood.models.Product;
import com.example.orderfood.models.Store;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HandleData {

    private static final String TAG = "FirestoreData";
    public  static  final FirebaseFirestore db = FirebaseFirestore.getInstance();

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
    public static List<Account> getAllAccounts(List<Integer> accId) throws ExecutionException, InterruptedException {
        Task<QuerySnapshot> accountTask = db.collection("account")
                .whereIn("id", accId)
                .get();

        QuerySnapshot accountSnapshot = Tasks.await(accountTask);
        List<Account> AccountList = new ArrayList<>();

        for (QueryDocumentSnapshot accountDoc : accountSnapshot) {
            Account ac = accountDoc.toObject(Account.class);
            AccountList.add(ac);
        }

        return AccountList;
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
    public static List<FeedBack> getAllFeedbacksByListOrderDetailID(List<Integer> OrderDetailIDs) throws ExecutionException, InterruptedException {
        Task<QuerySnapshot> feedbackTask = db.collection("feedback")
                .whereIn("orderDetailId", OrderDetailIDs)
                .get();

        QuerySnapshot feedbackSnapshot = Tasks.await(feedbackTask);
        List<FeedBack> feedBackS = new ArrayList<>();

        for (QueryDocumentSnapshot feedbackDoc : feedbackSnapshot) {
            FeedBack feedBack = feedbackDoc.toObject(FeedBack.class);
            feedBackS.add(feedBack);
        }
        return  feedBackS;
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
    public static List<Order> getAllOrders(List<Integer> OrderIDs) throws ExecutionException, InterruptedException {
        Task<QuerySnapshot> orderTask = db.collection("order")
                .whereIn("id", OrderIDs)
                .get();

        QuerySnapshot orderSnapshot = Tasks.await(orderTask);
        List<Order> orderList = new ArrayList<>();

        for (QueryDocumentSnapshot orderDoc : orderSnapshot) {
            Order or = orderDoc.toObject(Order.class);
            orderList.add(or);
        }

        return orderList;
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
    public static List<OrderDetail> getOrderDetailsByProductID(int productID) throws ExecutionException, InterruptedException {
        // Tiếp tục truy vấn OrderDetail
        Task<QuerySnapshot> orderDetailTask = db.collection("orderDetail")
                .whereEqualTo("productId", productID)
                .get();

        // Chờ đợi kết quả trả về từ Firestore
        QuerySnapshot orderDetailSnapshot = Tasks.await(orderDetailTask);

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (QueryDocumentSnapshot orderDoc : orderDetailSnapshot) {
            OrderDetail orderDetail = orderDoc.toObject(OrderDetail.class);
            orderDetails.add(orderDetail);
        }

        return orderDetails;
    }

    public static Product getProductById(int id) throws ExecutionException, InterruptedException {
        try {
            // Truy vấn collection "product" với điều kiện whereEqualTo
            Task<QuerySnapshot> task = db.collection("product")
                    .whereEqualTo("id", id)
                    .get();

            QuerySnapshot querySnapshot = Tasks.await(task);

            if (!querySnapshot.isEmpty()) {
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) querySnapshot.getDocuments().get(0);

                Product product = new Product();
                product.setId(document.getLong("id").intValue());
                product.setName(document.getString("name"));
                product.setImage_source(document.getLong("image_source").intValue());
                product.setImage((ArrayList<String>) document.get("image"));
                product.setPrice(document.getString("price"));
                product.setRate(document.getDouble("rate"));
                product.setMinutes(document.getLong("minutes").intValue());
                product.setDescription(document.getString("description"));
                product.setStoreID(document.getLong("storeID").intValue());
                product.setCategoryID(document.getLong("categoryID").intValue());

                return product; // Trả về Product
            }
        } catch (ExecutionException | InterruptedException e) {
            System.err.println("Error retrieving product by ID: " + e.getMessage());
            throw e; // Ném lại ngoại lệ để xử lý ở mức cao hơn nếu cần
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
        return null; // Trả về null nếu không tìm thấy hoặc xảy ra lỗi
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
