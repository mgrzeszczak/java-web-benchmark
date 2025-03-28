SPRING_REACTIVE_URL="http://localhost:8080"
SPRING_MVC_VIRTUAL_THREADS_URL="http://localhost:8081"
SPRING_MVC_URL="http://localhost:8082"
QUARKUS_REACTIVE_URL="http://localhost:8083"

BENCHMARK_STATIC_PATH="/api/benchmark/static"
BENCHMARK_FILE_READ_PATH="/api/benchmark/file-system-read"

APP=spring-reactive
#SCENARIO=static_12_400
SCENARIO=file_read_12_400

mkdir -p reports

echo "Running scenario $SCENARIO for app $APP"

report_name="$APP$SCENARIO"
wrk -t12 -c400 -d30s "$SPRING_REACTIVE_URL$BENCHMARK_FILE_READ_PATH" > "reports/$report_name.txt"&
child_pid=$!
> $report_name
while kill -0 $child_pid 2>/dev/null; do
    docker stats --no-stream --format "{{json .MemUsage}},{{json .CPUPerc}}" $APP | xargs -I {} echo "$(date +%s),{}" >> "reports/$report_name.csv"
    sleep 1
done

wait