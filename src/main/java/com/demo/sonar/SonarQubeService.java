package com.demo.sonar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;

@Service
public class SonarQubeService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SonarQubeService.class);

  @Value("${sonarqube.url}")
  private String sonarUrl;

  @Value("${sonarqube.token:}")
  private String sonarToken;

  @Value("${sonarqube.username:}")
  private String sonarUsername;

  @Value("${sonarqube.password:}")
  private String sonarPassword;

  private final RestTemplate restTemplate;

  public SonarQubeService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public String getComponentMeasures(String componentKey, String metrics) {
    String uri = UriComponentsBuilder.fromHttpUrl(sonarUrl + "/measures/component")
        .queryParam("component", componentKey)
        .queryParam("metricKeys", metrics)
        .toUriString();

    HttpHeaders headers = new HttpHeaders();
    String authHeader = getAuthHeader();
    headers.set("Authorization", authHeader);

    HttpEntity<String> entity = new HttpEntity<>(headers);
    LOGGER.info("Requesting component measures for componentKey: {} with metrics: {}", componentKey, metrics);
    ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
    LOGGER.info("Received response: {}", response.getBody());

    return response.getBody();
  }

  public String listAvailableMetrics() {
    String uri = UriComponentsBuilder.fromHttpUrl(sonarUrl + "/metrics/search").toUriString();

    HttpHeaders headers = new HttpHeaders();
    String authHeader = getAuthHeader();
    headers.set("Authorization", authHeader);

    HttpEntity<String> entity = new HttpEntity<>(headers);
    LOGGER.info("Requesting list of available metrics");
    ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
    LOGGER.info("Received response: {}", response.getBody());

    return response.getBody();
  }

  private String getAuthHeader() {
    if (sonarToken != null && !sonarToken.isEmpty()) {
      LOGGER.info("Using token-based authentication");
      return "Basic " + Base64.getEncoder().encodeToString((sonarToken + ":").getBytes());
    } else if (sonarUsername != null && !sonarUsername.isEmpty() && sonarPassword != null && !sonarPassword.isEmpty()) {
      LOGGER.info("Using username/password-based authentication");
      return "Basic " + Base64.getEncoder().encodeToString((sonarUsername + ":" + sonarPassword).getBytes());
    } else {
      LOGGER.error("Authentication information is missing. Please provide either a token or username and password.");
      throw new IllegalStateException("Authentication information is missing. Please provide either a token or username and password.");
    }
  }
}
