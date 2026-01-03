package githubScoringService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import githubScoringService.GithubScoringServiceApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {GithubScoringServiceApplication.class})
@ActiveProfiles("test")
class GithubScoringControllerIntegrationTest {

  public static final String GET_GITHUB_REPOS_SORTED_BY_POPULARITY_SCORE = "/getGithubReposSortedByPopularityScore/";
  @Autowired
  private WebTestClient webTestClient;

  static Stream<Arguments> searchParameterValueProvider() {
    return Stream.of(
        org.junit.jupiter.params.provider.Arguments.of("java", "2025-10-09", "TestToken"),
        org.junit.jupiter.params.provider.Arguments.of("java", "2025-10-09", null)
    );
  }

  private void addHeaderIfNotNull(org.springframework.http.HttpHeaders headers, String name, String value) {
    if (value != null) {
      headers.add(name, value);
    }
  }

  private String determineUrlStringWithPathVariables(String repositoryLanguage, String minCreationDateOfRepository) {
    String pathString = "";
    if (repositoryLanguage != null && minCreationDateOfRepository != null) {
      pathString = "/" + repositoryLanguage + "/" + minCreationDateOfRepository;
    }
    else if (minCreationDateOfRepository == null) {
      pathString = "/" + repositoryLanguage + "/null";
    }
    else if (repositoryLanguage == null) {
      pathString = "/null/" + minCreationDateOfRepository;
    }
    return pathString;
  }

  @ParameterizedTest
  @MethodSource("searchParameterValueProvider")
  public void should_returnGithubRepositoriesAsJson_when_differentParametersAreSet(String language, String date, String authToken) throws JsonProcessingException {
    String responseBody = webTestClient.get()
        .uri("/getGithubReposSortedByPopularityScore" + determineUrlStringWithPathVariables(language, date))
        .headers(headers -> {
          addHeaderIfNotNull(headers, "authorizationToken", authToken);
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

  @Test
  public void should_githubRepositorySearchReturnEmptyList_when_creationDateInFuture() {
    String responseBody = webTestClient.get()
        .uri(GET_GITHUB_REPOS_SORTED_BY_POPULARITY_SCORE + "null/2099-10-01")
        .headers(headers -> {
          addHeaderIfNotNull(headers, "authorizationToken", null);
        })
        .exchange()
        .returnResult(String.class)
        .getResponseBody()
        .blockFirst();

    assertThat("[]".equals(responseBody)).isTrue();
  }

  @Test
  public void should_githubRepositorySearchReturnEmptyList_when_languageNotExisting() throws JsonProcessingException {
    String responseBody = webTestClient.get()
        .uri(GET_GITHUB_REPOS_SORTED_BY_POPULARITY_SCORE + "notReallyExisting/null")
        .headers(headers -> {
          addHeaderIfNotNull(headers, "authorizationToken", "schmutz");
        })
        .exchange()
        .returnResult(String.class)
        .getResponseBody()
        .blockFirst();

    assertThat("[]".equals(responseBody)).isTrue();
  }
}
