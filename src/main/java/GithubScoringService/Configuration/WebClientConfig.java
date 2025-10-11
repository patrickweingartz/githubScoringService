package GithubScoringService.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Value("${githubBaseUrl}")
  private String githubBaseUrl;

  @Value("${maxByteCountInGithubApiResponse}")
  private Integer maxByteCountInGithubApiResponse;

  @Bean
  public WebClient webClient() {
    return WebClient.builder()
        .baseUrl(githubBaseUrl)
        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(maxByteCountInGithubApiResponse))
        .build();
  }
}