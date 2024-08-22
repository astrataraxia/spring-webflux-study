package com.sdragon.webfluxplayground.sec02.dto;

import java.time.Instant;

public record OrderDetails(
        String orderId,
        String customerName,
        String productName,
        Integer amount,
        Instant orderDate
) {
}
