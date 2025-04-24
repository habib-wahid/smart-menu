package org.example.menuapp.service;

import lombok.extern.slf4j.Slf4j;
import org.example.menuapp.config.FileStorageConfig;
import org.example.menuapp.dto.request.ItemRequest;
import org.example.menuapp.dto.response.ItemResponse;
import org.example.menuapp.entity.Category;
import org.example.menuapp.entity.Item;
import org.example.menuapp.error.custom_exceptions.SmFileStorageException;
import org.example.menuapp.error.custom_exceptions.SmResourceNotFoundException;
import org.example.menuapp.error.messages.ExceptionMessages;
import org.example.menuapp.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryService categoryService;
    private final FileStorageConfig fileStorageConfig;

    public ItemService(ItemRepository itemRepository, CategoryService categoryService, FileStorageConfig fileStorageConfig) {
        this.itemRepository = itemRepository;
        this.categoryService = categoryService;
        this.fileStorageConfig = fileStorageConfig;
    }


    public void createItem(ItemRequest request, MultipartFile file) {
        Set<Category> categories = categoryService.getAllCategoriesById(request.getCategoryIds());
        Item item = new Item();
        item.setName(request.getItemName());
        item.setDescription(request.getItemDescription());
        item.setPrice(request.getPrice());
        item.setCategory(categories);
        Path uploadPath = fileStorageConfig.getFilepath();

        if (!file.isEmpty()) {
            try {
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path targetPath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                item.setFilePath(fileName);
            } catch (IOException e) {
                log.info("Could not copy file: {}", e.getMessage());
                throw new SmFileStorageException(ExceptionMessages.FILE_NOT_ABLE_TO_SAVE);
            }
        }

        itemRepository.save(item);
    }

    public List<ItemResponse> getAllItems() {
        List<Item> items = itemRepository.findAll();
        List<ItemResponse> itemResponses = items.stream()
                .map(this::mapToItemResponse)
                .toList();
        return itemResponses;
    }

    private ItemResponse mapToItemResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .filePath(item.getFilePath())
                .fullPathUrl(item.getFullPathUrl())
                .rating(item.getRating())
                .build();
    }

    public Set<Item> getAllItemsByIds(Set<Long> itemIds) {
        List<Item> items = itemRepository.findAllById(itemIds);
        Set<Long> ids = items.stream()
                .map(Item::getId)
                .collect(Collectors.toSet());
        itemIds.removeAll(ids);
        if (!itemIds.isEmpty()) {
            List<Long> itemListIds = new ArrayList<>(itemIds);
            throw new SmResourceNotFoundException(
                   String.format(ExceptionMessages.RESOURCE_NOT_FOUND, "Item", itemListIds.getFirst())
            );
        }
        return new HashSet<>(items);
    }
}
