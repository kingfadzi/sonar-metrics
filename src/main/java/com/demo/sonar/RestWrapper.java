package com.demo.sonar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestWrapper {
  private final RestTemplate restTemplate;
  private final String apiToken;

  public RestWrapper(RestTemplate restTemplate, @Value("${sonarqube.api.token}") String apiToken) {
    this.restTemplate = restTemplate;
    this.apiToken = apiToken;
  }

  public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + this.apiToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    return restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
  }
}
