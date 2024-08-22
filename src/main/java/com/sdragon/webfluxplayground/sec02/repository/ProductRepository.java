package com.sdragon.webfluxplayground.sec02.repository;

import com.sdragon.webfluxplayground.sec02.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {

    Flux<Product> findByPriceBetween(int from, int to);

    Flux<Product> findBy(Pageable pageable);
}
