package com.sdragon.webfluxplayground.sec07;

import com.sdragon.webfluxplayground.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

public class Lec01MonoTest extends AbstractWebClient {

    private final WebClient client = createWebClient();

    @Test
    public void simpleGet() throws Exception {
        client.get()
                .uri("/lec01/product/1")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .subscribe();

        Thread.sleep(Duration.ofSeconds(2));
    }

    @Test
    public void concurrentRequests() throws Exception {
        for (int i = 1; i <= 50; i++) {
            client.get()
                    .uri("/{lec}/product/{id}","lec01",i)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .doOnNext(print())
                    .subscribe();
        }
        Thread.sleep(Duration.ofSeconds(2));
    }

}
