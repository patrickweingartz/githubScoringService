package GithubScoringService.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import githubScoringService.model.GithubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JsonUtilsTest {

  private JsonUtils underTest;

  private ObjectMapper objectMapper;

  private String JsonResult;

  @BeforeEach
  public void setup() {
    objectMapper = new ObjectMapper();
    underTest = new JsonUtils(objectMapper);
  }

  @Test
  public void parseGithubRepositoriesFromJsonResultReturnsGithubRepositoryList() throws IOException {
    JsonResult = TestdataInitializer.generateJsonNodeWithOneItem().toString();

    List<GithubRepository> githubRepositoryList = underTest.parseGithubRepositoriesFromJsonResult(JsonResult);
    assertThat(githubRepositoryList.size()).isEqualTo(1);
    assertThat(githubRepositoryList.getFirst().getFullName()).isEqualTo("TestRepository");
    assertThat(githubRepositoryList.getFirst().getForksCount()).isEqualTo(3);
    assertThat(githubRepositoryList.getFirst().getStargazersCount()).isEqualTo(5);
    assertThat(githubRepositoryList.getFirst().getUpdatedAt()).isEqualTo(OffsetDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC));
    assertThat(githubRepositoryList.getFirst().getPopularityScoring()).isEqualTo(55432);
    assertThat(githubRepositoryList.getFirst().getId()).isEqualTo("1234");
  }

  @Test
  public void parseGithubRepositoriesFromJsonResultWithoutItemsReturnsEmptyList() throws IOException {
    JsonResult = TestdataInitializer.generateJsonNodeWithoutItems().toString();

    List<GithubRepository> githubRepositoryList = underTest.parseGithubRepositoriesFromJsonResult(JsonResult);
    assertThat(githubRepositoryList.size()).isEqualTo(0);
    assertThat(githubRepositoryList.isEmpty()).isEqualTo(true);
  }
}