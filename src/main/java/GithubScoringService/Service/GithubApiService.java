package GithubScoringService.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

@Service
public class GithubApiService {

  @Value("${maxResultsPerPage}")
  private int maxResultsPerPage;
  private String languageSearchString = "";
  private String getLanguageSearchString = "";

  private final WebClient webClient;

  public GithubApiService(WebClient webClient) {
    this.webClient = webClient;
  }

  public String getFilteredAndSortedRepositories(String repositoryLanguage, LocalDate minCreationDateOfRepository) {
    determineSearchStrings(repositoryLanguage, minCreationDateOfRepository);

    return webClient.get()
        .uri("/search/repositories?"+languageSearchString + getLanguageSearchString + "&fork:" + true + "&per_page=" + maxResultsPerPage + "page=" + 1)
        .retrieve()
        .bodyToMono(String.class)
        .block();
  }

  private void determineSearchStrings(String repositoryLanguage, LocalDate minCreationDateOfRepository) {
    if (repositoryLanguage != null){
      languageSearchString = "q=language:" + repositoryLanguage;
    }
    if (minCreationDateOfRepository != null){
      getLanguageSearchString = "&q=created:>=" + minCreationDateOfRepository;
    }
  }
}