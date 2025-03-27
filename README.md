# java-web-benchmark

## Docker stats
```
docker stats --no-stream --format "{{json .}}"
```

## Running benchmark
```
wrk -t12 -c400 -d30s http://127.0.0.1:8080/api/benchmark/static
```
