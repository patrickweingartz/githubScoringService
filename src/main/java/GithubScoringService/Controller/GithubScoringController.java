package GithubScoringService.Controller;

import GithubScoringService.Service.GithubScoringService;
import githubScoringService.api.GetGithubReposSortedByScoreApi;
import githubScoringService.model.GithubRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@RestController
public class GithubScoringController implements GetGithubReposSortedByScoreApi {

  private final GithubScoringService githubScoringService;

  public GithubScoringController(GithubScoringService githubScoringService) {
    this.githubScoringService = githubScoringService;
  }

  @Override
  public ResponseEntity<List<GithubRepository>> getGithubReposSortedByScore(@RequestHeader(value = "authorizationToken", required = false)
                                                                            String authorizationToken,
                                                                            @RequestHeader(value = "repositoryLanguage", required = false)
                                                                            String repositoryLanguage,
                                                                            @RequestHeader(value = "minCreationDateOfRepository", required = false)
                                                                            LocalDate minCreationDateOfRepository) {
    List<GithubRepository> gitHubRespositoryList;
    try{
      gitHubRespositoryList = githubScoringService.getGithubRepositoriesByDateAndLanguage(authorizationToken, repositoryLanguage, minCreationDateOfRepository);
      List<GithubRepository> gitHubRespositoryListSortedByPopularity = gitHubRespositoryList.stream().sorted(Comparator.comparing(GithubRepository::getPopularityScoring).reversed()).toList();
      return ResponseEntity.status(HttpStatus.OK).body(gitHubRespositoryListSortedByPopularity);
    }catch(IOException ioException){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }
}

