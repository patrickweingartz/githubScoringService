package GithubScoringService.Service;

import GithubScoringService.Utils.JsonUtils;
import GithubScoringService.Utils.TestdataInitializer;
import githubScoringService.model.GithubRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class GithubScoringServiceTest {

  @InjectMocks
  private GithubScoringService underTest;

  @Mock
  private GithubApiService githubApiServiceMock;

  @Mock
  private JsonUtils jsonUtilsMock;

  @Test
  public void getGithubRepositoriesByDateAndLanguageReturnsStringResult() throws IOException {
    List<GithubRepository> githubRepositoryList = TestdataInitializer.generateRepositryListWithDataForTesting();

    Mockito.when(githubApiServiceMock.getFilteredAndSortedRepositories(any(), any())).thenReturn("");
    Mockito.when(jsonUtilsMock.parseGithubRepositoriesFromJsonResult(anyString())).thenReturn(githubRepositoryList);

    assertThat(underTest.getGithubRepositoriesByDateAndLanguage("java", LocalDate.of(2025, 1, 1))).isEqualTo(githubRepositoryList);
  }
}