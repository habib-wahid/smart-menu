package org.example.menuapp.controller;

import org.example.menuapp.dto.CategoryRequest;
import org.example.menuapp.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createCategory(
            @RequestPart("category") CategoryRequest categoryRequest,
            @RequestPart("file") MultipartFile file) {
        categoryService.createCategory(categoryRequest, file);
    }

}
