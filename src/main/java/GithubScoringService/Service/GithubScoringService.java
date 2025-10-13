package GithubScoringService.Service;

import githubScoringService.model.GithubRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class GithubScoringService {

  private GithubCallDistributionService githubCallDistributionService;

  public GithubScoringService(GithubCallDistributionService githubCallDistributionService) {
    this.githubCallDistributionService = githubCallDistributionService;
  }

  public List<GithubRepository> getGithubRepositoriesByDateAndLanguage(String authorizationToken, String repositoryLanguage, LocalDate minCreationDateOfRepository) throws IOException {
    List<GithubRepository> githubRepositoryList = githubCallDistributionService.distributeApiCallsByRemainingCallLimit(authorizationToken, repositoryLanguage, minCreationDateOfRepository).block();
    initiatePopularityScoringForRepositories(githubRepositoryList);
    return githubRepositoryList;
  }

  private void initiatePopularityScoringForRepositories(List<GithubRepository> githubRepositories) {
    if (githubRepositories == null || githubRepositories.isEmpty()) {
      return;
    }
    githubRepositories.forEach(this::calculateAndSetPopularityScoringForRepository);
  }

  private void calculateAndSetPopularityScoringForRepository(GithubRepository githubRepository) {
    int pularityScoring = Math.toIntExact(githubRepository.getForksCount() * 100 +
                                          githubRepository.getStargazersCount() * 1000 +
                                          ChronoUnit.MINUTES.between(githubRepository.getUpdatedAt().toLocalDateTime(), LocalDateTime.now()));
    githubRepository.setPopularityScoring(pularityScoring);
  }
}
