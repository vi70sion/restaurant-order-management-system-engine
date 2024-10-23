package org.example;

import org.example.service.OrderService;
import org.example.service.RabbitMQService;

public class RestaurantSystemEngine {
    public static void main(String[] args) throws Exception {

        RabbitMQService rabbitMQService =new RabbitMQService();
        OrderService orderService = new OrderService();

        orderService.readQueue(rabbitMQService);


    }
}