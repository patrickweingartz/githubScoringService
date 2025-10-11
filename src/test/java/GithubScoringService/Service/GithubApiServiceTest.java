package GithubScoringService.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.springframework.web.reactive.function.client.WebClient;

@ExtendWith(MockitoExtension.class)
class GithubApiServiceTest {

  @InjectMocks
  private GithubApiService underTest;

  @Mock
  private WebClient webClientMock;

  private static final String REPOSITORY_QUERY_RESULT = "TestString";

  @SuppressWarnings("rawtypes")
  @BeforeEach
  public void setUp() {
    WebClient.RequestHeadersUriSpec uriSpecMock = org.mockito.Mockito.mock(WebClient.RequestHeadersUriSpec.class);
    WebClient.RequestHeadersSpec headersSpecMock = org.mockito.Mockito.mock(WebClient.RequestHeadersSpec.class);
    WebClient.ResponseSpec responseSpecMock = org.mockito.Mockito.mock(WebClient.ResponseSpec.class);

    when(webClientMock.get()).thenReturn(uriSpecMock);
    when(uriSpecMock.uri(org.mockito.ArgumentMatchers.anyString())).thenReturn(headersSpecMock);
    when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
    when(responseSpecMock.bodyToMono(String.class)).thenReturn(reactor.core.publisher.Mono.just(REPOSITORY_QUERY_RESULT));
  }

  @Test
  public void githubCallViaWebclient() {
    assertThat(underTest.getFilteredAndSortedRepositories("java", LocalDate.of(2025, 1, 1))).isEqualTo(REPOSITORY_QUERY_RESULT);
  }
}