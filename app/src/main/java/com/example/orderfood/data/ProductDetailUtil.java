package com.example.orderfood.data;

import com.example.orderfood.models.Account;
import com.example.orderfood.models.FeedBack;
import com.example.orderfood.models.Order;
import com.example.orderfood.models.OrderDetail;
import com.example.orderfood.models.Product;
import com.example.orderfood.models.dto.FeedBackDTO;
import com.example.orderfood.models.dto.ProductDetailDTO;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

///
public class ProductDetailUtil {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static ProductDetailDTO getProductById(int proID){
        try {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    // Truy vấn sản phẩm
                    Product product = HandleData.getProductById(proID);

                    // Tiếp tục truy vấn OrderDetail
                    List<OrderDetail> orderDetails = HandleData.getOrderDetailsByProductID(product.getId());

                    // Lấy feedback từ orderDetailId
                    List<Integer> orderDetailID = orderDetails.stream()
                            .map(orderDetail -> orderDetail.getId())
                            .collect(Collectors.toList());

                    List<FeedBack> feedBackS = HandleData.getAllFeedbacksByListOrderDetailID(orderDetailID);

                    // lấy các order
                    List<Integer> orderId = orderDetails.stream().map(orderDetail -> orderDetail.getOrderId()).collect(Collectors.toList());
                    List<Order> orderList = HandleData.getAllOrders(orderId);

                    // => lấy các account
                    List<Integer> accountId = orderList.stream().map(order -> order.getCustomerId()).collect(Collectors.toList());
                    List<Account> AccountList = HandleData.getAllAccounts(accountId);

                    //// hoàn thành công đoạn call data => tạo DTO
                    ProductDetailDTO data = new ProductDetailDTO();

                    data.setPID(proID);
                    data.setName(product.getName());
                    data.setDescription(product.getDescription());
                    data.setStar(product.getRate());
                    data.setMin(product.getMinutes());
                    data.setListImage(product.getImage());
                    data.setPrice(product.getPrice());

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

                        if (account != null) {
                            FeedBackDTO fbDTO = new FeedBackDTO();

                            fbDTO.setImageUser(account.getImage());
                            fbDTO.setNameUser(account.getName());
                            fbDTO.setContent(feedBack.getContent());
                            fbDTO.setStar(feedBack.getStar());

                            tmpFeadBackList.add(fbDTO);
                        }

                    }

                    data.setListFeedBack(tmpFeadBackList);
                    return data;

                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }).get(); // Chờ kết quả hoàn thành (tương tự await)

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e); // Bọc ngoại lệ thành RuntimeException
        }
    }
}
