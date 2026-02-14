package com.fashion.payment.service;

import com.fashion.order.entity.Order;
import com.fashion.order.repository.OrderRepository;
import com.fashion.payment.dto.PaymentInitRequest;
import com.fashion.payment.dto.PaymentInitResponse;
import com.fashion.payment.entity.Payment;
import com.fashion.payment.entity.PaymentStatus;
import com.fashion.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public PaymentInitResponse init(PaymentInitRequest req) {

        Order order = orderRepository.findById(req.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // aynı order için tekrar init yapılmasını engelle
        paymentRepository.findByOrderId(order.getId()).ifPresent(p -> {
            throw new RuntimeException("Payment already initiated for this order");
        });

        // B1: gerçek provider entegrasyonu yok.
        // Şimdilik bir “mock checkout url” üretiyoruz.
        String mockCheckoutUrl = "https://checkout.mock/" + UUID.randomUUID();

        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalAmount())
                .status(PaymentStatus.INITIATED)
                .provider(req.provider() == null ? "IYZICO" : req.provider())
                .checkoutUrl(mockCheckoutUrl)
                .build();

        Payment saved = paymentRepository.save(payment);

        return new PaymentInitResponse(
                saved.getId(),
                saved.getStatus(),
                saved.getProvider(),
                saved.getCheckoutUrl()
        );
    }
}
