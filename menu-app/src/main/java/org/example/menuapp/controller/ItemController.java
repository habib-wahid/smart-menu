package org.example.menuapp.controller;

import jakarta.validation.Valid;
import org.example.menuapp.dto.request.ItemPriceUpdateRequest;
import org.example.menuapp.dto.request.ItemRequest;
import org.example.menuapp.dto.response.ApiResponse;
import org.example.menuapp.dto.response.ItemResponse;
import org.example.menuapp.service.ItemService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public ApiResponse<ItemResponse> getItem(@PathVariable Long id) {
        return ApiResponse.success("Successfully Fetched the Item.", itemService.getItem(id));
    }

    @GetMapping
    public ApiResponse<List<ItemResponse>> getItems() {
        return ApiResponse.success(itemService.getAllItems());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> createItem(
            @RequestPart(name = "item") @Valid ItemRequest request,
            @RequestPart(name = "file", required = false) MultipartFile file) {
        itemService.createItem(request, file);
        return ApiResponse.success("Item Created Successfully");
    }

    @PatchMapping("/{id}/price")
    public ApiResponse<String> updateItemPrice(@PathVariable Long id,
                                               @RequestBody ItemPriceUpdateRequest itemPriceUpdateRequest) {
        itemService.updateItemPrice(id, itemPriceUpdateRequest);
        return ApiResponse.success("Item Price Updated Successfully");
    }

    @PatchMapping("/{id}/availability")
    public ApiResponse<String> updateItemAvailability(@PathVariable Long id,
                                                      @RequestParam(value = "availability") boolean availability) {
        itemService.updateItemAvailability(id, availability);
        return ApiResponse.success("Item Availability Updated Successfully");
    }

    @GetMapping("/by-category/{categoryId}")
    public ApiResponse<List<ItemResponse>> getItemsByCategory(@PathVariable Long categoryId) {
        return ApiResponse.success(itemService.getItemsByCategory(categoryId));
    }
}
