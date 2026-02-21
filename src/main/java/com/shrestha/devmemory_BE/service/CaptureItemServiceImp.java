package com.shrestha.devmemory_BE.service;

import com.shrestha.devmemory_BE.dto.CaptureItemResponse;
import com.shrestha.devmemory_BE.dto.CreateCaptureItemRequest;
import com.shrestha.devmemory_BE.dto.UpdateCaptureItemRequest;
import com.shrestha.devmemory_BE.entity.CaptureItem;
import com.shrestha.devmemory_BE.entity.Users;
import com.shrestha.devmemory_BE.enums.Type;
import com.shrestha.devmemory_BE.exception.NotFoundException;
import com.shrestha.devmemory_BE.repository.CaptureItemRepository;
import com.shrestha.devmemory_BE.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class CaptureItemServiceImp implements CaptureItemService {
    //Temp
    @Value("${app.dev.single-user-id}")
    private UUID userId;

    private final CaptureItemRepository captureItemRepository;

    private final UserRepository userRepository;

    public CaptureItemServiceImp(CaptureItemRepository captureItemRepository, UserRepository userRepository) {
        this.captureItemRepository = captureItemRepository;
        this.userRepository = userRepository;
    }
    @Override
    public CaptureItemResponse createCaptureItem(CreateCaptureItemRequest captureItemRequest) {
        CaptureItem captureItem = new CaptureItem();

        //Temp
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("ItemNotFound"));
        captureItem.setUser(users);
        captureItem.setTitle(captureItemRequest.title());
        captureItem.setText(captureItemRequest.text());
        captureItem.setType(captureItemRequest.type());
        captureItem.setLanguage(captureItemRequest.language());
        captureItem.setDone(false);
        captureItem.setCreatedAt(Instant.now());
        return toResponse(captureItemRepository.save(captureItem));
    }

    @Override
    public List<CaptureItemResponse> getAllItems() {
        List<CaptureItem> captureItems = captureItemRepository.findAll(
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return captureItems.stream().map(this::toResponse).toList();
    }

    @Override
    public CaptureItemResponse getItemById(UUID id) {
        CaptureItem item = captureItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + id));
        return toResponse(item);
    }

    @Override
    public void deleteItem(UUID id) {
        if (!captureItemRepository.existsById(id)) {
            throw new NotFoundException("Item not found with id: " + id);
        }
        captureItemRepository.deleteById(id);
    }

    @Override
    public CaptureItemResponse updateItem(UUID id, UpdateCaptureItemRequest captureItemRequest) {
         CaptureItem captureItem = captureItemRepository.findById(id)
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
         return toResponse(captureItemRepository.save(captureItem));
    }

    private CaptureItemResponse toResponse(CaptureItem item) {
        UUID userId = (item.getUser() != null) ? item.getUser().getId() : null;

        return new CaptureItemResponse(
                item.getId(),
                item.getTitle(),
                item.getText(),
                item.getType(),
                item.getLanguage(),
                item.isDone(),
                item.getCreatedAt(),
                userId
        );
    }
}
