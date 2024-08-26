package com.sdragon.webfluxplayground.sec08.service;

import com.sdragon.webfluxplayground.sec08.dto.ProductDto;
import com.sdragon.webfluxplayground.sec08.mapper.EntityDtoMapper;
import com.sdragon.webfluxplayground.sec08.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Flux<ProductDto> saveProducts(Flux<ProductDto> flux) {
        return flux.map(EntityDtoMapper::toEntity)
                .as(repository::saveAll)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<Long> getProductsCount() {
        return repository.count();
    }

    public Flux<ProductDto> allProducts() {
        return repository.findAll()
                .map(EntityDtoMapper::toDto);
    }
}
