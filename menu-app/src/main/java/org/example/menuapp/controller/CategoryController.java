package org.example.menuapp.controller;

import org.example.menuapp.dto.request.CategoryRequest;
import org.example.menuapp.dto.response.CategoryResponseDto;
import org.example.menuapp.entity.Category;
import org.example.menuapp.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryResponseDto>> getCategory() {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
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
