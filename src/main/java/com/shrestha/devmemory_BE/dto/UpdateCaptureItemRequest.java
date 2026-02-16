package com.shrestha.devmemory_BE.dto;

public record UpdateCaptureItemRequest(
        String text,
        Boolean done
) {
}
