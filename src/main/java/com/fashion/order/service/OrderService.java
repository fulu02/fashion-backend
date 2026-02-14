package com.fashion.order.service;

import com.fashion.cart.repository.CartItemRepository;
import com.fashion.order.dto.OrderResponse;
import com.fashion.order.entity.*;
import com.fashion.order.repository.OrderRepository;
import com.fashion.product.entity.Product;
import com.fashion.product.repository.ProductRepository;
import com.fashion.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderResponse checkout(User user) {
        var cartItems = cartItemRepository.findByUserId(user.getId());
        if (cartItems.isEmpty()) throw new RuntimeException("Cart is empty");

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.CREATED)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (var ci : cartItems) {
            Product p = productRepository.findById(ci.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // kesin stok kontrolü (checkout'ta)
            if (p.getStock() < ci.getQuantity()) {
                throw new RuntimeException("Insufficient stock for: " + p.getName());
            }

            BigDecimal unit = p.getPrice();
            BigDecimal line = unit.multiply(BigDecimal.valueOf(ci.getQuantity()));

            OrderItem oi = OrderItem.builder()
                    .order(order)
                    .product(p)
                    .quantity(ci.getQuantity())
                    .unitPrice(unit)
                    .lineTotal(line)
                    .build();

            order.getItems().add(oi);
            total = total.add(line);
        }

        order.setTotalAmount(total);

        // ödeme sonrası düşürmek daha güvenli ama MVP için burada düşebiliriz:
        // ŞİMDİLİK stok düşürmeyi "PAID" aşamasına bırakacağız (B aşamasında).
        Order saved = orderRepository.save(order);

        // sepeti temizle
        cartItemRepository.deleteByUserId(user.getId());

        return new OrderResponse(
                saved.getId(),
                saved.getStatus(),
                saved.getTotalAmount(),
                saved.getItems().stream().map(i ->
                        new OrderResponse.OrderLine(
                                i.getProduct().getId(),
                                i.getProduct().getName(),
                                i.getQuantity(),
                                i.getUnitPrice(),
                                i.getLineTotal()
                        )
                ).collect(Collectors.toList())
        );
    }
}
