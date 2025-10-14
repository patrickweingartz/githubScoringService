package githubScoringService.service;

import githubScoringService.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class GithubApiService {

  @Value("${maxResultsPerPage}")
  private int maxResultsPerPage;
  @Value("${remainingRequestLimitForRespositorySearch:0}")
  private int remainingRequestLimit;

  private final WebClient webClient;
  private final JsonUtils jsonUtils;

  public GithubApiService(WebClient webClient, JsonUtils jsonUtils) {
    this.webClient = webClient;
    this.jsonUtils = jsonUtils;
  }

  private void addHeaderIfNotNull(org.springframework.http.HttpHeaders headers, String name, String value) {
    if (value != null) {
      headers.add(name, value);
    }
  }

  private String determineSearchStrings(String repositoryLanguage, LocalDate minCreationDateOfRepository) {
    String searchString="";
    if (repositoryLanguage != null && minCreationDateOfRepository != null) {
      searchString = "q=language:" + repositoryLanguage + "&created:>=" + minCreationDateOfRepository+"&";
    }
    else if (minCreationDateOfRepository == null) {
      searchString = "q=language:" + repositoryLanguage+"&";
    }
    else if (repositoryLanguage == null) {
      searchString = "q=created:>=" + minCreationDateOfRepository+"&";
    }
    return searchString;
  }

  public Mono<String> getFilteredAndSortedRepositories(String authorizationToken, String repositoryLanguage, LocalDate minCreationDateOfRepository, int resultPage) {
    return webClient.get()
        .uri("/search/repositories?" + determineSearchStrings(repositoryLanguage, minCreationDateOfRepository) +"per_page=" + maxResultsPerPage + "&page=" + resultPage)
        .headers(headers -> {
          addHeaderIfNotNull(headers, "authorizationToken", authorizationToken);
        })
        .retrieve()
        .bodyToMono(String.class);
  }

  public Integer getRemainingRateLimitForRepositorySearch(String authorizationToken) throws IOException {
    if (remainingRequestLimit > 0) {
      return remainingRequestLimit;
    }

    String response = webClient.get()
        .uri("/rate_limit")
        .headers(headers -> {
          addHeaderIfNotNull(headers, "Authorization", authorizationToken);
        })
        .retrieve()
        .bodyToMono(String.class)
        .block();

    return jsonUtils.parseRemainingCallsForRepositorySearchFromJsonResult(response);
  }
}