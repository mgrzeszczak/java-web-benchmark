#!/usr/bin/bash
# Define arrays for apps, URLs, and paths
APPS=("spring-reactive" "spring-mvc-virtual-threads" "spring-mvc" "quarkus-reactive")
URLS=("http://localhost:8080" "http://localhost:8081" "http://localhost:8082" "http://localhost:8083")
#PATHS=("/api/benchmark/static" "/api/benchmark/file-system-read")
PATHS=("/api/benchmark/file-system-read")
# Define scenarios
#SCENARIOS=("static_12_400" "file_read_12_400")
SCENARIOS=("file_read")

# Create reports directory if it doesn't exist
mkdir -p reports

# Loop over each app using index
for app_index in "${!APPS[@]}"; do
    APP="${APPS[$app_index]}"
    URL="${URLS[$app_index]}"  # Get URL corresponding to the app

    # Loop over each scenario using index
    for scenario_index in "${!SCENARIOS[@]}"; do
        SCENARIO="${SCENARIOS[$scenario_index]}"
        SCENARIO_PATH="${PATHS[$scenario_index]}"  # Get path corresponding to the scenario

        echo "Running scenario: $SCENARIO for app: $APP at $URL$SCENARIO_PATH"

        # Create the report name
        report_name="${APP}_${SCENARIO}"

        # Run wrk benchmark
        wrk -t12 -c50 -d60s "$URL$SCENARIO_PATH" > "reports/$report_name.txt" &
        child_pid=$!

        # Create a new empty file for stats
        > "reports/$report_name.csv"

        # Collect stats while the benchmark is running
        while kill -0 $child_pid 2>/dev/null; do
            docker stats --no-stream --format "{{json .MemUsage}},{{json .CPUPerc}}" "$APP" | xargs -I {} echo "$(date +%s),{}" >> "reports/$report_name.csv"
            sleep 0.1
        done

        ./plot.py $report_name
        # Wait for the wrk process to finish
        wait $child_pid
    done
done