package githubScoringService.service;

import githubScoringService.utils.TestdataInitializer;
import githubScoringService.model.GithubRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class GithubScoringServiceTest {

  @InjectMocks
  private GithubScoringService underTest;

  @Mock
  private GithubCallDistributionService githubCallDistributionServiceMock;

  @Test
  public void should_returnSortedRepositories_when_called() throws IOException {
    Mockito.when(githubCallDistributionServiceMock.distributeApiCallsByRemainingCallLimit(any(), any(), any())).thenReturn(Mono.just(TestdataInitializer.generateRepositryListWithDataForTesting()));

    List<GithubRepository> githubRepositoryListResponse = underTest.getRepositoriesSortedByPopularity("Testtoken", "java", LocalDate.of(2025, 1, 1));

    assertThat(githubRepositoryListResponse.getFirst().getFullName()).isEqualTo("TestRepository");
    assertThat(githubRepositoryListResponse.getFirst().getForksCount()).isEqualTo(12);
    assertThat(githubRepositoryListResponse.getFirst().getStargazersCount()).isEqualTo(5);
    assertThat(githubRepositoryListResponse.getFirst().getUpdatedAt()).isEqualTo("2025-01-01T00:00Z");
    assertThat(githubRepositoryListResponse.getFirst().getId()).isEqualTo("1234");
    assertThat(githubRepositoryListResponse.getFirst().getPopularityScoring()).isNotNull();
  }
}