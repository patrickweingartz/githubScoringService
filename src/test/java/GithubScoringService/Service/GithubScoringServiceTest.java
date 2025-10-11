package GithubScoringService.Service;

import GithubScoringService.Utils.JsonUtils;
import GithubScoringService.Utils.TestdataInitializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

  private final String JSON_RESULT = "id: 1234, full_name: TestRepository, score: 12345, numberOfStars: 5, numberOfForks: 3, recencyOfUpdates: 444, popularityScoring: 55432";

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