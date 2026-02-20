package com.shrestha.devmemory_BE.service;

import com.shrestha.devmemory_BE.dto.CreateCaptureItemRequest;
import com.shrestha.devmemory_BE.dto.UpdateCaptureItemRequest;
import com.shrestha.devmemory_BE.entity.CaptureItem;
import com.shrestha.devmemory_BE.enums.Type;
import com.shrestha.devmemory_BE.exception.NotFoundException;
import com.shrestha.devmemory_BE.repository.CaptureItemRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class CaptureItemServiceImp implements CaptureItemService {

    private final CaptureItemRepository captureItemRepository;

    public CaptureItemServiceImp(CaptureItemRepository captureItemRepository) {
        this.captureItemRepository = captureItemRepository;
    }
    @Override
    public CaptureItem createCaptureItem(CreateCaptureItemRequest captureItemRequest) {
        CaptureItem captureItem = new CaptureItem();
        captureItem.setTitle(captureItemRequest.title());
        captureItem.setText(captureItemRequest.text());
        captureItem.setType(captureItemRequest.type());
        captureItem.setLanguage(captureItemRequest.language());
        captureItem.setDone(false);
        captureItem.setCreatedAt(Instant.now());
        return captureItemRepository.save(captureItem);
    }

    @Override
    public List<CaptureItem> getAllItems() {
        return captureItemRepository.findAll(
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
    }

    @Override
    public CaptureItem getItemById(UUID id) {

        return captureItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + id));
    }

    @Override
    public void deleteItem(UUID id) {
        if (!captureItemRepository.existsById(id)) {
            throw new NotFoundException("Item not found with id: " + id);
        }
        captureItemRepository.deleteById(id);
    }

    @Override
    public CaptureItem updateItem(UUID userId, UUID id, UpdateCaptureItemRequest captureItemRequest) {
         CaptureItem captureItem = captureItemRepository.findByIdAndUser_Id(userId,id)
                 .orElseThrow(() -> new NotFoundException("Item not found with id: " + id));
        // Only update body/text
        if (captureItemRequest.text() != null) {
            captureItem.setText(captureItemRequest.text());
        }
        // Only allow done update if TASK
        if (captureItemRequest.done() != null && captureItem.getType() == Type.TASK) {
            captureItem.setDone(captureItemRequest.done());
        }
          captureItem.setCreatedAt(Instant.now());
         return captureItemRepository.save(captureItem);
    }

}
