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
import org.example.menuapp.mapper.ItemMapper;
import org.example.menuapp.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryService categoryService;
    private final FileStorageConfig fileStorageConfig;
    private final ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository, CategoryService categoryService,
                       FileStorageConfig fileStorageConfig, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.categoryService = categoryService;
        this.fileStorageConfig = fileStorageConfig;
        this.itemMapper = itemMapper;
    }

    public ItemResponse getItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new SmResourceNotFoundException(String.format(
                        ExceptionMessages.RESOURCE_NOT_FOUND, "Item", id)));

        return itemMapper.toItemResponse(item);
    }


    public List<ItemResponse> getItemsByCategory(Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        List<Item> itemsByCategory = itemRepository.getAllByCategory(Set.of(category));
        return itemsByCategory.stream()
                .map(itemMapper::toItemResponse)
                .toList();
    }

    @Transactional
    public void createItem(ItemRequest request, MultipartFile file) {
        Set<Category> categories = categoryService.getAllCategoriesByIds(request.categoryIds());
        if (categories.size() != request.categoryIds().size()) {
            throw new SmResourceNotFoundException(ExceptionMessages.SOME_CATEGORIES_ARE_NOT_FOUND);
        }
        String filePath = copyFile(file);
        String fileName = file != null ? file.getOriginalFilename() : null;
        Item item = itemMapper.toItem(request, fileName, filePath);
        item.setCategory(categories);
        itemRepository.save(item);
    }

    private String copyFile(MultipartFile file) {
        Path uploadPath = fileStorageConfig.getFilepath();
        Path targetPath = null;

        if (file != null && !file.isEmpty()) {
            try {
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String fileName = file.getOriginalFilename() + "_" + System.currentTimeMillis();
                targetPath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                log.info("Could not copy file: {}", e.getMessage());
                throw new SmFileStorageException(ExceptionMessages.FILE_NOT_ABLE_TO_SAVE);
            }
        }

        return targetPath != null ? targetPath.toString() : null;
    }

    public List<ItemResponse> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return items.stream()
                .map(itemMapper::toItemResponse)
                .toList();
    }


    public Set<Item> getAllItemsByIds(Set<Long> itemIds) {
        return new HashSet<>(itemRepository.findAllById(itemIds));
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(
                () -> new SmResourceNotFoundException(String.format(
                        ExceptionMessages.RESOURCE_NOT_FOUND, "Item", id)));
    }
}
