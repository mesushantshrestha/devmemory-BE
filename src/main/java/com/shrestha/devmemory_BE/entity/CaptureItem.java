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

    private Type type;
    private String language;
    private boolean done;

    private Instant createdAt;
}
