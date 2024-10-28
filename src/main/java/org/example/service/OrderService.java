package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.example.model.Order;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class OrderService implements Runnable{

    private String queueName;
    private static final String HOST = "localhost";
    private final ConnectionFactory factory;
    private final ObjectMapper objectMapper;
    private MongoDBService mongoDBService;

    private RedisService redisService;

    public OrderService(String queueName) {
        this.factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        this.queueName = queueName;
        this.objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        this.mongoDBService = new MongoDBService();
        this.redisService = new RedisService("localhost", 6379);
    }

    @Override
    public void run() {
        continuousReceiveAndProcessOrders();
    }


    public void continuousReceiveAndProcessOrders() {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(queueName, false, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String jsonMessage = new String(delivery.getBody(), "UTF-8");
                System.out.println(" Gautas JSON: " + jsonMessage);

                try {
                    Order order = objectMapper.readValue(jsonMessage, Order.class);
                    if (order.getPaymentMethod() == null ) {
                        //new order received from RabbitMQ order queue
                        order.setOrderId(mongoDBService.addOrder(order));
                        redisService.putWithExpiration(order.getOrderId(), order, 300);
                    } else if (order.getPaymentMethod().equals("card payment") || order.getPaymentMethod().equals("in cash")) {
                        //received payment confirmation from RabbitMQ payment queue
                        Order orderFromDB = mongoDBService.getOrderById(order.getOrderId());
                        if (orderFromDB != null && orderFromDB.getStatus().equals("completed")) {
                            orderFromDB.setAmount(order.getAmount());
                            orderFromDB.setPaymentMethod(order.getPaymentMethod());
                            mongoDBService.updateOrderStatus(orderFromDB, "paid");
                        }
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});

            System.out.println("Laukiama. Eilė " + queueName);
            while (true) {
                Thread.sleep(1000);
            }
        } catch (IOException | TimeoutException | InterruptedException  e){
            System.out.println(e.getMessage());
        }
    }




}
