package com.sdragon.webfluxplayground.sec02;

import com.sdragon.webfluxplayground.sec02.repository.CustomerOrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.*;

class Lec03CustomerOrderRepositoryTest extends AbstractTest{

    private static final Logger log = LoggerFactory.getLogger(Lec03CustomerOrderRepositoryTest.class);

    @Autowired
    private CustomerOrderRepository repository;

    @Test
    @DisplayName("Products Ordered By Customer")
    public void productsOrderedByCustomer() throws Exception {
        repository.getProductsOrderedByCustomer("mike")
                .doOnNext(p -> log.info("{}", p))
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }
    
    @Test
    @DisplayName("OrderDetails By Products")
    public void orderDetailsByProducts() throws Exception {
        repository.getOrderDetailsByProduct("iphone 20")
                .doOnNext(dto -> log.info("{}", dto))
                .as(StepVerifier::create)
                .assertNext(dto -> assertThat(dto.amount()).isEqualTo(975))
                .assertNext(dto -> assertThat(dto.amount()).isEqualTo(950))
                .verifyComplete();
    }
}