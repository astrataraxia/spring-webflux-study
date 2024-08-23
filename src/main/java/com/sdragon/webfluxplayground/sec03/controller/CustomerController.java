package com.sdragon.webfluxplayground.sec03.controller;

import com.sdragon.webfluxplayground.sec03.dto.CustomerDto;
import com.sdragon.webfluxplayground.sec03.service.CustomerService;
import org.springframework.http.ResponseEntity;
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
    public Mono<ResponseEntity<CustomerDto>> getCustomer(@PathVariable Integer id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<CustomerDto> saveCustomer(@RequestBody Mono<CustomerDto> mono) {
        return customerService.saveCustomer(mono);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CustomerDto>> updateCustomer(@PathVariable Integer id, @RequestBody Mono<CustomerDto> mono) {
        return customerService.updateCustomer(id, mono)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable Integer id) {
        return customerService.deleteCustomerById(id)
                .filter(aBoolean -> aBoolean)
                .map(aBoolean -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

