package com.sdragon.webfluxplayground.sec01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/reactive")
public class ReactiveWebController {

    private static final Logger log = LoggerFactory.getLogger(ReactiveWebController.class);
    private final WebClient webClient = WebClient.create("http://localhost:7070");

    /*
     * Browser unknown stream event
     */
    @GetMapping("/products")
    public Flux<Product> getProducts() {
        return webClient.get()
                .uri("/demo01/products")
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnNext(product -> log.info("received: {}", product));
    }

    /*
     * Browser know stream event
     */
    @GetMapping(value = "/products/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> getProductsStream() {
        return webClient.get()
                .uri("/demo01/products")
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnNext(product -> log.info("received: {}", product));
    }

    /*
     * It is pseudo code to given an idea
     *
     * getProducts()
     *    .subscribe(
     *       //on next
     *       product -> channel.writeAndFlush(product),
     *       subscription -> channel.conClose(() -> subscription.cancel())
     *     )
     */


    @GetMapping("/products2")
    public Flux<Product> getProducts2() {
        return webClient.get()
                .uri("/demo01/products/notorious")
                .retrieve()
                .bodyToFlux(Product.class)
                .onErrorComplete()
                .doOnNext(product -> log.info("received: {}", product));
    }

}



