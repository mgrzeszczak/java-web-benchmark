wrk -t12 -c400 -d30s http://127.0.0.1:8080/api/benchmark/static &
wait
wrk -t12 -c400 -d30s http://127.0.0.1:8080/api/benchmark/static
wait
wrk -t12 -c400 -d30s http://127.0.0.1:8080/api/benchmark/static