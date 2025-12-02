package org.example.menuapp.service;

import org.example.menuapp.dto.response.PopularItemResponse;
import org.example.menuapp.repository.ItemRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PopularItemService {

    //todo : Discount and Promotion logic, Popular Item logic
    private final ItemRepository itemRepository;

    public PopularItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

//    @Cacheable(value = "popular-item", key = "'mostPopular'")
//    public List<PopularItemResponse> getPopularItem() {
//        PageRequest pageRequest = PageRequest.of(0, 20);
//        return itemRepository.getAllPopularItems(pageRequest);
//    }
}
