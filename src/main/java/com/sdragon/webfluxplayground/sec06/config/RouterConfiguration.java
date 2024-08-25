package com.sdragon.webfluxplayground.sec06.config;

import com.sdragon.webfluxplayground.sec06.exceptions.CustomerNotFoundException;
import com.sdragon.webfluxplayground.sec06.exceptions.InvalidInputException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration {

    private final CustomerRequestHandler requestHandler;
    private final ApplicationExceptionHandler exceptionHandler;

    public RouterConfiguration(CustomerRequestHandler requestHandler, ApplicationExceptionHandler exceptionHandler) {
        this.requestHandler = requestHandler;
        this.exceptionHandler = exceptionHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> customerRoutes() {
        return RouterFunctions.route()
                .path("/customers",this::getRoutes)
                .POST("/customers", requestHandler::saveCustomer)
                .PUT("/customers/{id}", requestHandler::updateCustomer)
                .DELETE("/customers/{id}", requestHandler::deleteCustomer)
                .onError(CustomerNotFoundException.class, exceptionHandler::handleException)
                .onError(InvalidInputException.class, exceptionHandler::handleException)
                .build();
    }

    // It is  important to route order
    private RouterFunction<ServerResponse> getRoutes() {
        return RouterFunctions.route()
                .GET("/paginated", requestHandler::paginatedCustomers)
                .GET("/{id}", requestHandler::getCustomer)
                .GET(requestHandler::allCustomers)
                .build();
    }
}
