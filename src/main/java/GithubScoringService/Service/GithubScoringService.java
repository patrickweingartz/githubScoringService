package GithubScoringService.Service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class GithubScoringService {

  GithubApiService githubApiService;

  public GithubScoringService(GithubApiService githubApiService) {
    this.githubApiService = githubApiService;
  }

  public String getGithubRepositoriesByDateAndLanguage(String repositoryLanguage, LocalDate minCreationDateOfRepository) {
    return githubApiService.getFilteredAndSortedRepositories(repositoryLanguage, minCreationDateOfRepository);
  }
}
