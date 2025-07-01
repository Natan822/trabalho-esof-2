package config_server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ConfigServerApplicationTests {

	@Autowired
	WebTestClient webTestClient;

	@Test
	void testServiceRequest() {
		webTestClient.get()
				.uri("/test-service/default")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.consumeWith(response -> {
					String body = new String(response.getResponseBody());
					assertThat(body).contains("propertySources");
					assertThat(body).contains("aaaaaaa");
				});
	}

}
