package com.sdragon.webfluxplayground.sec05;

import com.sdragon.webfluxplayground.sec05.dto.CustomerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec05")
public class CustomerServiceTest {

    // just validate HTTP response status codes!
    // unauthorized - no token
    // unauthorized - invalid token
    // standard category - GET : success, OTHER method: forbidden
    // prime category - HTTP Method : success

    @Autowired
    private WebTestClient client;

    @Test
    public void unauthorized() {
        // no token
        client.get().uri("/customers")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);

        //invalid token
        validateGet("invalid_token", HttpStatus.UNAUTHORIZED);
    }


    @Test
    public void standardCategory() {
        validateGet("secret123", HttpStatus.OK);
        validatePost("secret123", HttpStatus.FORBIDDEN);
    }

    @Test
    public void primeCategory() {
        validateGet("secret456", HttpStatus.OK);
        validatePost("secret456", HttpStatus.OK);
    }

    private void validateGet(String token, HttpStatus expectedStatus) {
        client.get().uri("/customers")
                .header("auth-token", token)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }

    private void validatePost(String token, HttpStatus expectedStatus) {
        var dto = new CustomerDto(null, "nella", "nella@gmail.com");
        client.post().uri("/customers")
                .header("auth-token", token)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }
}
