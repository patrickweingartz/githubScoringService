package GithubScoringService.Utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import githubScoringService.model.GithubRepository;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestdataInitializer {

  public static List<GithubRepository> generateRepositryListWithDataForTesting() {
    List<GithubRepository> githubRepositoryList = new ArrayList<>();
    GithubRepository githubRepository = new GithubRepository();

    githubRepository.setId("1234");
    githubRepository.setFullName("TestRepository");
    githubRepository.setForksCount(12);
    githubRepository.setStargazersCount(5);
    githubRepository.setUpdatedAt(OffsetDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC));
    githubRepositoryList.add(githubRepository);

    return githubRepositoryList;
  }

  public static ObjectNode generateJsonNodeWithOneItem() {

    ObjectNode node = JsonNodeFactory.instance.objectNode();
    node.put("total_count", 1);
    node.put("incomplete_results", false);

    ObjectNode item = JsonNodeFactory.instance.objectNode();
    item.put("id", 1234);
    item.put("full_name", "TestRepository");
    item.put("stargazers_count", 5);
    item.put("forks_count", 3);
    item.put("updated_at", String.valueOf(OffsetDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)));
    item.put("popularityScoring", 55432);

    ArrayNode items = JsonNodeFactory.instance.arrayNode();
    items.add(item);

    node.set("items", items);

    return node;
  }

  public static ObjectNode generateJsonNodeWithoutItems() {
    ObjectNode node = JsonNodeFactory.instance.objectNode();
    node.put("total_count", 1);
    node.put("incomplete_results", false);
    return node;
  }

  public static ObjectNode generateJsonNoteFromRemainingRequestsResponse() {
    ObjectNode root = JsonNodeFactory.instance.objectNode();
    ObjectNode resources = JsonNodeFactory.instance.objectNode();

    ObjectNode search = JsonNodeFactory.instance.objectNode();
    search.put("limit", 11);
    search.put("used", 2);
    search.put("remaining", 9);
    search.put("reset", 1760282688);
    resources.set("search", search);
    root.set("resources", resources);

    return root;
  }
}
