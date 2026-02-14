package com.fashion.order.controller;

import com.fashion.order.dto.OrderResponse;
import com.fashion.order.service.OrderService;
import com.fashion.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public OrderResponse checkout(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return orderService.checkout(user);
    }
}
