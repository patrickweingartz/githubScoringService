package GithubScoringService.Controller;

import GithubScoringService.GithubScoringServiceApplication;
import GithubScoringService.Service.GithubScoringService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

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

  private static final String REPOSITORY_QUERY_RESULT = "TestString";

  @Test
  public void getGithubRepositoriesByDateAndLanguageReturnsStringResult() {
    when(githubScoringService.getGithubRepositoriesByDateAndLanguage(any(), any())).thenReturn(REPOSITORY_QUERY_RESULT);
    assertThat(underTest.getGithubReposSortedByScore(any(), any())).isEqualTo(ResponseEntity.status(HttpStatus.OK).body(REPOSITORY_QUERY_RESULT));
  }
}