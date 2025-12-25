package org.example.menuapp.repository;

import org.example.menuapp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select count(c) from Category c where c.id in :ids")
    long countCategories(@Param("ids") Set<Long> ids);
}
