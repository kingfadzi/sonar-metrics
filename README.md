# sonar-metrics
```
curl -s "http://localhost:8080/sonarqube/measures?componentKey=PROJECT_KEY&metrics=ncloc,bugs,comment_lines_density,vulnerabilities,security_hotspots,reliability_rating,sqale_index,code_smells,coverage,duplicated_lines_density,duplicated_blocks" | jq -r '
  .component.measures |
  map({(.metric): .value}) |
  add |
  [keys_unsorted[], values[]] |
  @csv
' > output.csv
```
fkat file

```
jq -r '.component.measures | map({(.metric): .value}) | add | [keys_unsorted[], values[]] | @csv' input.json > output.csv
```
