package com.example.orderfood.services;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.orderfood.R;
import com.example.orderfood.models.Account;
import com.example.orderfood.models.Address;
import com.example.orderfood.models.Category;
import com.example.orderfood.models.FeedBack;
import com.example.orderfood.models.Order;
import com.example.orderfood.models.OrderDetail;
import com.example.orderfood.models.Product;
import com.example.orderfood.models.Store;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeedingDataActivity extends AppCompatActivity {

    private static final String TAG = "FirestoreData";
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeding_data);

//        addMultipleAccountsToFirestore(db);
            addProduct();
//        // thêm accoount lên db
//        addMultipleAccountsToFirestore(db);
          addCategory();
//        addStore();

//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(task -> {
//                    if (!task.isSuccessful()) {
//                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                        return;
//                    }
//
//                    // Lấy FCM token và log ra để gửi thông báo tới thiết bị này
//                    String token = task.getResult();
//                    Log.d(TAG, "FCM Token: " + token);
//                });

    }

    // account
    private void addMultipleAccountsToFirestore(FirebaseFirestore db) {
// Tạo danh sách các tài khoản cần thêm hoặc cập nhật
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account(1, "Admin User", "adminuser", "adminpass", "0123456789", 0, 1, "https://res.cloudinary.com/duf1lmvzu/image/upload/v1732522352/ac2aab4b58cd4472edcfd62bd4618177_lpa8eo.gif"));
        accounts.add(new Account(2, "Customer One", "customer1", "customerpass1", "0987654321", 2, 1,"https://res.cloudinary.com/duf1lmvzu/image/upload/v1732524468/b028096d34128a39b8f90ef834307f0e_q0w705.jpg"));
        accounts.add(new Account(3, "Customer Two", "customer2", "customerpass2", "0112233445", 2, 1,"https://res.cloudinary.com/duf1lmvzu/image/upload/v1732524487/cute-girl-pic44_ka7ri1.jpg"));
        accounts.add(new Account(4, "Shipper One", "shipper1", "shipperpass1", "0554433221", 1, 1,"https://res.cloudinary.com/duf1lmvzu/image/upload/v1732524798/39187815-2900-4b3a-ae70-e6977d5111dc_qh3wln.jpg"));
        accounts.add(new Account(5, "Shipper Two", "shipper2", "shipperpass2", "0665544332", 1, 1,"https://res.cloudinary.com/duf1lmvzu/image/upload/v1732524841/183d9076-bfe4-4363-b4f4-6e993599a661_iyfauk.jpg"));

        for (Account account : accounts) {
            Map<String, Object> accountData = new HashMap<>();
            accountData.put("id", account.getId());
            accountData.put("name", account.getName());
            accountData.put("username", account.getUsername());
            accountData.put("password", account.getPassword());
            accountData.put("phone", account.getPhone());
            accountData.put("role", account.getRole());
            accountData.put("storeId", account.getStoreId());
            accountData.put("image", account.getImage());

            // Chọn tài liệu dựa trên "id" của tài khoản
            DocumentReference docRef = db.collection("account").document(String.valueOf(account.getId()));

            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    // Nếu tài liệu đã tồn tại, thực hiện cập nhật
                    docRef.update(accountData)
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "Cập nhật thành công cho ID tài liệu: " + account.getId()))
                            .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi cập nhật tài liệu", e));
                } else {
                    // Nếu tài liệu không tồn tại, tạo mới
                    docRef.set(accountData)
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "Thêm mới thành công với ID tài liệu: " + account.getId()))
                            .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi thêm tài liệu", e));
                }
            });
        }


        // tạo địa chỉ
        // Tạo danh sách địa chỉ
        List<Address> addresses = new ArrayList<>();

        // Địa chỉ cho Admin User (Account ID: 1)
        addresses.add(new Address(1, "123 Admin Street", "10.762622", "106.660172", 1));
        addresses.add(new Address(2, "456 Admin Avenue", "10.762723", "106.660273", 1));
        addresses.add(new Address(3, "789 Admin Road", "10.762824", "106.660374", 1));

        // Địa chỉ cho Customer One (Account ID: 2)
        addresses.add(new Address(4, "123 Customer Street", "10.763622", "106.661172", 2));
        addresses.add(new Address(5, "456 Customer Avenue", "10.763723", "106.661273", 2));
        addresses.add(new Address(6, "789 Customer Road", "10.763824", "106.661374", 2));

        // Địa chỉ cho Customer Two (Account ID: 3)
        addresses.add(new Address(7, "123 Customer Two Street", "10.764622", "106.662172", 3));
        addresses.add(new Address(8, "456 Customer Two Avenue", "10.764723", "106.662273", 3));
        addresses.add(new Address(9, "789 Customer Two Road", "10.764824", "106.662374", 3));

        // Địa chỉ cho Shipper One (Account ID: 4)
        addresses.add(new Address(10, "123 Shipper Street", "10.765622", "106.663172", 4));
        addresses.add(new Address(11, "456 Shipper Avenue", "10.765723", "106.663273", 4));
        addresses.add(new Address(12, "789 Shipper Road", "10.765824", "106.663374", 4));

        // Địa chỉ cho Shipper Two (Account ID: 5)
        addresses.add(new Address(13, "123 Shipper Two Street", "10.766622", "106.664172", 5));
        addresses.add(new Address(14, "456 Shipper Two Avenue", "10.766723", "106.664273", 5));
        addresses.add(new Address(15, "789 Shipper Two Road", "10.766824", "106.664374", 5));

        // Thêm tất cả địa chỉ vào Firestore
        for (Address address : addresses) {
            Map<String, Object> addressData = new HashMap<>();
            addressData.put("id", address.getId());
            addressData.put("fullAddress", address.getFullAddress());
            addressData.put("latitude", address.getLatitude());
            addressData.put("longitude", address.getLongtitude());
            addressData.put("accountId", address.getAccountId());

            db.collection("address")
                    .document(String.valueOf(address.getId()))
                    .set(addressData)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Thêm địa chỉ thành công cho Account ID: " + address.getAccountId()))
                    .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi thêm địa chỉ", e));
        }
    }


    // tạo 1 store
    private void addStore() {
        Store store = new Store(1, "Cửa hàng ĐỒ Ăn Bao Bao", "10", "10");

        Map<String, Object> storeMap = new HashMap<>();
        storeMap.put("id", store.getId());
        storeMap.put("name", store.getName());
        storeMap.put("latitude", store.getLatitude());
        storeMap.put("longtitude", store.getLongtitude());

        db.collection("store")
                .document("1")
                .set(storeMap)
                .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi thêm store", e));

    }


    // tạo product ( tạo 1 product )
    private void addProduct() {
        Product pro1 = new Product(
                1,
                "Cơm Sườn Sốt Chua Ngọt Mật Ong",
                101,
                new ArrayList<>(Arrays.asList("https://res.cloudinary.com/duf1lmvzu/image/upload/v1732455101/3_cdjfuj.jpg",
                        "https://res.cloudinary.com/duf1lmvzu/image/upload/v1732455101/2_druuh2.jpg",
                        "https://res.cloudinary.com/duf1lmvzu/image/upload/v1732455103/1_ms2fch.png",
                        "https://res.cloudinary.com/duf1lmvzu/image/upload/v1732457411/download_xqzbxs.jpg",
                        "https://res.cloudinary.com/duf1lmvzu/image/upload/v1732457418/savsdav_jnlnqm.jpg",
                        "https://res.cloudinary.com/duf1lmvzu/image/upload/v1732457546/images_lgzvre.jpg")),
                100000,
                4.5,
                30,
                "Cơm sườn sốt mật ong là món ăn đặc biệt, kết hợp giữa sườn heo mềm và nước sốt mật ong đậm đà, tạo nên một trải nghiệm ẩm thực khó quên. Sườn heo được chọn lọc kỹ càng, tẩm ướp với các gia vị truyền thống, sau đó nướng hoặc chiên đến khi thịt săn lại và vàng đều. Sốt mật ong được chế biến từ mật ong nguyên chất, kết hợp cùng các gia vị như tỏi, hành và một chút tiêu, mang lại hương vị ngọt ngào, mặn mà đầy quyến rũ.\\n" +
                        "\\n" +
                        "Món ăn được dọn kèm với cơm trắng nóng hổi, tạo nên sự hòa quyện tuyệt vời giữa vị ngọt của mật ong và vị mặn của sườn. Sự kết hợp này khiến cơm sườn sốt mật ong trở thành món ăn được yêu thích trong những bữa ăn gia đình hay những dịp sum vầy.\\n" +
                        "\\n" +
                        "Với sự sáng tạo trong cách chế biến và trình bày, cơm sườn sốt mật ong không chỉ hấp dẫn bởi hương vị mà còn bởi vẻ ngoài bắt mắt. Món ăn này chắc chắn sẽ khiến thực khách khó lòng quên, mang lại cảm giác ngon miệng và thỏa mãn.",
                1,
                1
        );

        Map<String, Object> proMap = new HashMap<>();

        proMap.put("id", pro1.getId());
        proMap.put("name", pro1.getName());
        proMap.put("image_source", pro1.getImage_source());
        proMap.put("image", pro1.getImage());
        proMap.put("price", pro1.getPrice());
        proMap.put("rate", pro1.getRate());
        proMap.put("minutes", pro1.getMinutes());
        proMap.put("description", pro1.getDescription());
        proMap.put("storeID", pro1.getStoreID());
        proMap.put("categoryID", pro1.getCategoryID());

        db.collection("product")
                .document("1")
                .set(proMap)
                .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi thêm pro", e));
    }

    // tạo order ( tạo 1 đơn )
    private void addOrder()
    {
        Order or = new Order(1,1,4,2,10,10);

        Map<String, Object> orMap = new HashMap<>();
        orMap.put("id", or.getId());
        orMap.put("addressId", or.getAddressId());
        orMap.put("shipperId", or.getShipperId());
        orMap.put("customerId", or.getCustomerId());
        orMap.put("shipLatitude", or.getShipLatitude());
        orMap.put("shipLongitude", or.getShipLongtitude());

        db.collection("order")
                .document("1")
                .set(orMap)
                .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi thêm order", e));

    }

    private void addOrderDetail()
    {
        OrderDetail orDetail = new OrderDetail(1,1,1,20000,1);
        Map<String, Object> orDetailMap = new HashMap<>();
        orDetailMap.put("id", orDetail.getId());
        orDetailMap.put("orderId", orDetail.getOrderId());
        orDetailMap.put("productId", orDetail.getProductId());
        orDetailMap.put("price", orDetail.getPrice());
        orDetailMap.put("amount", orDetail.getAmount());

        db.collection("orderDetail")
                .document("1")
                .set(orDetailMap)
                .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi thêm order", e));

    }

    private void addFeedBack()
    {
        FeedBack fb = new FeedBack(1,1,"Bình luân của siêu cấp khách hàng",5);
        Map<String, Object> fbMap = new HashMap<>();

        fbMap.put("id", fb.getId());
        fbMap.put("orderDetailId", fb.getOrderDetailId());
        fbMap.put("content", fb.getContent());
        fbMap.put("star", fb.getStar());

        db.collection("feedback")
                .document("1")
                .set(fbMap)
                .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi thêm feedback", e));
    }
    private void addCategory()
    {

        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1,"Đồ ăn nhanh","https://res.cloudinary.com/duf1lmvzu/image/upload/v1732526597/tyathkobox215lxipfri.png"));
        categories.add(new Category(2,"Đồ uống","https://res.cloudinary.com/duf1lmvzu/image/upload/v1732526605/grfbr1alz1bowcg3hpui.png"));
        categories.add(new Category(3,"Món tráng miệng","https://res.cloudinary.com/duf1lmvzu/image/upload/v1732526622/obfdb0bxc2uaxmxvw3kr.png"));
        categories.add(new Category(4,"Món chính","https://res.cloudinary.com/duf1lmvzu/image/upload/v1732526654/xqadh6rcj6hwuzzbrshn.png"));
        categories.add(new Category(5,"Rau củ","https://res.cloudinary.com/duf1lmvzu/image/upload/v1732526634/gxfvy56wa7a3rlzpn9ub.png"));
        categories.add(new Category(6,"Hải sản","https://res.cloudinary.com/duf1lmvzu/image/upload/v1732526646/vezhsgh1dif55tclyele.png"));
        categories.add(new Category(7,"Healthy","https://res.cloudinary.com/duf1lmvzu/image/upload/v1732526664/qqa3qioelqwxcwdrcbyg.png"));
        for (Category category : categories){
            Map<String, Object> categoryData = new HashMap<>();
            categoryData.put("id", category.getId());
            categoryData.put("name",category.getName());
            categoryData.put("image",category.getImage());
            db.collection("category")
                    .document(String.valueOf(category.getId()))
                    .set(categoryData)
                    .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi thêm category", e));
        }






    }

}