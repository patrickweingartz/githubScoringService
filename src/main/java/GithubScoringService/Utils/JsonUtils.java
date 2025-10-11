package GithubScoringService.Utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import githubScoringService.model.GithubRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class JsonUtils {
  private  ObjectMapper objectMapper;

  public JsonUtils(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public List<GithubRepository> parseGithubRepositoriesFromJsonResult(String json) throws IOException {
    List<GithubRepository> githubRepositories;
    if (objectMapper.readTree(json).get("items") != null) {
      githubRepositories = objectMapper.readValue(objectMapper.readTree(json).get("items").toString(),
                                                  new TypeReference<List<GithubRepository>>() {
                                                  });
    }
    else {
      return List.of();
    }

    return githubRepositories;
  }

}
