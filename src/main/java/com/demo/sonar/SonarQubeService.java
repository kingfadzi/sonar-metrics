package com.demo.sonar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SonarQubeService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SonarQubeService.class);

  @Value("${sonarqube.url}")
  private String baseUrl;

  @Autowired
  private RestWrapper restWrapper;

  public String getComponentMeasures(String componentKey, String metrics) {
    String url = String.format("%s/measures/component?component=%s&metricKeys=%s", baseUrl, componentKey, metrics);
    LOGGER.info("Getting component measures with URL : {}", url);
    String response = restWrapper.getForEntity(url, String.class).getBody();
    LOGGER.info("Response from SonarQube: {}", response);
    return response;
  }

  public String listAvailableMetrics() {
    String url = baseUrl + "/metrics/search";
    LOGGER.info("Listing available metrics with URL : {}", url);
    String response = restWrapper.getForEntity(url, String.class).getBody();
    LOGGER.info("Available metrics from SonarQube : {}", response);
    return response;
  }
}
