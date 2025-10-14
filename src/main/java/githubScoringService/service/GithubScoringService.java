package githubScoringService.service;

import githubScoringService.model.GithubRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
public class GithubScoringService {

  @Value("${forkRelevanceFactorForAlgorithm}")
  public long fork_relevance_factor;
  @Value("${stargazersRelevanceFactorAlgorithm}")
  public long stargazers_relevance_factor;

  private GithubCallDistributionService githubCallDistributionService;

  public GithubScoringService(GithubCallDistributionService githubCallDistributionService) {
    this.githubCallDistributionService = githubCallDistributionService;
  }

  public List<GithubRepository> getRepositoriesSortedByPopularity(String authorizationToken, String repositoryLanguage, LocalDate minCreationDateOfRepository) throws IOException {
    List<GithubRepository> githubRepositoryList = githubCallDistributionService.distributeApiCallsByRemainingCallLimit(authorizationToken, repositoryLanguage,
                                                                                                                       minCreationDateOfRepository).block();
    if (!githubRepositoryList.isEmpty()) {
      initiatePopularityScoringForRepositories(githubRepositoryList);
      return orderReturnedRepositoriesByPopularity(githubRepositoryList);
    }
    return githubRepositoryList;
  }

  private void initiatePopularityScoringForRepositories(List<GithubRepository> githubRepositories) {
    if (githubRepositories == null || githubRepositories.isEmpty()) {
      return;
    }
    githubRepositories.forEach(this::calculateAndSetPopularityScoringForRepository);
  }

  private void calculateAndSetPopularityScoringForRepository(GithubRepository githubRepository) {
    int pularityScoring = Math.toIntExact(githubRepository.getForksCount() * fork_relevance_factor +
                                          githubRepository.getStargazersCount() * stargazers_relevance_factor +
                                          ChronoUnit.MINUTES.between(githubRepository.getUpdatedAt().toLocalDateTime(), LocalDateTime.now()));
    githubRepository.setPopularityScoring(pularityScoring);
  }

  private List<GithubRepository>orderReturnedRepositoriesByPopularity(List<GithubRepository>repositoryResultList) {
    return repositoryResultList.stream().sorted(Comparator.comparing(GithubRepository::getPopularityScoring).reversed()).toList();
  }
}
