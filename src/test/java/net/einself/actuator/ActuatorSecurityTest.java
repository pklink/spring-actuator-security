package net.einself.actuator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

import static java.nio.charset.StandardCharsets.UTF_8;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ActuatorSecurityTest {

    @Value("${spring.security.user.name}")
    String username;

    @Value("${spring.security.user.password}")
    String password;

    @Autowired
    WebTestClient webTestClient;

    @Test
    @WithAnonymousUser
    void shouldNotReturnHealthDetailsIfUserNotLoggedIn() {
        webTestClient
                .get().uri("/actuator/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("status").isEqualTo("UP")
                    .jsonPath("$.components").doesNotExist();
    }

    @Test
    void shouldReturnHealthDetailsIfUserLoggedIn() {
        final var login = String.format("%s:%s", username, password);
        final var authHeaderValue = Base64Utils.encodeToString(login.getBytes(UTF_8));

        webTestClient
                .get()
                    .uri("/actuator/health")
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + authHeaderValue)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("status").isEqualTo("UP")
                    .jsonPath("$.components").exists();
    }

}
