package com.fashion.order.dto;

import com.fashion.order.entity.OrderStatus;
import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        Long orderId,
        OrderStatus status,
        BigDecimal totalAmount,
        List<OrderLine> items
) {
    public record OrderLine(
            Long productId,
            String productName,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal lineTotal
    ) {}
}
