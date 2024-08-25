package com.sdragon.webfluxplayground.sec07;

import com.sdragon.webfluxplayground.sec07.dto.CalculatorResponse;
import com.sdragon.webfluxplayground.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec05ErrorResponseTest extends AbstractWebClient{

    private static final Logger log = LoggerFactory.getLogger(Lec05ErrorResponseTest.class);


    private final WebClient client = createWebClient();

    @Test
    public void handlingError() {
        client.get()
                .uri("/lec05/calculator/{a}/{b}", 10, 20)
                .header("operation", "+")
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
//                .onErrorReturn(new CalculatorResponse(0,0, null, 0.0))
                .doOnError(WebClientResponseException.class, err-> log.info("{}",err.getResponseBodyAs(ProblemDetail.class)))
                .onErrorReturn(WebClientResponseException.InternalServerError.class, new CalculatorResponse(0,0, null, 0.0))
                .onErrorReturn(WebClientResponseException.BadRequest.class, new CalculatorResponse(0,0, null, -1.0))
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void exchange() {
        client.get()
                .uri("/lec05/calculator/{a}/{b}", 10, 20)
                .header("operation", "-")
                .exchangeToMono(this::decode )
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }

    private Mono<CalculatorResponse> decode(ClientResponse clientResponse) {
//        clientResponse.cookies()
//        clientResponse.headers()
        log.info("status code: {}", clientResponse.statusCode());
        if (clientResponse.statusCode().isError()) {
            return clientResponse.bodyToMono(ProblemDetail.class)
                    .doOnNext(pd -> log.info("{}", pd))
                    .then(Mono.empty());
        }
        return clientResponse.bodyToMono(CalculatorResponse.class);
    }
}
