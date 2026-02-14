package com.fashion.product.service;

import com.fashion.product.dto.ProductCreateRequest;
import com.fashion.product.dto.ProductResponse;
import com.fashion.product.dto.ProductUpdateRequest;
import com.fashion.product.entity.Product;
import com.fashion.product.mapper.ProductMapper;
import com.fashion.product.repository.ProductRepository;
import com.fashion.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    @Override
    public ProductResponse create(ProductCreateRequest request) {
        Product product = ProductMapper.toEntity(request);
        return ProductMapper.toResponse(productRepository.save(product));
    }


    @Override
    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toResponse)

                .toList();
    }

    @Override
    public ProductResponse update(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setActive(request.active());

        return ProductMapper.toResponse(productRepository.save(product));

    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }
}

