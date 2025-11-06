package org.example.menuapp.controller;

import org.example.menuapp.dto.response.PopularItemResponse;
import org.example.menuapp.service.PopularItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PopularItemController {

    private final PopularItemService popularItemService;

    public PopularItemController(PopularItemService popularItemService) {
        this.popularItemService = popularItemService;
    }

//    @GetMapping("/popular-item")
//    public ResponseEntity<List<PopularItemResponse>> getPopularItem() {
//        return ResponseEntity.ok(popularItemService.getPopularItem());
//    }
}
