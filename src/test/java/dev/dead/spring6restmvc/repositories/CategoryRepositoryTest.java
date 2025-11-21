package dev.dead.spring6restmvc.repositories;

import dev.dead.spring6restmvc.entities.Beer;
import dev.dead.spring6restmvc.entities.Category;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class CategoryRepositoryTest {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BeerRepository beerRepository;

    Beer testBeer;

    @BeforeEach
    void setUp() {
        testBeer = beerRepository.findAll()
                .get(0);
    }

    @AfterEach
    void tearDown() {
    }

    @Transactional
    @Test
    void addCategoryTest() {
        var savedCategory = categoryRepository.save(
                Category.builder()
                        .description("Test Category")
                        .build()
        );
        testBeer.addCategory(savedCategory);
        Beer savedBeer = beerRepository.save(testBeer);
        log.debug("saved beer: `{}`", savedBeer);
        assertEquals(1, savedBeer.getCategories()
                .size());
    }

}