package com.sdragon.webfluxplayground.sec08;

import com.sdragon.webfluxplayground.sec08.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.nio.file.Path;
import java.time.Duration;

public class ProductsUploadDownloadTest {

    private static final Logger log = LoggerFactory.getLogger(ProductsUploadDownloadTest.class);
    private final ProductClient productClient = new ProductClient();

    @Test
    public void upload() {
        var flux = Flux.just(new ProductDto(null, "iphone", 1000))
                .delayElements(Duration.ofSeconds(10));

        productClient.uploadProducts(flux)
                .doOnNext(r -> log.info("received: {}", r))
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void uploadRange() {
        var flux = Flux.range(1, 1_000_000)
                .map(i -> new ProductDto(null, "product-" + i, i));

        productClient.uploadProducts(flux)
                .doOnNext(r -> log.info("received: {}", r))
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void download() {
        productClient.downloadProducts()
                .map(ProductDto::toString)
                .as(flux -> FileWriter.create(flux, Path.of("products.txt")))
                .as(StepVerifier::create)
                .verifyComplete();
    }
}
