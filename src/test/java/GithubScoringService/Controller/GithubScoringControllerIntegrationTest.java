package GithubScoringService.Controller;

import GithubScoringService.GithubScoringServiceApplication;
import GithubScoringService.Service.GithubScoringService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {GithubScoringServiceApplication.class})
class GithubScoringControllerIntegrationTest {

  @Autowired
  private WebTestClient webTestClient;

  static Stream<Arguments> searchParameterValueProvider() {
    return Stream.of(
        org.junit.jupiter.params.provider.Arguments.of("java", "2025-10-09"),
        org.junit.jupiter.params.provider.Arguments.of(null, "2025-10-09"),
        org.junit.jupiter.params.provider.Arguments.of("java", null),
        org.junit.jupiter.params.provider.Arguments.of(null, null)
    );
  }

  private void addHeaderIfNotNull(org.springframework.http.HttpHeaders headers, String name, String value) {
    if (value != null) {
      headers.add(name, value);
    }
  }

  @ParameterizedTest
  @MethodSource("searchParameterValueProvider")
  public void getGithubRepositoriesByDateAndLanguageReturnsGithubRepositoriesAsJson(String language, String date) throws JsonProcessingException {
    String responseBody = webTestClient.get()
        .uri("/getGithubReposSortedByScore/")
        .headers(headers -> {
          addHeaderIfNotNull(headers, "repositoryLanguage", language);
          addHeaderIfNotNull(headers, "minCreationDateOfRepository", date);
        })
        .exchange()
        .returnResult(String.class)
        .getResponseBody()
        .blockFirst();

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(responseBody);

    assertThat(jsonNode.has("total_count")).isTrue();
    assertThat(jsonNode.get("total_count").asInt()).isGreaterThan(0);
    assertThat(jsonNode.get("items").isArray()).isTrue();
    assertThat(jsonNode.get("items").size()).isGreaterThan(0);
  }
}