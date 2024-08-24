package com.sdragon.webfluxplayground.sec05.controller;

import com.sdragon.webfluxplayground.sec05.dto.CustomerDto;
import com.sdragon.webfluxplayground.sec05.exceptions.ApplicationExceptions;
import com.sdragon.webfluxplayground.sec05.service.CustomerService;
import com.sdragon.webfluxplayground.sec05.validator.RequestValidator;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public Flux<CustomerDto> allCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/paginated")
    public Mono<List<CustomerDto>> allCustomers(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "3") Integer size) {
        return customerService.getAllCustomers(page, size)
                              .collectList();
    }

    @GetMapping("/{id}")
    public Mono<CustomerDto> getCustomer(@PathVariable Integer id) {
        return customerService.getCustomerById(id)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id));
    }

    @PostMapping
    public Mono<CustomerDto> saveCustomer(@RequestBody Mono<CustomerDto> mono) {
        return mono.transform(RequestValidator.validate())
                .as(customerService::saveCustomer);
        // return customerService.saveCustomer(mono.transform(RequestValidator.validate())); bad
    }

    @PutMapping("/{id}")
    public Mono<CustomerDto> updateCustomer(@PathVariable Integer id, @RequestBody Mono<CustomerDto> mono) {
        return mono.transform(RequestValidator.validate())
                .as(validReq -> customerService.updateCustomer(id, validReq))
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCustomer(@PathVariable Integer id) {
        return customerService.deleteCustomerById(id)
                .filter(aBoolean -> aBoolean)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                .then();
    }
}

