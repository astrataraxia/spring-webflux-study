package com.sdragon.webfluxplayground.sec06.assignment;

import com.sdragon.webfluxplayground.sec06.exceptions.ApplicationExceptions;
import com.sdragon.webfluxplayground.sec06.exceptions.InvalidInputException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/*
    /calculator/{a}/{b}

    Header: operation +,-,/,*
 */
@Configuration
public class CalculatorAssignment {

    @Bean
    public RouterFunction<ServerResponse> calculatorRoutes() {
        return RouterFunctions.route()
                .path("/calculator", this::routes)
                .build();

    }

    private RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
                .GET("/{a}/0", badRequest("b cannot be 0"))
                .GET("/{a}/{b}",isOperation("+"),handle((a,b) -> a + b ))
                .GET("/{a}/{b}",isOperation("-"),handle((a,b) -> a - b ))
                .GET("/{a}/{b}",isOperation("*"),handle((a,b) -> a * b ))
                .GET("/{a}/{b}",isOperation("/"),handle((a,b) -> a / b ))
                .GET("/{a}/{b}",badRequest("operation header should be + - * /"))
                .build();
    }

    private RequestPredicate isOperation(String operation) {
        return RequestPredicates.headers(h -> operation.equals(h.firstHeader("operation")));
    }

    private HandlerFunction<ServerResponse> handle(BiFunction<Integer, Integer, Integer> function) {
        return req -> {
            var a = Integer.parseInt(req.pathVariable("a"));
            var b = Integer.parseInt(req.pathVariable("b"));
            Integer result = function.apply(a, b);
            return ServerResponse.ok().bodyValue(result);
        };
    }

    private HandlerFunction<ServerResponse> badRequest(String message) {
        return request -> ServerResponse.badRequest().bodyValue(message);
    }
}
