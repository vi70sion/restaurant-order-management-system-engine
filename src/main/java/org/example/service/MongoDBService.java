package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.model.Order;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MongoDBService {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private ObjectMapper objectMapper;

    public MongoDBService() {
        // Connect to MongoDB (default localhost:27017)
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("ordersDB");
        collection = database.getCollection("orders");
        objectMapper = new ObjectMapper();
    }

    private Order documentToOrder(Document doc) {
        Order order = new Order();
        order.setOrderId(doc.getObjectId("_id").toString());
        order.setClientName(doc.getString("clientName"));
        order.setTableNo(doc.getInteger("tableNo"));
        order.setDishes(doc.getList("dishes", String.class));
        order.setStatus(doc.getString("status"));

        Date orderTime = doc.getDate("orderTime");
        order.setOrderTime(orderTime.toInstant()
                .atZone( ZoneOffset.UTC)
                .toLocalDateTime());

        Date completeTime = doc.getDate("completeTime");
        if(completeTime != null){
            order.setCompleteTime(completeTime.toInstant()
                    .atZone( ZoneOffset.UTC)
                    .toLocalDateTime());
        } else order.setCompleteTime(null);

        return order;
    }

    private Document orderToDocument(Order order) {
        return new Document( "clientName", order.getClientName())
                .append("tableNo", order.getTableNo())
                .append("dishes", order.getDishes())
                .append("status", order.getStatus())
                .append("orderTime", order.getOrderTime())
                .append("completeTime", order.getCompleteTime())
                .append("amount", order.getAmount())
                .append("paymentMethod", order.getPaymentMethod());
    }

    private Document orderWithIDToDocument(Order order) {
        return new Document( "_id", order.getOrderId())
                .append("clientName", order.getClientName())
                .append("tableNo", order.getTableNo())
                .append("dishes", order.getDishes())
                .append("status", order.getStatus())
                .append("orderTime", order.getOrderTime())
                .append("completeTime", order.getCompleteTime())
                .append("amount", order.getAmount())
                .append("paymentMethod", order.getPaymentMethod());
    }

    public String addOrder(Order order) {
        Document doc = orderToDocument(order);
        collection.insertOne(doc);
        order.setOrderId(doc.getObjectId("_id").toString());  // set generated ID
        System.out.println("Added order: " + order);
        return order.getOrderId();
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                orders.add(documentToOrder(doc));
            }
        } finally {
            cursor.close();
        }
        return orders;
    }

    public Order getOrderById(String id) {
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? documentToOrder(doc) : null;
    }

    public Order getOrderByName(String clientName) {
        Document doc = collection.find(Filters.eq("clientName", clientName)).first();
        return doc != null ? documentToOrder(doc) : null;
    }

    public void updateOrder(String id, Order order) {
        Document updatedDoc = orderToDocument(order);
        collection.updateOne(Filters.eq("_id", new ObjectId(id)), new Document("$set", updatedDoc));
        System.out.println("Updated order with id: " + id);
    }

    public void updateOrderStatus(Order order, String status) {
        String id = order.getOrderId();
        order.setStatus(status);
        Document updatedDoc = orderWithIDToDocument(order);
        updatedDoc.remove("_id");
        collection.updateOne(Filters.eq("_id", new ObjectId(id)), new Document("$set", updatedDoc));
        System.out.println("Updated oder with id: " + order.getOrderId());
    }

    public void deleteOrder(String id) {
        collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
        System.out.println("Deleted order with id: " + id);
    }

    public void close() {
        mongoClient.close();
    }
}
