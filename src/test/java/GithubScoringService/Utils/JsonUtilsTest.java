package GithubScoringService.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import githubScoringService.model.GithubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JsonUtilsTest {

  @InjectMocks
  private JsonUtils underTest;

  private ObjectMapper objectMapper;

  private String JsonResult;

  @BeforeEach
  public void setup() {
    objectMapper = new ObjectMapper();
    underTest = new JsonUtils(objectMapper);

    JsonResult = TestdataInitializer.generateJsonNodeWithOneItem().toString();
  }

  @Test
  public void parseGithubRepositoriesFromJsonResultReturnsGithubRepositoryList() throws IOException {
    List<GithubRepository> githubRepositoryList = underTest.parseGithubRepositoriesFromJsonResult(JsonResult);
    assertThat(githubRepositoryList.size()).isEqualTo(1);
    assertThat(githubRepositoryList.getFirst().getFullName()).isEqualTo("TestRepository");
    assertThat(githubRepositoryList.getFirst().getNumberOfForks()).isEqualTo(3);
    assertThat(githubRepositoryList.getFirst().getNumberOfStars()).isEqualTo(5);
    assertThat(githubRepositoryList.getFirst().getRecencyOfUpdates()).isEqualTo(444);
    assertThat(githubRepositoryList.getFirst().getPopularityScoring()).isEqualTo(55432);
    assertThat(githubRepositoryList.getFirst().getId()).isEqualTo("1234");
  }
}