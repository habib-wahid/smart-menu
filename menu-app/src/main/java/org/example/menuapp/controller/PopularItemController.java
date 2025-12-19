package org.example.menuapp.controller;

import org.example.menuapp.dto.response.ApiResponse;
import org.example.menuapp.dto.response.PopularItemResponse;
import org.example.menuapp.service.PopularItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PopularItemController {

    //todo : {discount service, promotion service, popular item service}

    private final PopularItemService popularItemService;

    public PopularItemController(PopularItemService popularItemService) {
        this.popularItemService = popularItemService;
    }

    @GetMapping("/popular-item")
    public ApiResponse<List<PopularItemResponse>> getPopularItem() {
        return ApiResponse.success("Fetched Popular Items Successfully", popularItemService.getPopularItems());
    }
}
