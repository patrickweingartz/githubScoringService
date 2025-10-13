package GithubScoringService.Service;

import GithubScoringService.Utils.JsonUtils;
import githubScoringService.model.GithubRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class GithubCallDistributionService {

  private final GithubApiService githubApiService;
  private final JsonUtils jsonUtils;
  private Integer remainingCalls;

  public GithubCallDistributionService(GithubApiService githubApiService, JsonUtils jsonUtils) {
    this.githubApiService = githubApiService;
    this.jsonUtils = jsonUtils;
  }

  private Integer determineNumberOfPagesIterateForSearchresults(String authorizationToken) throws IOException {
    return Math.min(githubApiService.getRemainingRateLimitForRepositorySearch(authorizationToken), 10);
  }

  public Mono<List<GithubRepository>> distributeApiCallsByRemainingCallLimit(String authorizationToken, String repositoryLanguage, LocalDate minCreationDateOfRepository) throws IOException {
    Integer numberOfCallsRemaining = determineNumberOfPagesIterateForSearchresults(authorizationToken);
    Flux<Integer> numberOfParallelCalls = Flux.range(1, numberOfCallsRemaining);
    return numberOfParallelCalls.flatMap(pageToReqeust
                                             -> githubApiService.getFilteredAndSortedRepositories(authorizationToken,
                                                                                                  repositoryLanguage,
                                                                                                  minCreationDateOfRepository,
                                                                                                  pageToReqeust)
                                             .subscribeOn(Schedulers.boundedElastic())
                                             .map(json -> {
                                               return jsonUtils.parseGithubRepositoriesFromJsonResult(json);
                                             }).onErrorResume(e -> Mono.just(List.of())),
                                         numberOfCallsRemaining)
        .flatMap(Flux::fromIterable)
        .collectList();
  }
}