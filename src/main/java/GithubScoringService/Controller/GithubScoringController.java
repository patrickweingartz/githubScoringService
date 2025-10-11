package GithubScoringService.Controller;

import GithubScoringService.Service.GithubScoringService;
import githubScoringService.api.GetGithubReposSortedByScoreApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class GithubScoringController implements GetGithubReposSortedByScoreApi {

  private final GithubScoringService githubScoringService;

  public GithubScoringController(GithubScoringService githubScoringService) {
    this.githubScoringService = githubScoringService;
  }

  @Override
  public ResponseEntity<String> getGithubReposSortedByScore(String repositoryLanguage, LocalDate minCreationDateOfRepository) {
    String gitHubRespositoryList =  githubScoringService.getGithubRepositoriesByDateAndLanguage(repositoryLanguage, minCreationDateOfRepository);
    return ResponseEntity.status(HttpStatus.OK).body(gitHubRespositoryList);
  }
}

