#!/usr/bin/bash
# Define arrays for apps, URLs, and paths
APPS=("spring-reactive" "spring-mvc-virtual-threads" "spring-mvc" "quarkus-reactive")
URLS=("http://localhost:8080" "http://localhost:8081" "http://localhost:8082" "http://localhost:8083")
JMX_URLS=("localhost:5050" "localhost:5051" "localhost:5052" "localhost:5053")
#PATHS=("/api/benchmark/static" "/api/benchmark/file-system-read")
PATHS=("/api/benchmark/file-system-read")
# Define scenarios
#SCENARIOS=("static_12_400" "file_read_12_400")
SCENARIOS=("file_read")

# Create reports directory if it doesn't exist
mkdir -p reports
final_report="reports/report.md"
> $final_report

# Loop over each app using index
for app_index in "${!APPS[@]}"; do
    APP="${APPS[$app_index]}"
    URL="${URLS[$app_index]}"  # Get URL corresponding to the app
    JMX_URL="${JMX_URLS[$app_index]}"

    # Loop over each scenario using index
    for scenario_index in "${!SCENARIOS[@]}"; do
        SCENARIO="${SCENARIOS[$scenario_index]}"
        SCENARIO_PATH="${PATHS[$scenario_index]}"  # Get path corresponding to the scenario

        echo "Running scenario: $SCENARIO for app: $APP at $URL$SCENARIO_PATH"

        # Create the report name
        report_name="${APP}_${SCENARIO}"

        # Run monitoring
        ./jmx.py $JMX_URL "reports/$report_name.csv" &
        monitoring_pid=$!
        # Run wrk benchmark
        wrk -t12 -c100 -d60s "$URL$SCENARIO_PATH" > "reports/$report_name.txt" &
        wrk_pid=$!

        # Wait for the wrk process to finish
        wait $wrk_pid
        kill -2 $monitoring_pid
        wait $monitoring_pid

        ./plot.py $report_name

        echo "## $report_name" >> $final_report
        echo '```' >> $final_report
        cat "reports/$report_name.txt" >> $final_report
        echo '```' >> $final_report
        echo "![CPU/Heap Graph]($report_name.png)" >> $final_report

    done
done