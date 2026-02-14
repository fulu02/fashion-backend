package com.fashion.cart.controller;

import com.fashion.cart.dto.AddToCartRequest;
import com.fashion.cart.service.CartService;
import com.fashion.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public void add(Authentication auth, @RequestBody AddToCartRequest req) {
        User user = (User) auth.getPrincipal();
        cartService.add(user, req);
    }

    @DeleteMapping("/remove/{productId}")
    public void remove(Authentication auth, @PathVariable Long productId) {
        User user = (User) auth.getPrincipal();
        cartService.remove(user, productId);
    }

    @DeleteMapping("/clear")
    public void clear(Authentication auth) {
        User user = (User) auth.getPrincipal();
        cartService.clear(user);
    }
}
