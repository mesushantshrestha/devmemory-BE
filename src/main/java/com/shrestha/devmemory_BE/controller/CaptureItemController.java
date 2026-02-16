package com.shrestha.devmemory_BE.controller;

import com.shrestha.devmemory_BE.dto.CreateCaptureItemRequest;
import com.shrestha.devmemory_BE.dto.UpdateCaptureItemRequest;
import com.shrestha.devmemory_BE.entity.CaptureItem;
import com.shrestha.devmemory_BE.service.CaptureItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CaptureItemController {
    private final CaptureItemService captureItemService;

    public CaptureItemController(CaptureItemService captureItemService) {
        this.captureItemService = captureItemService;
    }

    @GetMapping("/items")
    public ResponseEntity<?> getAllItems(){
        return ResponseEntity.ok(captureItemService.getAllItems());
    }

    @PostMapping("/items")
    public ResponseEntity<?> addItem(@Valid @RequestBody CreateCaptureItemRequest captureItemRequest) {
        CaptureItem captureItem = captureItemService.createCaptureItem(captureItemRequest);
        URI location = URI.create(String.format("/api/items/%s", captureItem.getId()));

        return ResponseEntity
                .created(location)
                .body(captureItem);
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<?> getItemById(@PathVariable UUID id){
        return ResponseEntity.ok(captureItemService.getItemById(id));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID id){
        captureItemService.deleteItem(id);
        return ResponseEntity.noContent().build(); //204
    }

    @PutMapping("items/{id}")
    public ResponseEntity<?> updateItem(@PathVariable UUID id, @RequestBody UpdateCaptureItemRequest captureItemRequest){
        CaptureItem updatedItem = captureItemService.updateItem(id, captureItemRequest);
        URI location = URI.create(String.format("/api/items/%s", updatedItem.getId()));
        return ResponseEntity
                .created(location)
                .body(updatedItem);
    }
}
