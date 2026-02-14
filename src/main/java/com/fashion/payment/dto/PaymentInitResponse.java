package com.fashion.payment.dto;

import com.fashion.payment.entity.PaymentStatus;

public record PaymentInitResponse(
        Long paymentId,
        PaymentStatus status,
        String provider,
        String checkoutUrl
) {}
