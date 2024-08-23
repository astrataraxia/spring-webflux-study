package com.sdragon.webfluxplayground.sec03.mapper;

import com.sdragon.webfluxplayground.sec03.dto.CustomerDto;
import com.sdragon.webfluxplayground.sec03.entity.Customer;

public class EntityDtoMapper {

    public static Customer toEntity(CustomerDto dto) {
        return new Customer(dto.id(), dto.name(), dto.email());
    }

    public static CustomerDto toDto(Customer customer) {
        return new CustomerDto(customer.id(), customer.name(), customer.email());
    }

}
