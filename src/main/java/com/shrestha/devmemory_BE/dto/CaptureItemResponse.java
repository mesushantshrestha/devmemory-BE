package com.shrestha.devmemory_BE.dto;

import com.shrestha.devmemory_BE.enums.Type;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;


public record CaptureItemResponse(
        UUID id,
        String title,
        String text,
        Type type,
        String language,
        boolean done,
        Instant createdAt,
        UUID userId
) {

}
