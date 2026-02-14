package com.fashion.payment.entity;

public enum PaymentStatus {
    INITIATED,   // ödeme başlatıldı
    SUCCESS,     // ödeme başarılı (B2 callback’te)
    FAILED       // ödeme başarısız (B2 callback’te)
}
