package org.example.menuapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class ItemRequest {
    private Long itemId;
    private String itemName;
    private String itemDescription;
    private Double price;
    private Set<Long> categoryIds;
}
