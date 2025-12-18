package org.example.menuapp.controller;

import org.example.menuapp.service.PopularItemService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PopularItemController {

    //todo : {discount service, promotion service, popular item service}

    private final PopularItemService popularItemService;

    public PopularItemController(PopularItemService popularItemService) {
        this.popularItemService = popularItemService;
    }

//    @GetMapping("/popular-item")
//    public ResponseEntity<List<PopularItemResponse>> getPopularItem() {
//        return ResponseEntity.ok(popularItemService.getPopularItem());
//    }
}
