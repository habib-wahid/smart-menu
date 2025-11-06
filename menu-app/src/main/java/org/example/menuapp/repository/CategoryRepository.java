package org.example.menuapp.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.example.menuapp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select count(c) from Category c where c.id in :ids")
    long countCategories(@Param("ids") Set<Long> ids);
}
