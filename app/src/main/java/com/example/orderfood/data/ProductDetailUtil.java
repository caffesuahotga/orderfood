package com.example.orderfood.data;

import com.example.orderfood.models.Account;
import com.example.orderfood.models.FeedBack;
import com.example.orderfood.models.Order;
import com.example.orderfood.models.OrderDetail;
import com.example.orderfood.models.Product;
import com.example.orderfood.models.dto.FeedBackDTO;
import com.example.orderfood.models.dto.ProductDetailDTO;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

///
public class ProductDetailUtil {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static ProductDetailDTO getProductById(int proID) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<ProductDetailDTO> task = () -> {
            try {
                // Truy vấn sản phẩm
                Task<QuerySnapshot> productTask = db.collection("product")
                                                    .whereEqualTo("id", proID)
                                                    .get();

                // Chờ đợi kết quả trả về từ Firestore
                QuerySnapshot productSnapshot = Tasks.await(productTask);

                if (productSnapshot.isEmpty()) {
                    System.out.println("No product found with ID: " + proID);
                    return new ProductDetailDTO();
                }

                DocumentSnapshot productDocument = productSnapshot.getDocuments().get(0);
                Product product = productDocument.toObject(Product.class);

                // Tiếp tục truy vấn OrderDetail
                Task<QuerySnapshot> orderDetailTask = db.collection("orderDetail")
                        .whereEqualTo("productId", proID)
                        .get();

                // Chờ đợi kết quả trả về từ Firestore
                QuerySnapshot orderDetailSnapshot = Tasks.await(orderDetailTask);

                List<OrderDetail> orderDetails = new ArrayList<>();
                for (QueryDocumentSnapshot orderDoc : orderDetailSnapshot) {
                    OrderDetail orderDetail = orderDoc.toObject(OrderDetail.class);
                    orderDetails.add(orderDetail);
                }

                // Lấy feedback từ orderDetailId
                List<Integer> orderDetailID = orderDetails.stream()
                        .map(orderDetail -> orderDetail.getId())
                        .collect(Collectors.toList());

                Task<QuerySnapshot> feedbackTask = db.collection("feedback")
                        .whereIn("orderDetailId", orderDetailID)
                        .get();

                QuerySnapshot feedbackSnapshot = Tasks.await(feedbackTask);
                List<FeedBack> feedBackS = new ArrayList<>();

                for (QueryDocumentSnapshot feedbackDoc : feedbackSnapshot) {
                    FeedBack feedBack = feedbackDoc.toObject(FeedBack.class);
                    feedBackS.add(feedBack);
                }

                // lấy các order
                List<Integer> orderId = orderDetails.stream().map(orderDetail -> orderDetail.getOrderId()).collect(Collectors.toList());

                Task<QuerySnapshot> orderTask = db.collection("order")
                        .whereIn("id", orderId)
                        .get();

                QuerySnapshot orderSnapshot = Tasks.await(orderTask);
                List<Order> orderList = new ArrayList<>();

                for (QueryDocumentSnapshot orderDoc : orderSnapshot) {
                    Order or = orderDoc.toObject(Order.class);
                    orderList.add(or);
                }

                // => lấy các account

                List<Integer> accountId = orderList.stream().map(order -> order.getCustomerId()).collect(Collectors.toList());
                Task<QuerySnapshot> accountTask = db.collection("account")
                        .whereIn("id", accountId)
                        .get();

                QuerySnapshot accountSnapshot = Tasks.await(accountTask);
                List<Account> AccountList = new ArrayList<>();

                for (QueryDocumentSnapshot accountDoc : accountSnapshot) {
                    Account ac = accountDoc.toObject(Account.class);
                    AccountList.add(ac);
                }

                //// hoàn thành công đoạn call data => tạo DTO

                ProductDetailDTO data = new ProductDetailDTO();

                data.setPID(proID);
                data.setName(product.getName());
                data.setDescription(product.getDescription());
                data.setStar(product.getRate());
                data.setMin(product.getMinutes());
                data.setListImage(product.getImage());

                // tạo danh sách feedback của món ăn
                ArrayList<FeedBackDTO> tmpFeadBackList = new ArrayList<>();

                for (FeedBack feedBack : feedBackS) {

                    // 1. Lấy OrderDetail từ danh sách OrderDetails dựa trên feedBack
                    OrderDetail orderDetail = orderDetails.stream()
                            .filter(od -> od.getId() == feedBack.getOrderDetailId())  // Lọc OrderDetail dựa trên orderDetailId
                            .findFirst()
                            .orElse(null); // Nếu không tìm thấy, trả về null ngay

                    if (orderDetail == null) {
                        System.out.println("No OrderDetail found for FeedBack ID: " + feedBack.getId());
                    }

                    // 2. Lấy Order từ danh sách Orders dựa trên orderDetail.getOrderId()
                    Order order = orderList.stream()
                            .filter(o -> o.getId() == orderDetail.getOrderId())  // Lọc Order dựa trên orderId của OrderDetail
                            .findFirst()
                            .orElse(null); // Nếu không tìm thấy, trả về null ngay

                    if (order == null) {
                        System.out.println("No Order found for OrderDetail ID: " + orderDetail.getId());
                    }

                    // 3. Lấy Account từ danh sách AccountList dựa trên order.getAccountId()
                    Account account = AccountList.stream()
                            .filter(acc -> acc.getId() == order.getCustomerId())  // Lọc Account dựa trên accountId của Order
                            .findFirst()
                            .orElse(null); // Nếu không tìm thấy, trả về null ngay

                    if (account == null) {
                        FeedBackDTO fbDTO = new FeedBackDTO();

                        fbDTO.setImageUser("https://tamnhuhoa.com/datafiles/37/2023-08/31848625-com-tam-suon-bi-ba-chi.png");
                        fbDTO.setNameUser(account.getName());
                        fbDTO.setContent(feedBack.getContent());
                        fbDTO.setStar(feedBack.getStar());

                        tmpFeadBackList.add(fbDTO);
                    }

                }

                data.setListFeedBack(tmpFeadBackList);
                return data;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        };

        Future<ProductDetailDTO> future = executor.submit(task);

        try {
            return future.get(); // Chờ kết quả từ luồng nền (tương tự await)
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        } finally {
            executor.shutdown(); // Đảm bảo đóng Executor
        }
    }
}
