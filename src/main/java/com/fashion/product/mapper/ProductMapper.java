package com.fashion.product.mapper;

import com.fashion.product.dto.ProductCreateRequest;
import com.fashion.product.dto.ProductResponse;
import com.fashion.product.entity.Product;

public class ProductMapper {

    private ProductMapper() {
        // utility class
    }

    public static Product toEntity(ProductCreateRequest request) {
        return Product.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .active(request.getActive())
                .build();
    }

    public static ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .active(product.getActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
