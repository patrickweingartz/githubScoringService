package GithubScoringService.Controller;

import GithubScoringService.GithubScoringServiceApplication;
import GithubScoringService.Service.GithubScoringService;
import GithubScoringService.Utils.TestdataInitializer;
import githubScoringService.model.GithubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {GithubScoringServiceApplication.class})
class GithubScoringControllerTest {

  @InjectMocks
  private GithubScoringController underTest;

  @Mock
  private GithubScoringService githubScoringService;

  private List<GithubRepository> ggithubRepositoryList;

  @BeforeEach
  public void initializeGithubRespositoryList() {
    ggithubRepositoryList = TestdataInitializer.generateRepositryListWithDataForTesting();
  }

  @Test
  public void getGithubRepositoriesByDateAndLanguageReturnsStringResult() throws IOException {
    when(githubScoringService.getGithubRepositoriesByDateAndLanguage(any(), any())).thenReturn(ggithubRepositoryList);
    assertThat(underTest.getGithubReposSortedByScore(any(), any())).isEqualTo(ResponseEntity.status(HttpStatus.OK).body(ggithubRepositoryList));
  }
}