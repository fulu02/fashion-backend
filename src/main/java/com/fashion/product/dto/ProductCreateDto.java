package com.fashion.product.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductCreateDto {

    @NotBlank
    private String name;

    @NotBlank
    private String slug;

    @Size(max = 2000)
    private String description;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    @NotNull
    @Min(0)
    private Integer stock;

    @NotNull
    private Boolean active;

    @NotNull
    private Long categoryId;
}
