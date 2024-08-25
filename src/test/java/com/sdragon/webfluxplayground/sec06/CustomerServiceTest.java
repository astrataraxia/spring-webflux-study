package com.sdragon.webfluxplayground.sec06;

import com.sdragon.webfluxplayground.sec03.dto.CustomerDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec06")
public class CustomerServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Autowired
    private WebTestClient client;

    @Test
    @DisplayName("GET: AllCustomers")
    public void allCustomers() throws Exception {
        client.get()
                .uri("/customers")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CustomerDto.class)
                .value(customerList -> log.info("{}", customerList))
                .hasSize(10);
    }

    @Test
    @DisplayName("GET: AllCustomers Paginated")
    public void paginatedCustomers() throws Exception {
        client.get()
                .uri("/customers/paginated?page=3&size=2")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(result -> log.info("{}", new String(Objects.requireNonNull(result.getResponseBody()))))
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].id").isEqualTo(5)
                .jsonPath("$[1].id").isEqualTo(6);
    }

    @Test
    @DisplayName("Get: One CustomerById")
    public void customerById() throws Exception {
        client.get()
                .uri("/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(result -> log.info("{}", new String(Objects.requireNonNull(result.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("sam")
                .jsonPath("$.email").isEqualTo("sam@gmail.com");
    }

    @Test
    @DisplayName("POST & DELETE: create customer and delete ")
    public void createAndDelete() throws Exception {
        var dto = new CustomerDto(null, "marshal", "marshal@gmail.com");

        // create
        client.post()
                .uri("/customers")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(result -> log.info("{}", new String(Objects.requireNonNull(result.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(11)
                .jsonPath("$.name").isEqualTo("marshal")
                .jsonPath("$.email").isEqualTo("marshal@gmail.com");
        // delete
        client.delete()
                .uri("/customers/11")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().isEmpty();
    }

    @Test
    @DisplayName("UPDATE: customer update data ")
    public void updateCustomer() throws Exception {
        var dto = new CustomerDto(10, "noel", "noel@gmail.com");

        client.put()
                .uri("/customers/10")
                .bodyValue(dto)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(result -> log.info("{}", new String(Objects.requireNonNull(result.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(10)
                .jsonPath("$.name").isEqualTo("noel")
                .jsonPath("$.email").isEqualTo("noel@gmail.com");
    }

    @Test
    @DisplayName("CustomerNotFound")
    public void customerNotFound() throws Exception {
        //get
        client.get()
                .uri("/customers/11")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Customer [id=11] is not found")
                .jsonPath("$.title").isEqualTo("Customer Not Found");


        // delete
        client.delete()
                .uri("/customers/11")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Customer [id=11] is not found")
                .jsonPath("$.title").isEqualTo("Customer Not Found");

        // put
        var dto = new CustomerDto(null, "noel", "noel@gmail.com");
        client.put()
                .uri("/customers/11")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Customer [id=11] is not found")
                .jsonPath("$.title").isEqualTo("Customer Not Found");
    }

    @Test
    @DisplayName("Invalid Input Test")
    public void invalidInput() throws Exception {
        var missingName = new CustomerDto(null, null, "noel@gmail.com");
        // missing name
        client.post()
                .uri("/customers")
                .bodyValue(missingName)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Name is required!")
                .jsonPath("$.title").isEqualTo("Invalid Input");

        var missingEmail = new CustomerDto(null, "marshal", null);
        // Missing email
        client.post()
                .uri("/customers")
                .bodyValue(missingEmail)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Valid email is required!")
                .jsonPath("$.title").isEqualTo("Invalid Input");

        var invalidEmail = new CustomerDto(null, "marshal", "marshal");
        // Invalid email
        client.post()
                .uri("/customers")
                .bodyValue(invalidEmail)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Valid email is required!")
                .jsonPath("$.title").isEqualTo("Invalid Input");
    }
}

