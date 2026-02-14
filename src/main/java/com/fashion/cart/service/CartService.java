package com.fashion.cart.service;

import com.fashion.cart.dto.AddToCartRequest;
import com.fashion.cart.entity.CartItem;
import com.fashion.cart.repository.CartItemRepository;
import com.fashion.product.entity.Product;
import com.fashion.product.repository.ProductRepository;
import com.fashion.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public void add(User user, AddToCartRequest req) {
        if (req.quantity() == null || req.quantity() <= 0) {
            throw new RuntimeException("Quantity must be > 0");
        }

        Product product = productRepository.findById(req.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // stok kontrolü (sepete eklerken)
        if (product.getStock() < req.quantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        CartItem item = cartItemRepository.findByUserIdAndProductId(user.getId(), product.getId())
                .orElse(CartItem.builder().user(user).product(product).quantity(0).build());

        item.setQuantity(item.getQuantity() + req.quantity());
        cartItemRepository.save(item);
    }

    public List<CartItem> myCart(User user) {
        return cartItemRepository.findByUserId(user.getId());
    }

    public void remove(User user, Long productId) {
        cartItemRepository.findByUserIdAndProductId(user.getId(), productId)
                .ifPresent(cartItemRepository::delete);
    }

    public void clear(User user) {
        cartItemRepository.deleteByUserId(user.getId());
    }
}
