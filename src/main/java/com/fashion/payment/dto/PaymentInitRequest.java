package com.fashion.payment.dto;

public record PaymentInitRequest(
        Long orderId,
        String provider // "IYZICO" veya "STRIPE" (şimdilik string)
) {}
