package org.example.menuapp.repository;

import org.example.menuapp.dto.response.PopularItemResponse;
import org.example.menuapp.entity.AddOn;
import org.example.menuapp.entity.Category;
import org.example.menuapp.entity.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select distinct a from addon a join a.item i where i.id = :itemId")
    List<AddOn> getAddOnsByItem(@Param("itemId") Long itemId);

    @Query(
            "select new org.example.menuapp.dto.response.PopularItemResponse(i.id, i.name, i.description, i.price, i.filePath, i.rating, COUNT(oi.item.id)) " +
            "from order_item oi join Item i on oi.item.id = i.id " +
            "group by i.id " +
            "order by count(oi.item.id) desc")
    List<PopularItemResponse> getAllPopularItems(Pageable pageable);


    List<Item> getAllByCategory(Set<Category> category);

}
