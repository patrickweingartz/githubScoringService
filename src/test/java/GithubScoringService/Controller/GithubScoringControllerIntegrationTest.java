package GithubScoringService.Controller;

import GithubScoringService.GithubScoringServiceApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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

    assertThat(jsonNode.get(0).has("id")).isTrue();
    assertThat(jsonNode.get(0).has("full_name")).isTrue();
    assertThat(jsonNode.get(0).has("stargazers_count")).isTrue();
    assertThat(jsonNode.get(0).has("forks_count")).isTrue();
    assertThat(jsonNode.get(0).has("updated_at")).isTrue();
    assertThat(jsonNode.get(0).has("popularityScoring")).isTrue();
  }
}