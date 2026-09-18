package com.RoomMateX.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Value("${razorpay.key}")
    private String key;

    @Value("${razorpay.secret}")
    private String secret;

    public Order createOrder(Integer amount) throws Exception {

        RazorpayClient client = new RazorpayClient(key, secret);

        JSONObject obj = new JSONObject();
        obj.put("amount", amount * 100); // paise
        obj.put("currency", "INR");
        obj.put("receipt", "txn_123");

        return client.orders.create(obj);
    }
    public void refund(String paymentId) throws Exception {

        RazorpayClient client = new RazorpayClient(key, secret);
        client.payments.refund(paymentId);
    }

}
