package com.shrestha.devmemory_BE.repository;

import com.shrestha.devmemory_BE.entity.CaptureItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CaptureItemRepository extends JpaRepository<CaptureItem, UUID> {
}
