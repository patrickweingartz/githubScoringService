package githubScoringService.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import githubScoringService.model.GithubRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class JsonUtils {

  private ObjectMapper objectMapper;

  public JsonUtils(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
  }

  public List<GithubRepository> parseGithubRepositoriesFromJsonResult(String json){
    try {
      if (objectMapper.readTree(json).get("items") != null) {
        return objectMapper.readValue(objectMapper.readTree(json).get("items").toString(),
                                                    new TypeReference<List<GithubRepository>>() {
                                                    });
      }
      else {
        return List.of();
      }
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public Integer parseRemainingCallsForRepositorySearchFromJsonResult(String json) throws IOException {
    JsonNode root = objectMapper.readTree(json);
    return root.path("resources").path("search").path("remaining").asInt();
  }
}
