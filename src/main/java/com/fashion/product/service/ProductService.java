package com.fashion.product.service;

import com.fashion.product.dto.ProductCreateRequest;
import com.fashion.product.dto.ProductResponse;
import com.fashion.product.dto.ProductUpdateRequest;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductCreateRequest request);

    List<ProductResponse> getAll();

    ProductResponse update(Long id, ProductUpdateRequest request);

    void delete(Long id);
}
