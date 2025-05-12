package org.example.menuapp.controller;

import org.example.menuapp.dto.request.ItemRequest;
import org.example.menuapp.dto.response.ItemResponse;
import org.example.menuapp.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<ItemResponse> getItem(@PathVariable Long id) {
        ItemResponse itemResponse = itemService.getItem(id);
        return ResponseEntity.status(HttpStatus.OK).body(itemResponse);
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createItem(
            @RequestPart(name = "item") ItemRequest request,
            @RequestPart(name = "file") MultipartFile file) {
        itemService.createItem(request, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
