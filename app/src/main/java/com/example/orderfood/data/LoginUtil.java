package com.example.orderfood.data;

import com.example.orderfood.models.Account;

import java.util.concurrent.ExecutionException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class LoginUtil {

    public static Account createAccount(String Name, String userName, String password) {
        try {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return HandleData.createAccount(Name, userName, password);
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).get();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean checkAccount(String userName) {
        try {
            return CompletableFuture.supplyAsync(() -> {
                
                try {
                    boolean ck = HandleData.checkAccount(userName);
                    return ck;

                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).get();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Account getAccountByUsername (String userName) {
        try {
            return CompletableFuture.supplyAsync(() -> {

                try {
                    Account ac = HandleData.getAccountByUsername(userName);
                    return ac;

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).get();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

