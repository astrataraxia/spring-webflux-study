package com.sdragon.webfluxplayground.sec08.mapper;

import com.sdragon.webfluxplayground.sec08.dto.ProductDto;
import com.sdragon.webfluxplayground.sec08.entity.Product;

public class EntityDtoMapper {

    public static Product toEntity(ProductDto dto) {
        var product = new Product();
        product.setId(dto.id());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        return product;
    }

    public static ProductDto toDto(Product product) {
        return new ProductDto(product.id(), product.description(), product.price());
    }
}
