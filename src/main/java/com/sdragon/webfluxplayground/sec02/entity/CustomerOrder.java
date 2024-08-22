package com.sdragon.webfluxplayground.sec02.entity;

import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

public class CustomerOrder {

    @Id
    private UUID orderId;
    private Integer customerId;
    private Integer productId;
    private Integer amount;
    private Instant orderDate;

    public UUID orderId() {
        return orderId;
    }

    public Integer customerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer productId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer amount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Instant orderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "CustomerOrder{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", productId=" + productId +
                ", amount=" + amount +
                ", orderDate=" + orderDate +
                '}';
    }
}

