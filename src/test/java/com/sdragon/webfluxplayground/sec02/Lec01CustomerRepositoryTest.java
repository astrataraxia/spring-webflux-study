package com.sdragon.webfluxplayground.sec02;

import com.sdragon.webfluxplayground.sec02.entity.Customer;
import com.sdragon.webfluxplayground.sec02.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

public class Lec01CustomerRepositoryTest extends AbstractTest{

    private static final Logger log = LoggerFactory.getLogger(Lec01CustomerRepositoryTest.class);

    @Autowired
    private CustomerRepository repository;

    @Test
    @DisplayName("FindAll")
    public void findAll() {
        repository.findAll()
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    @DisplayName("FindById")
    public void findById() {
        repository.findById(2)
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c ->
                        assertThat(c.getName()).isEqualTo("mike")
                )
                .verifyComplete();
    }
    
    @Test
    @DisplayName("FindByName")
    public void findByName() throws Exception {
        repository.findByName("jake")
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c ->
                        assertThat(c.getEmail()).isEqualTo("jake@gmail.com"))
                .verifyComplete();
    }

    /*
        Query methods
        https://docs.spring.io/spring-data/relational/reference/r2dbc/query-methods.html

        Task: find all customers whose email ending with "ke@gmail.com"
     */

    @Test
    @DisplayName("FindByEmail ending with ke@gamil.com")
    public void findQuery() throws Exception {
        repository.findByEmailEndingWith("ke@gmail.com")
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c ->
                        assertThat(c.getEmail()).isEqualTo("mike@gmail.com"))
                .assertNext(c ->
                        assertThat(c.getEmail()).isEqualTo("jake@gmail.com"))
                .verifyComplete();
    }

    @Test
    @DisplayName("InsertAndDeleteCustomer")
    public void insertAndDeleteCustomer() throws Exception {
        //insert
        var customer = new Customer();
        customer.setName("marshal");
        customer.setEmail("marshal@gmail.com");
        repository.save(customer)
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> assertThat(c.getId()).isNotNull())
                .verifyComplete();

        // count
        repository.count()
                .as(StepVerifier::create)
                .expectNext(11L)
                .verifyComplete();

        //delete
        repository.deleteById(11)
                .then(repository.count())
                .as(StepVerifier::create)
                .expectNext(10L)
                .verifyComplete();
    }
    
    @Test
    @DisplayName("Update Customer")
    public void updateCustomer() throws Exception {
        repository.findByName("ethan")
                .doOnNext(c -> c.setName("noel"))
                .flatMap(c -> repository.save(c))
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> assertThat(c.getName()).isEqualTo("noel"))
                .verifyComplete();

    }
}
