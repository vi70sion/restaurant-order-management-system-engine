package org.example.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;  // good practice to use
    private String orderId;  // Unikalus užsakymo ID
    private String clientName;    // Kliento vardas arba identifikacija
    private int tableNo;     // Staliuko numeris
    private List<String> dishes;  // Užsakytų patiekalų sąrašas
    private String status;      // Užsakymo būsena ("placed", "completed", "is_late", "paid")
    private LocalDateTime orderTime;  // Laikas, kada užsakymas buvo pateiktas
    private LocalDateTime completeTime;
    private BigDecimal amount;
    private String paymentMethod;

    public Order() {
    }

    public Order(String orderId, String clientName, int tableNo, List<String> dishes, String status, LocalDateTime orderTime, LocalDateTime completeTime, BigDecimal amount, String paymentMethod) {
        this.orderId = orderId;
        this.clientName = clientName;
        this.tableNo = tableNo;
        this.dishes = dishes;
        this.status = status;
        this.orderTime = orderTime;
        this.completeTime = completeTime;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }

    public List<String> getDishes() {
        return dishes;
    }

    public void setDishes(List<String> dishes) {
        this.dishes = dishes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public LocalDateTime getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(LocalDateTime completeTime) {
        this.completeTime = completeTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
