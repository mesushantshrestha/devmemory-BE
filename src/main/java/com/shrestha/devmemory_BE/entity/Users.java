package com.shrestha.devmemory_BE.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="app_user")
@Data
public class Users {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;
    @Column(name = "google_sub", nullable = false, unique = true, length = 64)
    private String googleSub;
    @Column(nullable = false)
    private String email;
    private String name;

    @Column(name = "picture_url")
    private String picture_url;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }
}
