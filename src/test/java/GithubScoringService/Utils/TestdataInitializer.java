package GithubScoringService.Utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import githubScoringService.model.GithubRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestdataInitializer {

  public static List<GithubRepository> generateRepositryListWithDataForTesting() {
    List<GithubRepository> githubRepositoryList = new ArrayList<>();
    GithubRepository githubRepository = new GithubRepository();

    githubRepository.setId("1234");
    githubRepository.setFullName("TestRepository");
    githubRepository.setScore(new BigDecimal(12345));
    githubRepository.setNumberOfForks(12);
    githubRepository.setNumberOfStars(5);
    githubRepository.setPopularityScoring(55432);
    githubRepository.setRecencyOfUpdates(444);
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
    item.put("score", 12345);
    item.put("numberOfStars", 5);
    item.put("numberOfForks", 3);
    item.put("recencyOfUpdates", 444);
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
}
