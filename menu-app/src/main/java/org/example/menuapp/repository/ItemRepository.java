package org.example.menuapp.repository;

import org.example.menuapp.entity.Addon;
import org.example.menuapp.entity.Category;
import org.example.menuapp.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select distinct a from addon a join a.item i where i.id = :itemId")
    List<Addon> getAddOnsByItem(@Param("itemId") Long itemId);


  //  List<PopularItemResponse> getAllPopularItems(Pageable pageable);


    List<Item> getAllByCategory(Set<Category> category);

}
