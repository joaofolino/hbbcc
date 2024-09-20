package com.example.hbbcc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.LinkedList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HbbccApplicationIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testGetStudentById_success_returnStudent() {
        LinkedList<String> productIdList = new LinkedList<String>(List.of("001", "002", "001", "004", "003"));
        this.webTestClient
                .post()
                .uri("/checkout")
                .body(BodyInserters.fromValue(productIdList))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("price").isEqualTo("360.0");
    }

}