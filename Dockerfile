FROM openjdk:17
ENTRYPOINT ["/usr/bin/sonar-metrics.sh"]

COPY sonar-metrics.sh /usr/bin/sonar-metrics.sh
COPY target/sonar-metrics.jar /usr/share/sonar-metrics/sonar-metrics.jar
