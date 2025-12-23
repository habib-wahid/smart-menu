package org.example.menuapp.service;

import lombok.RequiredArgsConstructor;
import org.example.menuapp.dto.response.PopularItemPojo;
import org.example.menuapp.dto.response.PopularItemResponse;
import org.example.menuapp.enums.OrderStatus;
import org.example.menuapp.repository.ItemRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PopularItemService {

    //todo : Discount and Promotion logic, Popular Item logic
    private final ItemRepository itemRepository;


    @Cacheable("popularItems")
    public List<PopularItemResponse> getPopularItems() {
        List<PopularItemPojo> popularItems = itemRepository.getPopularItems(
                OrderStatus.PLACED.getStatus(), LocalDateTime.now().minusDays(60), LocalDateTime.now().plusDays(1));
        return popularItems.stream()
                .map(popularItemPojo -> new PopularItemResponse(
                        popularItemPojo.getId(),
                        popularItemPojo.getName(),
                        popularItemPojo.getDescription(),
                        popularItemPojo.getPrice(),
                        popularItemPojo.getFilePath(),
                        popularItemPojo.getFileName(),
                        popularItemPojo.getRating(),
                        popularItemPojo.getTotalRevenue()
                ))
                .toList();
    }

}
