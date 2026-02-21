package com.shrestha.devmemory_BE.service;

import com.shrestha.devmemory_BE.dto.CaptureItemResponse;
import com.shrestha.devmemory_BE.dto.CreateCaptureItemRequest;
import com.shrestha.devmemory_BE.dto.UpdateCaptureItemRequest;
import com.shrestha.devmemory_BE.entity.CaptureItem;

import java.util.List;
import java.util.UUID;

public interface CaptureItemService {
    CaptureItemResponse createCaptureItem(CreateCaptureItemRequest captureItemRequest);
    List<CaptureItemResponse> getAllItems();
    CaptureItemResponse getItemById(UUID id);
    void deleteItem(UUID id);
    CaptureItemResponse updateItem(UUID id, UpdateCaptureItemRequest captureItemRequest);
}
