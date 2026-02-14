package com.fashion.payment.entity;

import com.fashion.common.base.BaseEntity;
import com.fashion.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

// com.fashion.payment.entity.Payment
@Entity
@Table(name="payments")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    @Column(nullable=false)
    private Long orderId;

    @Column(nullable=false)
    private String conversationId;

    @Column(nullable=false)
    private String token;

    @Column(length = 10000)
    private String checkoutFormContent;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private PaymentStatus status; // INITIATED, SUCCESS, FAILED

    private String iyzicoPaymentId;
    private String iyzicoConversationId;
    private String errorMessage;
}
