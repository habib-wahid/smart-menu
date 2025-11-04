package org.example.menuapp.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.menuapp.dto.request.CategoryRequest;
import org.example.menuapp.dto.response.CategoryResponseDto;
import org.example.menuapp.entity.Category;
import org.example.menuapp.error.custom_exceptions.SmResourceNotFoundException;
import org.example.menuapp.error.messages.ExceptionMessages;
import org.example.menuapp.mapper.CategoryMapper;
import org.example.menuapp.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository,
                           CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional
    public Void createCategory(
            CategoryRequest categoryRequest) {
            Category category = categoryMapper.toCategory(categoryRequest);
            categoryRepository.save(category);
            return null;
    }

    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(categoryMapper::mapToCategoryResponseDto)
                .toList();
    }


    @Transactional
    public void updateCategory(Long categoryId, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new SmResourceNotFoundException(
                        String.format(ExceptionMessages.RESOURCE_NOT_FOUND, "Category", categoryId)));
        Optional.ofNullable(categoryRequest.name()).ifPresent(category::setName);
        Optional.ofNullable(categoryRequest.description()).ifPresent(category::setDescription);
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
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

    public CategoryResponseDto getSpecificCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        return categoryMapper.mapToCategoryResponseDto(category);
    }

    public String getBaseFilePath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
