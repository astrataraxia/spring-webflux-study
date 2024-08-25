package com.sdragon.webfluxplayground.sec06;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

import static org.assertj.core.api.Assertions.*;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec06")
public class CalculatorTest {


    private static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Autowired
    private WebTestClient client;

    @Test
    public void calculator() {
        // success
        validate(20, 10, "+", "30", 200);
        validate(20, 10, "-", "10", 200);
        validate(20, 10, "*", "200", 200);
        validate(20, 10, "/", "2", 200);

        // bad requests
        validate(20, 0, "+", "b cannot be 0", 400);
        validate(20, 10, "@", "operation header should be + - * /", 400);
        validate(20, 10, null, "operation header should be + - * /", 400);
    }

    private void validate(int a, int b, String operation, String expectedResult, int httpStatus) {
        client.get()
                .uri("/calculator/{a}/{b}", a, b)
                .headers(header -> {
                    if (Objects.nonNull(operation)) {
                        header.add("operation", operation);
                    }
                })
                .exchange()
                .expectStatus().isEqualTo(httpStatus)
                .expectBody(String.class)
                .value(s -> {
                    assertThat(s).isNotNull();
                    assertThat(s).isEqualTo(expectedResult);
                });
    }
}
