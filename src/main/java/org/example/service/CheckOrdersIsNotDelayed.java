package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.model.Order;
import java.time.LocalDateTime;
import java.util.List;

public class CheckOrdersIsNotDelayed implements Runnable{

    private final ObjectMapper objectMapper;
    private final MongoDBService mongoDBService;
    private final RedisService redisService;

    public CheckOrdersIsNotDelayed() {
        this.mongoDBService = new MongoDBService();
        this.redisService = new RedisService("localhost", 6379);
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void run() {
        while (true) {
            checkOrders();
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    public void checkOrders(){
        List<Order> orders = mongoDBService.getAllOrders();
        for(Order order : orders) {
            if((order.getCompleteTime() == null) && (order.getStatus().equals("placed"))) {
                if(order.getOrderTime().isBefore(LocalDateTime.now().minusMinutes(10))){
                    mongoDBService.updateOrderStatus(order, "is_late");
                }
            }
        }
    }

}
