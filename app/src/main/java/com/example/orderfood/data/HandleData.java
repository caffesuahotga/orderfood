package com.example.orderfood.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.orderfood.R;
import com.example.orderfood.models.Account;
import com.example.orderfood.models.Address;
import com.example.orderfood.models.Category;
import com.example.orderfood.models.FeedBack;
import com.example.orderfood.models.Order;
import com.example.orderfood.models.OrderDetail;
import com.example.orderfood.models.Product;
import com.example.orderfood.models.Store;
import com.example.orderfood.models.dto.OrderDTO;
import com.example.orderfood.models.dto.OrderProductDTO;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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
        if(accId.size() == 0)
        {
            return new ArrayList<Account>();
        }

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
    public static Account createAccount(String name ,String userName, String password) throws ExecutionException, InterruptedException {
        // Tạo ID ngẫu nhiên
        int id = generateRandomId();

        // Tạo đối tượng Account mới
        Account newAccount = new Account();
        newAccount.setId(id);
        newAccount.setStoreId(1);
        newAccount.setName(name); // Giá trị mặc định, bạn có thể tùy chỉnh theo yêu cầu
        newAccount.setUsername(userName);
        newAccount.setPassword(password);
        newAccount.setPhone(""); // Giá trị mặc định
        newAccount.setRole(2); // Giá trị mặc định, có thể tùy chỉnh (0 admin, 1 shipper, 2 customer)
        newAccount.setImage("https://res.cloudinary.com/duf1lmvzu/image/upload/v1733065510/profile-default-icon-2048x2045-u3j7s5nj_zspsqa.png"); // Giá trị mặc định

        // Thêm account mới vào Firestore
        Task<Void> addTask = db.collection("account")
                .document(String.valueOf(id))
                .set(newAccount);

        // Đợi task hoàn thành
        Tasks.await(addTask);

        // Trả về account đã tạo
        return newAccount;
    }
    public static boolean checkAccount(String userName) throws ExecutionException, InterruptedException {
        // Tạo task kiểm tra username trong Firestore
        Task<QuerySnapshot> userNameTask = db.collection("account")
                .whereEqualTo("username", userName)
                .get();

        // Đợi task hoàn thành
        QuerySnapshot userNameSnapshot = Tasks.await(userNameTask);

        // Kiểm tra nếu có ít nhất một tài liệu được tìm thấy
        return !userNameSnapshot.isEmpty();
    }
    public static Account getAccountByUsername(String username) {
        try {
            // Thực hiện truy vấn Firestore để tìm account theo username
            Task<QuerySnapshot> accountTask = db.collection("account")
                    .whereEqualTo("username", username)
                    .get();

            // Chờ Task hoàn thành
            QuerySnapshot accountSnapshot = Tasks.await(accountTask);

            if (accountTask.isSuccessful() && !accountSnapshot.isEmpty()) {
                QueryDocumentSnapshot accountDoc = (QueryDocumentSnapshot) accountSnapshot.getDocuments().get(0);
                return accountDoc.toObject(Account.class); // Trả về đối tượng Account
            }
        } catch (Exception e) {
            Log.e("TAG", "Error getting account by username", e);
        }
        return null; // Trả về null nếu có lỗi hoặc không tìm thấy
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

        if(OrderDetailIDs.size() == 0)
        {
            return new ArrayList<FeedBack>();
        }

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
    public static int getLastOrderId() {
        int lastOrderId = 0; // Giá trị mặc định nếu không có đơn hàng nào trước đó
        try {
            Query query = db.collection("order")
                    .orderBy("id", Query.Direction.DESCENDING)
                    .limit(1);

            Task<QuerySnapshot> queryTask = query.get();
            Tasks.await(queryTask);

            if (queryTask.isSuccessful() && !queryTask.getResult().isEmpty()) {
                for (DocumentSnapshot document : queryTask.getResult().getDocuments()) {
                    lastOrderId = Integer.parseInt(document.getId());
                }
            } else if (!queryTask.isSuccessful()) {
                throw new Exception("Error getting last order ID: " + queryTask.getException().getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error getting last order ID: " + e.getMessage());
        }

        return lastOrderId;
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
        if(OrderIDs.size() == 0)
        {
            return new ArrayList<Order>();
        }

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
    public static Order createOrder(OrderDTO dto) {
        try {
            int id = getLastOrderId() + 1;
            Order order = new Order();

            order.setId(id);
            order.setNameUserOrder(dto.getNameUserOrder());
            order.setAddress(dto.getAddress());
            order.setPhone(dto.getPhone());
            order.setNote(dto.getNote());
            order.setAddressId(dto.getAddressId());
            order.setShipperId(dto.getShipperId());
            order.setCustomerId(dto.getCustomerId());
            order.setShipLatitude(dto.getShipLatitude());
            order.setShipLongtitude(dto.getShipLongtitude());
            order.setTotalPrice(dto.getProducts().stream().mapToDouble(product -> product.getPrice() * product.getQuantity()).sum());
            order.setPaymentType(1);

            Task<Void> task = db.collection("order")
                    .document(String.valueOf(order.getId()))
                    .set(order);

            Tasks.await(task);

            if (task.isSuccessful()) {
                // tạo chi tiết đơn hàng
                ArrayList<OrderDetail> ods = new ArrayList<OrderDetail>();
                int curId = getLastOrderDetailId();
                for (OrderProductDTO odetail: dto.getProducts()) {
                    curId++;
                    OrderDetail ode = new OrderDetail();

                    ode.setId(curId);
                    ode.setOrderId(order.getId());
                    ode.setProductId(odetail.getProductId());
                    ode.setPrice(odetail.getPrice());
                    ode.setAmount(odetail.getQuantity());

                    ods.add(ode);
                }

                for (OrderDetail ode : ods) {
                    Task<Void> taskAddOrdetail = db.collection("orderDetail")
                            .document(String.valueOf(ode.getId()))
                            .set(ode);

                    Tasks.await(taskAddOrdetail);

                    if (taskAddOrdetail.isSuccessful()) {
                        System.out.println("OrderDetail created successfully for Order ID: " + order.getId());
                    } else {
                        throw new Exception("Error creating order detail: " + task.getException().getMessage());
                    }
                }

            } else {
                System.err.println("Error creating order: " + task.getException().getMessage());
            }

            return order;
        } catch (Exception e) {
            System.err.println("Error creating order: " + e.getMessage());
        }
        return null;
    }
    public static ArrayList<Order> getAllOrdersByAccountId(int accId) throws ExecutionException, InterruptedException {
        if(accId == 0)
        {
            return new ArrayList<Order>();
        }

        Task<QuerySnapshot> orderTask = db.collection("order")
                .whereIn("customerId", Collections.singletonList(accId))
                .get();

        QuerySnapshot orderSnapshot = Tasks.await(orderTask);
        ArrayList<Order> orderList = new ArrayList<>();

        for (QueryDocumentSnapshot orderDoc : orderSnapshot) {
            Order or = orderDoc.toObject(Order.class);
            orderList.add(or);
        }

        return orderList;
    }

    public static int getLastOrderDetailId() {
        int lastOrderId = 0; // Giá trị mặc định nếu không có đơn hàng nào trước đó
        try {
            Query query = db.collection("orderDetail")
                    .orderBy("id", Query.Direction.DESCENDING)
                    .limit(1);

            Task<QuerySnapshot> queryTask = query.get();
            Tasks.await(queryTask);

            if (queryTask.isSuccessful() && !queryTask.getResult().isEmpty()) {
                for (DocumentSnapshot document : queryTask.getResult().getDocuments()) {
                    lastOrderId = Integer.parseInt(document.getId());
                }
            } else if (!queryTask.isSuccessful()) {
                throw new Exception("Error getting last order ID: " + queryTask.getException().getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error getting last order ID: " + e.getMessage());
        }

        return lastOrderId;
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
//    public OrderDetail CreateOrderDetail(){};

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
                product.setPrice(document.getDouble("price"));
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
    public static List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        try {
            Task<QuerySnapshot> task = db.collection("product").get(); // Thay "products" bằng tên collection của bạn

            // Chờ Task hoàn thành
            while (!task.isComplete()) {
                Thread.sleep(10); // Đợi Task hoàn thành
            }

            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Product product = new Product();
                    product.setId(document.getLong("id").intValue());
                    product.setName(document.getString("name"));
                    product.setImage_source(document.getLong("image_source").intValue());
                    product.setImage((ArrayList<String>) document.get("image"));
                    product.setPrice(document.getDouble("price"));
                    product.setRate(document.getDouble("rate"));
                    product.setMinutes(document.getLong("minutes").intValue());
                    product.setDescription(document.getString("description"));
                    product.setStoreID(document.getLong("storeID").intValue());
                    product.setCategoryID(document.getLong("categoryID").intValue());
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



    //////////////// HELPER


    private static int generateRandomId() {
        // Phương pháp tạo ID ngẫu nhiên (ví dụ sử dụng Random)
        Random random = new Random();
        return random.nextInt(100000); // Bạn có thể tùy chỉnh phạm vi ID
    }

}
