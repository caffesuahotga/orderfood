package com.example.orderfood.data;

import android.content.Context;

import com.example.orderfood.models.Order;
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

    public static boolean Feedback( ArrayList<CartDTO> cartDTOList)
    {
        try {
            return CompletableFuture.supplyAsync(() -> {
                try {

                    boolean data = HandleData.addFeedback(cartDTOList);

                    return data;

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).get();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
