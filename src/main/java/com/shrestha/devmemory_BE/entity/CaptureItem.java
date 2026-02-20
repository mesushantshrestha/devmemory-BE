package com.shrestha.devmemory_BE.entity;

import com.shrestha.devmemory_BE.enums.Type;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="capture_items")
@Data
public class CaptureItem {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String text;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;
    private String language;
    private boolean done;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = true) // make optional=true until you enforce NOT NULL
    @JoinColumn(name = "user_id")
    private Users user;
    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (createdAt == null) createdAt = Instant.now();
    }
}
