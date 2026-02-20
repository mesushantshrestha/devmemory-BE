package com.shrestha.devmemory_BE.repository;

import com.shrestha.devmemory_BE.entity.CaptureItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface CaptureItemRepository extends JpaRepository<CaptureItem, UUID> {
    List<CaptureItem> findAllByUser_Id(UUID userId);

    Optional<CaptureItem> findByIdAndUser_Id(UUID id, UUID userId);

    void deleteByIdAndUser_Id(UUID id, UUID userId);
}
