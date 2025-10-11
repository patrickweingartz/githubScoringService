package GithubScoringService.Controller;

import GithubScoringService.Service.GithubScoringService;
import githubScoringService.api.GetGithubReposSortedByScoreApi;
import githubScoringService.model.GithubRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
public class GithubScoringController implements GetGithubReposSortedByScoreApi {

  private final GithubScoringService githubScoringService;

  public GithubScoringController(GithubScoringService githubScoringService) {
    this.githubScoringService = githubScoringService;
  }

  @Override
  public ResponseEntity<List<GithubRepository>> getGithubReposSortedByScore(String repositoryLanguage, LocalDate minCreationDateOfRepository) {
    List<GithubRepository> gitHubRespositoryList = null;
    try {
      gitHubRespositoryList = githubScoringService.getGithubRepositoriesByDateAndLanguage(repositoryLanguage, minCreationDateOfRepository);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    return ResponseEntity.status(HttpStatus.OK).body(gitHubRespositoryList);
  }
}

