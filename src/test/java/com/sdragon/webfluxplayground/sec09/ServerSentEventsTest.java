package com.sdragon.webfluxplayground.sec09;

import com.sdragon.webfluxplayground.sec09.dto.ProductDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AssertionsKt;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.*;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec09")
public class ServerSentEventsTest {

    private static final Logger log = LoggerFactory.getLogger(ServerSentEventsTest.class);

    @Autowired
    private WebTestClient client;

    @Test
    public void serverSentEvents() {
        client.get()
                .uri("/products/stream/{maxPrice}", 80)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(ProductDto.class)
                .getResponseBody()
                .take(50)
                .doOnNext(dto -> log.info("received: {}", dto))
                .collectList()
                .as(StepVerifier::create)
                .assertNext(list -> {
                    assertThat(list.size()).isEqualTo(50);
                    assertThat(list.stream().allMatch(p -> p.price() <= 80)).isTrue();
                })
                .verifyComplete();
    }
}
