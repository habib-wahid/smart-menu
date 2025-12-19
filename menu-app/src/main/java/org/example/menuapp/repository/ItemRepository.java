package org.example.menuapp.repository;

import org.example.menuapp.dto.response.PopularItemPojo;
import org.example.menuapp.entity.Addon;
import org.example.menuapp.entity.Category;
import org.example.menuapp.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select distinct a from addon a join a.item i where i.id = :itemId")
    List<Addon> getAddOnsByItem(@Param("itemId") Long itemId);


  //  List<PopularItemResponse> getAllPopularItems(Pageable pageable);


    List<Item> getAllByCategory(Set<Category> category);


    @Query(value = """
               SELECT i.id as id, i.name as name, i.description as description,
                       i.item_price as price, i.file_path as filePath, i.file_name as fileName, i.item_rating as rating,
                       SUM(oi.total_item_price) as totalRevenue
                FROM item i
                INNER JOIN order_item oi ON i.id = oi.item_id
                INNER JOIN customer_order co ON oi.order_id = co.id
                WHERE co.order_status = :orderStatus
                AND co.order_time BETWEEN :fromDate AND :toDate
                GROUP BY i.id
                ORDER BY totalRevenue DESC
            """, nativeQuery = true)
    List<PopularItemPojo> getPopularItems(@Param("orderStatus") String orderStatus,
                                          @Param("fromDate") LocalDateTime fromDate,
                                          @Param("toDate") LocalDateTime toDate);

}
