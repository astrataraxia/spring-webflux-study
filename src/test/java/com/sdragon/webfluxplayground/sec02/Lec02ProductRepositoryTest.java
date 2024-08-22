package com.sdragon.webfluxplayground.sec02;

import com.sdragon.webfluxplayground.sec02.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.*;

class Lec02ProductRepositoryTest extends AbstractTest{

    private static final Logger log = LoggerFactory.getLogger(Lec02ProductRepositoryTest.class);

    @Autowired
    private ProductRepository repository;

    @Test
    @DisplayName("Price Range")
    public void findByPriceRange() throws Exception {
        repository.findByPriceBetween(750, 1000)
                .doOnNext(p -> log.info("{}", p))
                .as(StepVerifier::create)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    @DisplayName("Pageable Test")
    public void pageable() throws Exception {
        repository.findBy(PageRequest.of(0, 3).withSort(Sort.by("price").ascending()))
                .doOnNext(p -> log.info("{}", p))
                .as(StepVerifier::create)
                .assertNext(p-> assertThat(p.getPrice()).isEqualTo(200))
                .assertNext(p-> assertThat(p.getPrice()).isEqualTo(250))
                .assertNext(p-> assertThat(p.getPrice()).isEqualTo(300))
                .verifyComplete();
    }
}