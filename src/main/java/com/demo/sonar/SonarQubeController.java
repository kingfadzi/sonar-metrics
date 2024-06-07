package com.demo.sonar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/sonarqube")
public class SonarQubeController {

  private static final Logger LOGGER = LoggerFactory.getLogger(SonarQubeController.class);

  @Autowired
  private SonarQubeService sonarQubeService;

  @GetMapping("/measures")
  public String getComponentMeasures(@RequestParam String componentKey, @RequestParam String metrics) {
    LOGGER.info("Get component measures for componentKey: {}, metrics: {}", componentKey, metrics);
    String result = sonarQubeService.getComponentMeasures(componentKey, metrics);
    LOGGER.info("Result: {}", result);
    return result;
  }

  @GetMapping("/metrics")
  public String listAvailableMetrics() {
    LOGGER.info("List available metrics");
    String result = sonarQubeService.listAvailableMetrics();
    LOGGER.info("Result: {}", result);
    return result;
  }
}
