package com.sdragon.webfluxplayground.sec09.service;

import com.sdragon.webfluxplayground.sec09.config.ApplicationConfig;
import com.sdragon.webfluxplayground.sec09.dto.ProductDto;
import com.sdragon.webfluxplayground.sec09.mapper.EntityDtoMapper;
import com.sdragon.webfluxplayground.sec09.repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final Sinks.Many<ProductDto> sink;

    public ProductService(ProductRepository repository, Sinks.Many<ProductDto> sink) {
        this.repository = repository;
        this.sink = sink;
    }

    public Mono<ProductDto> saveProducts(Mono<ProductDto> mono) {
        return mono.map(EntityDtoMapper::toEntity)
                .flatMap(repository::save)
                .map(EntityDtoMapper::toDto)
                .doOnNext(sink::tryEmitNext);
    }

    public Flux<ProductDto> productStream() {
        return sink.asFlux();
    }
}
