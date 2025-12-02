package org.example.menuapp.repository;

import org.example.menuapp.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.Set;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Category Repository Test")
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        category1 = new Category(null, "Beverages", "Hot and cold drinks", LocalDateTime.now());
        category2 = new Category(null, "Desserts", "Sweet Items", LocalDateTime.now());
    }

    @Test
    @DisplayName("Should count categories By Ids")
    void testCountCategories() {
        Category savedCategory1 = categoryRepository.save(category1);
        Category savedCategory2 = categoryRepository.save(category2);

        Set<Long> ids = Set.of(savedCategory1.getId(), savedCategory2.getId());

        long count = categoryRepository.countCategories(ids);

        assertEquals(1, count);
    }


}
