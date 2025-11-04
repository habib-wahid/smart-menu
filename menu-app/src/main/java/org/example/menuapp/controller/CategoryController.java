package org.example.menuapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.menuapp.dto.request.CategoryRequest;
import org.example.menuapp.dto.response.ApiResponse;
import org.example.menuapp.dto.response.CategoryResponseDto;
import org.example.menuapp.entity.Category;
import org.example.menuapp.service.CategoryService;
import org.springframework.http.MediaType;
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
    public ApiResponse<List<CategoryResponseDto>> getCategory(HttpServletRequest request) {
        List<CategoryResponseDto> categoryResponse = categoryService.getAllCategories();
        return ApiResponse.success("Fetched Successfully", categoryResponse);
    }


//    @GetMapping(path = "/v3/category", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ApiResponse getCategoryWithApiVersion(HttpServletRequest request) {
//        String tracerId = request.getHeader("X-Tracer-Id");
//        String version = request.getHeader("Version");
//        System.out.println("Tracer Id: " + tracerId + " Version: " + version);
//        Category categoryResponse = categoryService.getCategoryById(2L);
//        return ApiResponse.success("Fetched Successfully", "trace-id", categoryResponse);
//    }
//
//    @GetMapping(path = "/v3/category/retry", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ApiResponse getCategoryWithRetry(HttpServletRequest request) {
//        count++;
//        if (count <= 2) {
//            System.out.println("Count " + count);
//            throw new RuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//        }
//        String tracerId = request.getHeader("X-Tracer-Id");
//        String version = request.getHeader("Version");
//        System.out.println("Tracer Id: " + tracerId + " Version: " + version);
//        Category categoryResponse = categoryService.getCategoryById(2L);
//        return ApiResponse.success("Fetched Successfully", "trace-id", categoryResponse);
//    }


    @GetMapping("/{categoryId}")
    public ApiResponse<Category> getCategoryById(@PathVariable Long categoryId) {
       return ApiResponse.success(categoryService.getCategoryById(categoryId));
    }

    @PostMapping()
    public ApiResponse<Void> createCategory(
            @RequestBody CategoryRequest categoryRequest) {
        return ApiResponse.success("Category Created Successfully", categoryService.createCategory(categoryRequest));
    }

    @PutMapping(path = "/{categoryId}")
    public ApiResponse<Void> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody CategoryRequest categoryRequest
    ) {
        categoryService.updateCategory(categoryId, categoryRequest);
        return ApiResponse.success("Category Updated Successfully", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.success("Category Deleted Successfully", null);
    }

}
