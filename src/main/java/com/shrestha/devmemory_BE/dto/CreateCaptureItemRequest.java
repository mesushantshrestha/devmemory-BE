package com.shrestha.devmemory_BE.dto;

import com.shrestha.devmemory_BE.enums.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateCaptureItemRequest(
        UUID userId,
        String title,
        @NotBlank String text,
        @NotNull Type type,
        String language)
{}
