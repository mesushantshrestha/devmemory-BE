package com.shrestha.devmemory_BE.service;

import com.shrestha.devmemory_BE.dto.CreateCaptureItemRequest;
import com.shrestha.devmemory_BE.entity.CaptureItem;

import java.util.List;
import java.util.UUID;

public interface CaptureItemService {
    CaptureItem createCaptureItem(CreateCaptureItemRequest captureItemRequest);
    List<CaptureItem> getAllItems();
    CaptureItem getItemById(UUID id);
    void deleteItem(UUID id);
}
