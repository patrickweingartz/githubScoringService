package GithubScoringService.Service;

import GithubScoringService.Utils.JsonUtils;
import githubScoringService.model.GithubRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class GithubScoringService {

  private GithubApiService githubApiService;

  private JsonUtils jsonUtils;

  public GithubScoringService(GithubApiService githubApiService, JsonUtils jsonUtils) {
    this.githubApiService = githubApiService;
    this.jsonUtils = jsonUtils;
  }

  public List<GithubRepository> getGithubRepositoriesByDateAndLanguage(String repositoryLanguage, LocalDate minCreationDateOfRepository) throws IOException {
    String githubRepositoryAsJsonResult = githubApiService.getFilteredAndSortedRepositories(repositoryLanguage, minCreationDateOfRepository);
    List<GithubRepository> githubRepositories = jsonUtils.parseGithubRepositoriesFromJsonResult(githubRepositoryAsJsonResult);

    initiatePopularityScoringForRepositories(githubRepositories);
    return githubRepositories;
  }

  private void initiatePopularityScoringForRepositories(List<GithubRepository> githubRepositories) {
    githubRepositories.forEach(this::calculateAndSetPopularityScoringForRepository);
  }

  private void calculateAndSetPopularityScoringForRepository(GithubRepository githubRepository) {
    int pularityScoring = Math.toIntExact(githubRepository.getForksCount() * 100 +
                                          githubRepository.getStargazersCount() * 1000 +
                                          ChronoUnit.MINUTES.between(githubRepository.getUpdatedAt().toLocalDateTime(), LocalDateTime.now()));
    githubRepository.setPopularityScoring(pularityScoring);
  }
}
