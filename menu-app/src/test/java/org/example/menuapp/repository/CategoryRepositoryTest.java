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
    @DisplayName("Should count categories By Ids - all categories")
    void testCountCategoriesWithAllIds() {
        Category savedCategory1 = categoryRepository.save(category1);
        Category savedCategory2 = categoryRepository.save(category2);

        Set<Long> ids = Set.of(savedCategory1.getId(), savedCategory2.getId());

        long count = categoryRepository.countCategories(ids);
        assertEquals(2, count);
    }

    @Test
    @DisplayName("Should count categories By Ids - some categories")
    void testCountCategoriesWithSomeIds() {
        Category savedCategory1 = categoryRepository.save(category1);
        Set<Long> ids = Set.of(savedCategory1.getId(), 100L);

        long count = categoryRepository.countCategories(ids);
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Should count categories By Ids - non existing categories")
    void testCountCategoriesWithNonExistingIds() {
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        Set<Long> ids = Set.of(100L, 200L);
        long count = categoryRepository.countCategories(ids);
        assertEquals(0, count);
    }

    @Test
    @DisplayName("Should count categories By Ids - empty categories")
    void testCountCategoriesWithEmptyIds() {
        long count = categoryRepository.countCategories(Set.of());
        assertEquals(0, count);
    }


}
