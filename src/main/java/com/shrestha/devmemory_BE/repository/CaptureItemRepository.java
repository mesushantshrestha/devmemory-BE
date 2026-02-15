package com.shrestha.devmemory_BE.repository;

import com.shrestha.devmemory_BE.entity.CaptureItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CaptureItemRepository extends JpaRepository<CaptureItem, UUID> {
}
