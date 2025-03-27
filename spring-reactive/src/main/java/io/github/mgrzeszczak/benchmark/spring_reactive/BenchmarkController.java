package io.github.mgrzeszczak.benchmark.spring_reactive;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("/api/benchmark")
public class BenchmarkController {

    @GetMapping("/static")
    public Mono<String> staticResponse() {
        return Mono.just("hello world");
    }

}
