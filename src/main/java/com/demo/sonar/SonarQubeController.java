package com.demo.sonar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/sonarqube")
public class SonarQubeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SonarQubeController.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SonarQubeService sonarQubeService;

    @GetMapping("/measures")
    public String getComponentMeasures(@RequestParam String componentKey, @RequestParam String metrics) {
        LOGGER.info("Get component measures for componentKey: {}, metrics: {}", componentKey, metrics);
        String result = sonarQubeService.getComponentMeasures(componentKey, metrics);
        LOGGER.info("Result: {}", result);
        return result;
    }

    @GetMapping("/measures/csv")
    public String getComponentMeasuresCsv(@RequestParam String componentKey, @RequestParam String metrics) {
        LOGGER.info("Get component measures for CSV output for componentKey: {}, metrics: {}", componentKey, metrics);
        String jsonResponse = sonarQubeService.getComponentMeasures(componentKey, metrics);
        return convertJsonToCsv(jsonResponse);
    }

    @GetMapping("/metrics")
    public String listAvailableMetrics() {
        LOGGER.info("List available metrics");
        String result = sonarQubeService.listAvailableMetrics();
        LOGGER.info("Result: {}", result);
        return result;
    }

    private String convertJsonToCsv(String jsonResponse) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            JsonNode measures = jsonNode.path("component").path("measures");
            TreeMap<String, String> sortedMetrics = new TreeMap<>(); // TreeMap to sort keys automatically

            for (JsonNode measure : measures) {
                String metric = measure.path("metric").asText();
                String value = measure.path("value").asText();
                sortedMetrics.put(metric, value); // Adding to TreeMap
            }

            StringWriter writer = new StringWriter();
            // Write headers
            for (String key : sortedMetrics.keySet()) {
                writer.append(key).append(",");
            }
            writer.replace(writer.length() - 1, writer.length(), "\n"); // Replace last comma with newline

            // Write values
            for (String value : sortedMetrics.values()) {
                writer.append(value).append(",");
            }
            writer.replace(writer.length() - 1, writer.length(), "\n"); // Replace last comma with newline

            return writer.toString();
        } catch (Exception e) {
            LOGGER.error("Error converting JSON to CSV", e);
            return "Error converting JSON to CSV";
        }
    }
}
