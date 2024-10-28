package org.example;

import org.example.service.CheckOrdersIsNotDelayed;
import org.example.service.OrderService;

public class RestaurantSystemEngine {
    public static void main(String[] args) throws Exception {

        OrderService orderService = new OrderService("orders_queue");
        Thread orderServiceThread = new Thread(orderService);
        orderServiceThread.start();

        OrderService paymentService = new OrderService("payment_queue");
        Thread paymentServiceThread = new Thread(paymentService);
        paymentServiceThread.start();

        CheckOrdersIsNotDelayed checkOrdersIsNotDelayed = new CheckOrdersIsNotDelayed();
        Thread checkOrdersIsNotDelayedThread = new Thread(checkOrdersIsNotDelayed);
        checkOrdersIsNotDelayedThread.start();

    }
}