package org.example.menuapp.service;

import lombok.extern.slf4j.Slf4j;
import org.example.menuapp.config.FileStorageConfig;
import org.example.menuapp.dto.request.CategoryRequest;
import org.example.menuapp.dto.response.CategoryResponseDto;
import org.example.menuapp.entity.Category;
import org.example.menuapp.error.custom_exceptions.SmFileStorageException;
import org.example.menuapp.error.custom_exceptions.SmResourceNotFoundException;
import org.example.menuapp.error.messages.ExceptionMessages;
import org.example.menuapp.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final FileStorageConfig fileStorageConfig;

    public CategoryService(CategoryRepository categoryRepository, FileStorageConfig fileStorageConfig) {
        this.categoryRepository = categoryRepository;
        this.fileStorageConfig = fileStorageConfig;
    }

    public void createCategory(
            CategoryRequest categoryRequest,
            MultipartFile uploadedFile
    ) {
        Path uploadPath = fileStorageConfig.getFilepath();
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID() + "_" + uploadedFile.getOriginalFilename();
            Path targetPath = uploadPath.resolve(fileName);
            Files.copy(uploadedFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            Category category = Category.builder()
                    .name(categoryRequest.getName())
                    .description(categoryRequest.getDescription())
                    .imageUrl(fileName)
                    .build();

            categoryRepository.save(category);
        } catch (IOException e) {
            log.info("Could not copy file: {}", e.getMessage());
            throw new SmFileStorageException(ExceptionMessages.FILE_NOT_ABLE_TO_SAVE);
        }
    }

    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::mapToCategoryResponseDto)
                .toList();
    }


    private CategoryResponseDto mapToCategoryResponseDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .createdAt(category.getCreatedAt())
                .build();
    }

    public void updateCategory(CategoryRequest categoryRequest, MultipartFile file) {
        Category category = categoryRepository.findById(categoryRequest.getCategoryId())
                .orElseThrow(() -> new SmResourceNotFoundException(
                        String.format(ExceptionMessages.RESOURCE_NOT_FOUND, "Category", categoryRequest.getCategoryId())));

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        if (file != null && !file.isEmpty()) {
            Path storagePath = fileStorageConfig.getFilepath();
            if (category.getImageUrl() != null) {
                try {
                    Path oldPath = storagePath.resolve(category.getImageUrl());
                    Files.deleteIfExists(oldPath);
                } catch(IOException exception) {
                    log.info("Could not delete old image: {}", exception.getMessage());
                }
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path targetLocation = storagePath.resolve(fileName);
            try {
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            } catch(IOException exception) {
                log.info("Could not copy file: {}", exception.getMessage());
            }
            category.setImageUrl(fileName);
        }
        categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new SmResourceNotFoundException(
                                String.format(ExceptionMessages.RESOURCE_NOT_FOUND, "Category", id)));
        if (category.getImageUrl() != null) {
            try {
                Path filePath = fileStorageConfig.getFilepath().resolve(category.getImageUrl());
                Files.deleteIfExists(filePath);
            } catch(IOException exception) {
                log.info("Could not delete old image: {}", exception.getMessage());
            }
        }
        categoryRepository.deleteById(id);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new SmResourceNotFoundException(
                        String.format(ExceptionMessages.RESOURCE_NOT_FOUND, "Category", id)));
    }

    public Set<Category> getAllCategoriesById(Set<Long> categoryIds) {
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        Set<Long> itemIds = categories.stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        categoryIds.removeAll(itemIds);
        if (!categoryIds.isEmpty()) {
            throw new SmResourceNotFoundException(
                    String.format(ExceptionMessages.RESOURCE_NOT_FOUND, "Category", categoryIds));
        }
        return new HashSet<>(categories);
    }
}
