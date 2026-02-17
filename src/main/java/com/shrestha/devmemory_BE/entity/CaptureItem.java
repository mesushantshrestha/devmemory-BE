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
    @GeneratedValue(strategy = GenerationType.UUID)
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
    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
    }
}
