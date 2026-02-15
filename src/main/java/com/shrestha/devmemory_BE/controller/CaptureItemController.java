package com.shrestha.devmemory_BE.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CaptureItemController {
    @GetMapping("/items")
    public ResponseEntity<?> getAllItems(){
        return ResponseEntity.ok("All items");
    }
}
