package githubScoringService.controller;

import githubScoringService.api.GithubScoringServiceApi;
import githubScoringService.exception.NoParamsForSearchException;
import githubScoringService.model.ErrorResponse;
import githubScoringService.model.GithubRepository;
import githubScoringService.service.GithubScoringService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
public class GithubScoringController implements GithubScoringServiceApi {

  private final GithubScoringService githubScoringService;

  public GithubScoringController(GithubScoringService githubScoringService) {
    this.githubScoringService = githubScoringService;
  }

  @Override
  @GetMapping("/getGithubReposSortedByPopularityScore/{repositoryLanguage}/{minCreationDateOfRepository}")
  public ResponseEntity<List<GithubRepository>> getGithubReposSortedByPopularityScore(@RequestHeader(value = "authorizationToken", required = false)
                                                                                      String authorizationToken,
                                                                                      @PathVariable(value = "repositoryLanguage", required = false)
                                                                                      String repositoryLanguagePath,
                                                                                      @PathVariable(value = "minCreationDateOfRepository", required = false)
                                                                                      String minCreationDateOfRepositoryPath) {

    String repositoryLanguage = "null".equals(repositoryLanguagePath) ? null : repositoryLanguagePath;
    LocalDate minCreationDateOfRepository = "null".equals(minCreationDateOfRepositoryPath) ? null : LocalDate.parse(minCreationDateOfRepositoryPath);

    if (repositoryLanguage == null && minCreationDateOfRepository == null) {
      throw new NoParamsForSearchException("The Github-API needs at least one Searchparameter to perform a repository search!");
    }
    List<GithubRepository> gitHubRespositoryList;
    try {
      gitHubRespositoryList = githubScoringService.getRepositoriesSortedByPopularity(authorizationToken, repositoryLanguage, minCreationDateOfRepository);
      return ResponseEntity.status(HttpStatus.OK).body(gitHubRespositoryList);
    }
    catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Collections.emptyList());
    }
  }

  @ExceptionHandler(NoParamsForSearchException.class)
  public ResponseEntity<ErrorResponse> handleNoParamsException(NoParamsForSearchException noParamException) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setError("Parameter Error");
    errorResponse.setMessage(noParamException.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }
}

