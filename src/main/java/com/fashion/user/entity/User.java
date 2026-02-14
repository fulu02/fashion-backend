package com.fashion.user.entity;

import com.fashion.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;  // HASH OLACAK

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
