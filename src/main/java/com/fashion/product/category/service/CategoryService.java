package com.fashion.product.category.service;

import com.fashion.product.category.dto.CategoryDto;
import com.fashion.product.category.entity.Category;
import com.fashion.product.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public Category create(CategoryDto dto) {
        Category category = Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        return repository.save(category);
    }
}
