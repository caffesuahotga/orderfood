package com.example.orderfood.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.orderfood.models.FeedBack;
import com.example.orderfood.models.Order;
import com.example.orderfood.models.OrderDetail;
import com.example.orderfood.models.dto.CartDTO;
import com.example.orderfood.models.dto.OrderDTO;
import com.example.orderfood.models.dto.OrderProductDTO;
import com.example.orderfood.services.OrderActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class OrderUtil {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private HandleData handleData;

    public static Order CreateOrder(OrderDTO od) {
        try {
            return CompletableFuture.supplyAsync(() -> {
                try {

                    Order orderNew = HandleData.createOrder(od);

                    return orderNew;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).get();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Order> getAllOrdersByAccountId( Context context)
    {
        try {
            return CompletableFuture.supplyAsync(() -> {
                try {

                    CurrentUser.init(context);

                    ArrayList<Order> orderNew = HandleData.getAllOrdersByAccountId(CurrentUser.getId());

                    return orderNew;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).get();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Order> getAllOrdersNewForShipper()
    {
        try {
            return CompletableFuture.supplyAsync(() -> {
                try {

                    ArrayList<Integer> statusList = new ArrayList<>();
                    statusList.add(2);
                    ArrayList<Order> orderNew = HandleData.getAllOrderByStatus(statusList);

                    return orderNew;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).get();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Order> getAllOrdersNewForManager()
    {
        try {
            return CompletableFuture.supplyAsync(() -> {
                try {

                    ArrayList<Integer> statusList = new ArrayList<>();
                    statusList.add(1);
                    ArrayList<Order> orderNew = HandleData.getAllOrderByStatus(statusList);

                    return orderNew;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).get();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // lấy các lịch sử đơn hàng ( lấy all )
    public static ArrayList<Order> getAllListOrdersForManager()
    {
        try {
            return CompletableFuture.supplyAsync(() -> {
                try {
// 1 chờ xác nhận, 2 đã xác nhận, 3 đang giao, 4 đã giao-hoàn thành, 5 hủy
                    ArrayList<Integer> statusList = new ArrayList<>();
                    statusList.add(1);
                    statusList.add(2);
                    statusList.add(3);
                    statusList.add(4);
                    statusList.add(5);
                    ArrayList<Order> orderNew = HandleData.getAllOrderByStatus(statusList);

                    return orderNew;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).get();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Order> getAllHistoryOrderForShipper()
    {
        try {
            return CompletableFuture.supplyAsync(() -> {
                try {

                    ArrayList<Integer> statusList = new ArrayList<>();

                    statusList.add(3);
                    statusList.add(4);
                    statusList.add(5);

                    ArrayList<Order> orderNew = HandleData.getAllOrderByStatus(statusList);

                    return orderNew;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).get();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OrderDTO GetOrderInfo(int odId)
    {
        try {
            return CompletableFuture.supplyAsync(() -> {
                try {

                    OrderDTO data = HandleData.GetOrderInfo(odId);

                    return data;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).get();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean ChangeStatusOrder(int odId, int sta)
    {
        try {
            return CompletableFuture.supplyAsync(() -> {
                try {

                    boolean data = HandleData.ChangeStatusOrder(odId,sta);

                    return data;

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).get();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean Feedback(ArrayList<CartDTO> cartDTOList) {
        try {
            CompletableFuture.supplyAsync(() -> {
                try {
                    boolean data = HandleData.addFeedback(cartDTOList);
//                    List<OrderDetail> orderDetailList = new ArrayList<>();
//                    double rate = 0.0;
//                    int count = 0;
//                    List<FeedBack> feedBackList = new ArrayList<>();
//
//                    for (CartDTO cartDTO : cartDTOList) {
//                        orderDetailList = HandleData.getOrderDetailsByProductID(cartDTO.getProductID());
//                        for (OrderDetail orderDetail : orderDetailList) {
//                            feedBackList.add(HandleData.getFeedbackByOrderDetailID(orderDetail.getId()));
//                            rate = 0.0; // Reset rate for each new product
//                            count = 0;
//
//                            for (FeedBack feedBack : feedBackList) {
//                                rate += feedBack.getStar();
//                                count += 1;
//                            }
//
//                            // Tính giá trị rate trung bình
//                            double averageRate = (count > 0) ? rate / count : 0;
//
//                            // Gọi UpdateRateTask để cập nhật rate cho sản phẩm
//                            new UpdateRateTask().execute(cartDTO.getProductID(), averageRate);
//
//                            // Clear feedBackList for next iteration
//                            feedBackList.clear();
//                        }
                    //}

                    return data;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).thenAccept(result -> {
                if (result) {
                    Log.e("Feedback", "Successfully added feedback");
                } else {
                    Log.e("Feedback", "Failed to add feedback");
                }
            }).exceptionally(ex -> {
                Log.e("Feedback", "Error in processing feedback", ex);
                return null;
            });

            return true; // Indicating async task has been triggered
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class UpdateRateTask extends AsyncTask<Object, Void, Boolean> {
        private HandleData handleData;

        // Constructor nhận đối tượng HandleData
        public UpdateRateTask() {

        }

        @Override
        protected Boolean doInBackground(Object... params) {
            // Chuyển từ Object sang đúng kiểu
            int productID = (int) params[0];  // productID là kiểu int
            double rate = (double) params[1]; // rate là kiểu double

            try {
                // Sử dụng đối tượng handleData để gọi phương thức không tĩnh
                handleData.setRateProductByID(productID, rate);
                return true; // Trả về true nếu thành công
            } catch (ExecutionException | InterruptedException e) {
                return false; // Trả về false nếu có lỗi
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                Log.e("oke", "sdsdsd");
            } else {
                Log.e("ngu", "sdsdsd");
            }
        }
    }


}
