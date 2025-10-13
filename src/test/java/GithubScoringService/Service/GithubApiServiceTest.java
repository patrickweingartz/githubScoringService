package GithubScoringService.Service;

import GithubScoringService.Utils.JsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GithubApiServiceTest {

  @InjectMocks
  private GithubApiService underTest;

  @Mock
  private WebClient webClientMock;

  @Mock
  private JsonUtils jsonUtilsMock;

  private static final String REPOSITORY_QUERY_RESULT = "TestString";

  @SuppressWarnings("rawtypes")
  @BeforeEach
  public void setUp() throws IOException {
    WebClient.RequestHeadersUriSpec uriSpecMock = org.mockito.Mockito.mock(WebClient.RequestHeadersUriSpec.class);
    WebClient.RequestHeadersSpec headersSpecMock = org.mockito.Mockito.mock(WebClient.RequestHeadersSpec.class);
    WebClient.ResponseSpec responseSpecMock = org.mockito.Mockito.mock(WebClient.ResponseSpec.class);

    when(webClientMock.get()).thenReturn(uriSpecMock);
    when(uriSpecMock.uri(org.mockito.ArgumentMatchers.anyString())).thenReturn(headersSpecMock);
    when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
    when(headersSpecMock.headers(any())).thenReturn(headersSpecMock);
    when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
    when(responseSpecMock.bodyToMono(String.class)).thenReturn(reactor.core.publisher.Mono.just(REPOSITORY_QUERY_RESULT));
    when(jsonUtilsMock.parseRemainingCallsForRepositorySearchFromJsonResult(any())).thenReturn(1);
  }

  @Test
  public void remainingRateLimitCallWithBearerCallsAllMocks() throws IOException {
    underTest.getRemainingRateLimitForRepositorySearch("Bearer Testtoken");
  }

  @Test
  public void remainingRateLimitCallWithoutBearerCallsAllMocks() throws IOException {
    underTest.getRemainingRateLimitForRepositorySearch(null);
  }
}