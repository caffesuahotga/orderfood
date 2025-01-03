package com.example.orderfood.data;

import android.content.Context;
import android.util.Log;

import com.example.orderfood.models.Account;
import com.example.orderfood.models.Noti;
import com.example.orderfood.models.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class NotiUtil {
    public static void saveTokenFCMAccount(Context context) {
        CompletableFuture.runAsync(() -> {
            try {
                CurrentUser.init(context);
                HandleData.saveTokenFCMAccount(CurrentUser.getUsername());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).exceptionally(ex -> {
            Log.e("TAG", "Error saving token FCM account", ex);
            return null;
        });
    }

    public static void SendNotiToRole(int role, String title, String content, Date date, int orderId) {
        CompletableFuture.runAsync(() -> {
            try {
                // lấy danh sách acc ứng với role để gưởi token
                List<Account> accs = HandleData.getFCMTokenAccountsByRole(role);

                for (Account ac : accs) {
                    MyFirebaseMessagingService.sendNotificationToServer(ac.getFCMToken(), title, content);
                }

                // tạo noti
                for (Account ac : accs) {
                    HandleData.createNoti(title, content, date, orderId, ac.getId());
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).exceptionally(ex -> {
            Log.e("TAG", "Error saving token FCM account", ex);
            return null;
        });
    }

    public static void SendNotiToAccount(int role, String title, String content, Date date, int orderId, int accID) {
        CompletableFuture.runAsync(() -> {
            try {
                // lấy danh sách acc ứng với role để gưởi token
                Account acc = HandleData.getAccountId(accID);

                MyFirebaseMessagingService.sendNotificationToServer(acc.getFCMToken(), title, content);

                // tạo noti
                HandleData.createNoti(title, content, date, orderId, acc.getId());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).exceptionally(ex -> {
            Log.e("TAG", "Error saving token FCM account", ex);
            return null;
        });
    }

    public static ArrayList<Noti> GetNotiByAccId(int accID) {
        try {
            return CompletableFuture.supplyAsync(() -> {
                try {

                    ArrayList<Noti>  data =  HandleData.GetAllNotiByAccId(accID);
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
