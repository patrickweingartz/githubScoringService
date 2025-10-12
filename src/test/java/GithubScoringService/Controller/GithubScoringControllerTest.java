package GithubScoringService.Controller;

import GithubScoringService.Service.GithubScoringService;
import GithubScoringService.Utils.TestdataInitializer;
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
  public void getGithubRepositoriesByDateAndLanguageReturnsStringResult() throws IOException {
    when(githubScoringService.getGithubRepositoriesByDateAndLanguage(any(), any())).thenReturn(githubRepositoryList);
    assertThat(underTest.getGithubReposSortedByScore(any(), any())).isEqualTo(ResponseEntity.status(HttpStatus.OK).body(githubRepositoryList));
  }
}