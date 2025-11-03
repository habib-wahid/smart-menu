package org.example.menuapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.menuapp.dto.request.CategoryRequest;
import org.example.menuapp.dto.response.ApiResponse;
import org.example.menuapp.dto.response.CategoryResponseDto;
import org.example.menuapp.entity.Category;
import org.example.menuapp.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService categoryService;
    private int count = 0;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(path = "/category", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse getCategory(HttpServletRequest request) {
        String tracerId = request.getHeader("X-Tracer-Id");
        String version = request.getHeader("Version");
        System.out.println("Tracer Id: " + tracerId + " Version: " + version);
        List<CategoryResponseDto> categoryResponse = categoryService.getAllCategories();
        return ApiResponse.success("Fetched Successfully", "trace-id", categoryResponse);
    }


    @GetMapping(path = "/v3/category", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse getCategoryWithApiVersion(HttpServletRequest request) {
        String tracerId = request.getHeader("X-Tracer-Id");
        String version = request.getHeader("Version");
        System.out.println("Tracer Id: " + tracerId + " Version: " + version);
        Category categoryResponse = categoryService.getCategoryById(2L);
        return ApiResponse.success("Fetched Successfully", "trace-id", categoryResponse);
    }

    @GetMapping(path = "/v3/category/retry", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse getCategoryWithRetry(HttpServletRequest request) {
        count++;
        if (count <= 2) {
            System.out.println("Count " + count);
            throw new RuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
        String tracerId = request.getHeader("X-Tracer-Id");
        String version = request.getHeader("Version");
        System.out.println("Tracer Id: " + tracerId + " Version: " + version);
        Category categoryResponse = categoryService.getCategoryById(2L);
        return ApiResponse.success("Fetched Successfully", "trace-id", categoryResponse);
    }


    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public void createCategory(
            @RequestBody CategoryRequest categoryRequest) {
        categoryService.createCategory(categoryRequest);
    }

    @PutMapping(path = "/{categoryId}")
    public ResponseEntity<Void> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody CategoryRequest categoryRequest
    ) {
        categoryService.updateCategory(categoryId, categoryRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
