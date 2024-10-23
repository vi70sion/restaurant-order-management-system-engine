package org.example.service;

public class OrderService {

    public OrderService() {
    }

    public void readQueue (RabbitMQService rabbitMQService) throws Exception {
        rabbitMQService.continuousReceiveAndProcess();
    }


}
