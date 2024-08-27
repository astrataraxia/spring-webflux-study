package com.sdragon.webfluxplayground.sec10;

import com.sdragon.webfluxplayground.sec10.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

public class Lec02Http2Test extends AbstractWebClient {

    private final WebClient webClient = createWebClient(b -> {
        var poolSize = 1;
        var provider = ConnectionProvider.builder("sy")
                .lifo()
                .maxConnections(poolSize)
                .build();
        var httpClient = HttpClient.create(provider)
                .protocol(HttpProtocol.H2C)
                .compress(true)
                .keepAlive(true);
        b.clientConnector(new ReactorClientHttpConnector(httpClient));
    });

    @Test
    public void concurrentRequests() {
        var max = 20_000;
        Flux.range(1, max)
                .flatMap(this::getProduct,max)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(list -> assertThat(list.size()).isEqualTo(max))
                .verifyComplete();
    }


    private Mono<Product> getProduct(int id) {
        return webClient.get()
                .uri("/product/{id}", id)
                .retrieve()
                .bodyToMono(Product.class);
    }
}
