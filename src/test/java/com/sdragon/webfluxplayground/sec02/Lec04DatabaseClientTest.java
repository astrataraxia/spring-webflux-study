package com.sdragon.webfluxplayground.sec02;

import com.sdragon.webfluxplayground.sec02.dto.OrderDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

public class Lec04DatabaseClientTest extends AbstractTest{

    private static final Logger log = LoggerFactory.getLogger(Lec04DatabaseClientTest.class);

    @Autowired
    private DatabaseClient client;


    @Test
    @DisplayName("OrderDetails By Products")
    public void orderDetailsByProducts() throws Exception {
        String query = """
                SELECT
                    co.order_id,
                    c.name AS customer_name,
                    p.description AS product_name,
                    co.amount,
                    co.order_date
                FROM
                    customer c
                INNER JOIN customer_order co ON c.id = co.customer_id
                INNER JOIN product p ON p.id = co.product_id
                WHERE
                    p.description = :description
                ORDER BY co.amount DESC
                """;

        client.sql(query)
                .bind("description", "iphone 20")
                .mapProperties(OrderDetails.class)
                .all()
                .doOnNext(dto -> log.info("{}", dto))
                .as(StepVerifier::create)
                .assertNext(dto -> assertThat(dto.amount()).isEqualTo(975))
                .assertNext(dto -> assertThat(dto.amount()).isEqualTo(950))
                .verifyComplete();
    }
}
