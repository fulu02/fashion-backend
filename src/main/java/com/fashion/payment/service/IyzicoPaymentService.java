package com.fashion.payment.service;

import com.fashion.order.entity.Order;
import com.fashion.order.entity.OrderItem;
import com.fashion.order.repository.OrderRepository;
import com.fashion.payment.entity.Payment;
import com.fashion.payment.entity.PaymentStatus;
import com.fashion.payment.repository.PaymentRepository;
import com.fashion.user.entity.User;
import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.model.Locale;
import com.iyzipay.request.CreateCheckoutFormInitializeRequest;
import com.iyzipay.request.RetrieveCheckoutFormRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IyzicoPaymentService {

    private final Options options;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Value("${app.iyzico.callbackUrl}")
    private String callbackUrl;

    public CheckoutStartResponse start(Long orderId, String clientIp) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // aynı order için ödeme tekrar başlatılmasın
        paymentRepository.findByOrderId(orderId).ifPresent(p -> {
            throw new RuntimeException("Payment already initiated for this order");
        });

        String conversationId = UUID.randomUUID().toString();

        CreateCheckoutFormInitializeRequest req = new CreateCheckoutFormInitializeRequest();
        req.setLocale(Locale.TR.getValue());
        req.setConversationId(conversationId);

        BigDecimal total = order.getTotalAmount();
        req.setPrice(total);      // iyzico SDK BigDecimal kabul ediyor
        req.setPaidPrice(total);

        req.setCurrency(Currency.TRY.name());
        req.setBasketId("ORDER-" + order.getId());
        req.setPaymentGroup(PaymentGroup.PRODUCT.name());
        req.setCallbackUrl(callbackUrl);

        // Buyer + Address
        User user = order.getUser();
        req.setBuyer(buildBuyer(user, clientIp));
        req.setShippingAddress(buildShippingAddress(user));
        req.setBillingAddress(buildBillingAddress(user));

        // Basket items
        req.setBasketItems(buildBasketItems(order.getItems()));

        CheckoutFormInitialize init = CheckoutFormInitialize.create(req, options);

        if (!"success".equalsIgnoreCase(init.getStatus())) {
            throw new RuntimeException("Iyzico init failed: " + init.getErrorMessage());
        }

        Payment payment = Payment.builder()
                .order(order)
                .amount(total)
                .status(PaymentStatus.INITIATED)
                .provider("IYZICO")
                .token(init.getToken())
                .checkoutFormContent(init.getCheckoutFormContent())
                .build();

        Payment saved = paymentRepository.save(payment);

        return new CheckoutStartResponse(
                saved.getId(),
                init.getToken(),
                init.getCheckoutFormContent()
        );
    }

    public CheckoutResultResponse retrieve(String token) {
        RetrieveCheckoutFormRequest req = new RetrieveCheckoutFormRequest();
        req.setLocale(Locale.TR.getValue());
        req.setToken(token);

        CheckoutForm form = CheckoutForm.retrieve(req, options);

        return new CheckoutResultResponse(
                form.getStatus(),
                form.getPaymentStatus(),
                form.getErrorMessage()
        );
    }

    private Buyer buildBuyer(User user, String clientIp) {
        Buyer buyer = new Buyer();

        // ZORUNLU alanlar: id, name, surname, gsmNumber, email, identityNumber, registrationAddress, ip, city, country
        buyer.setId(String.valueOf(user.getId()));
        buyer.setName(getOrDefault(user.getFirstName(), "Fatma"));     // User’da yoksa placeholder
        buyer.setSurname(getOrDefault(user.getLastName(), "Ulu"));
        buyer.setGsmNumber(getOrDefault(user.getPhone(), "+905555555555"));
        buyer.setEmail(getOrDefault(user.getEmail(), "test@test.com"));

        // Sandbox için örnek TC/identity (iyzico test verileri kullanılır)
        buyer.setIdentityNumber("11111111111");

        buyer.setRegistrationAddress("Test Mah. Test Sok. No:1");
        buyer.setIp(getOrDefault(clientIp, "85.34.78.112"));
        buyer.setCity("Istanbul");
        buyer.setCountry("Turkey");
        buyer.setZipCode("34000");

        // opsiyonel tarih alanları
        buyer.setRegistrationDate("2026-01-01 12:00:00");
        buyer.setLastLoginDate("2026-01-01 12:00:00");

        return buyer;
    }

    private Address buildShippingAddress(User user) {
        Address a = new Address();
        a.setContactName(fullName(user));
        a.setCity("Istanbul");
        a.setCountry("Turkey");
        a.setZipCode("34000");
        a.setAddress("Test Mah. Test Sok. No:1 (Shipping)");
        return a;
    }

    private Address buildBillingAddress(User user) {
        Address a = new Address();
        a.setContactName(fullName(user));
        a.setCity("Istanbul");
        a.setCountry("Turkey");
        a.setZipCode("34000");
        a.setAddress("Test Mah. Test Sok. No:1 (Billing)");
        return a;
    }

    private List<BasketItem> buildBasketItems(List<OrderItem> items) {
        return items.stream().map(oi -> {
            BasketItem bi = new BasketItem();
            bi.setId(String.valueOf(oi.getProduct().getId()));
            bi.setName(oi.getProduct().getName());

            // Category opsiyonel: varsa koy
            String categoryName = (oi.getProduct().getCategory() != null)
                    ? oi.getProduct().getCategory().getName()
                    : "Fashion";

            bi.setCategory1(categoryName);
            bi.setItemType(BasketItemType.PHYSICAL.name());

            // lineTotal veya unitPrice*qty (ikisi de olur) -> iyzico itemPrice: satır toplamı daha doğru
            bi.setPrice(oi.getLineTotal());

            return bi;
        }).collect(Collectors.toList());
    }

    private String fullName(User user) {
        String fn = getOrDefault(user.getFirstName(), "Fatma");
        String ln = getOrDefault(user.getLastName(), "Ulu");
        return fn + " " + ln;
    }

    private String getOrDefault(String v, String def) {
        return (v == null || v.isBlank()) ? def : v;
    }

    // DTO’lar:
    public record CheckoutStartResponse(Long paymentId, String token, String checkoutFormContent) {}
    public record CheckoutResultResponse(String status, String paymentStatus, String errorMessage) {}
}
