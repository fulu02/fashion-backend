package com.fashion.product.category.entity;

import com.fashion.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@Table(name = "categories")
@SuperBuilder
public class Category extends BaseEntity {

    protected Category() {
        super();
    }

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 2000)
    private String description;
}
