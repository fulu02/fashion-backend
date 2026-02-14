package com.fashion.product.category.controller;

import com.fashion.product.category.dto.CategoryDto;
import com.fashion.product.category.entity.Category;
import com.fashion.product.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class              CategoryController {

    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@RequestBody @Valid CategoryDto dto) {
        return service.create(dto);
    }
}
