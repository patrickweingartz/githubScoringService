package GithubScoringService.Service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class GithubScoringServiceTest {

  @InjectMocks
  private GithubScoringService underTest;

  @Mock
  private GithubApiService githubApiServiceMock;

  private static final String REPOSITORY_QUERY_RESULT = "TestString";

  @Test
  public void getGithubRepositoriesByDateAndLanguageReturnsStringResult() {
    Mockito.when(githubApiServiceMock.getFilteredAndSortedRepositories(any(), any())).thenReturn(REPOSITORY_QUERY_RESULT);
    assertThat(underTest.getGithubRepositoriesByDateAndLanguage(any(),any()).equals(REPOSITORY_QUERY_RESULT));
  }
}