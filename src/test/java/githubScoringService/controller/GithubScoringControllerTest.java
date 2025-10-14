package githubScoringService.controller;

import githubScoringService.exception.NoParamsForSearchException;
import githubScoringService.service.GithubScoringService;
import githubScoringService.utils.TestdataInitializer;
import githubScoringService.model.GithubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GithubScoringControllerTest {

  @InjectMocks
  private GithubScoringController underTest;

  @Mock
  private GithubScoringService githubScoringService;

  private List<GithubRepository> githubRepositoryList;

  @BeforeEach
  public void initializeGithubRespositoryList() {
    githubRepositoryList = TestdataInitializer.generateRepositryListWithDataForTesting();
  }

  @Test
  public void should_returnRepositoryResultAsString_when_called() throws IOException {
    when(githubScoringService.getRepositoriesSortedByPopularity(any(), any(), any())).thenReturn(githubRepositoryList);
    assertThat(underTest.getGithubReposSortedByPopularityScore("", "java", "null")).isEqualTo(ResponseEntity.status(HttpStatus.OK).body(githubRepositoryList));
  }

  @Test
  public void should_returnParameterError_when_calledWithoutSearchparam() throws IOException {
    assertThrows(NoParamsForSearchException.class, () -> {
      underTest.getGithubReposSortedByPopularityScore("", "null", "null");
    });
  }
}