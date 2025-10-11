package GithubScoringService.Service;

import GithubScoringService.Utils.JsonUtils;
import githubScoringService.model.GithubRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
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

    return githubRepositories;
  }
}
