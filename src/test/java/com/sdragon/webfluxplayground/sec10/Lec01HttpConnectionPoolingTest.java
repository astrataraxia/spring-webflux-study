package com.sdragon.webfluxplayground.sec10;

import com.sdragon.webfluxplayground.sec10.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

public class Lec01HttpConnectionPoolingTest extends AbstractWebClient {
    /*
        windows
        netstat -an | findstr 127.0.0.1:7070

        mac & linux
        netstat -an| grep -w 127.0.0.1.7070
        watch 'netstat -an| grep -w 127.0.0.1.7070'
     */

    // number of connections * average response time = expected through put => 500 * 100ms => 5000/sec it is large!
    //http 1.1
    private final WebClient webClient = createWebClient(b -> {
        var poolSize = 20000;
        var provider = ConnectionProvider.builder("sy")
                .lifo()
                .maxConnections(poolSize)
                .pendingAcquireMaxCount(poolSize * 5)  //waiting queue size
                .build();
        var httpClient = HttpClient.create(provider)
                .compress(true)
                .keepAlive(true);
        b.clientConnector(new ReactorClientHttpConnector(httpClient));
    });

    @Test
    public void concurrentRequests() {
        var max = 20000;
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
