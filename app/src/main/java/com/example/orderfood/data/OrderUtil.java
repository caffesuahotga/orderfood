package com.example.orderfood.data;

import com.example.orderfood.models.Order;
import com.example.orderfood.models.dto.OrderDTO;
import com.google.firebase.firestore.FirebaseFirestore;

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
}
