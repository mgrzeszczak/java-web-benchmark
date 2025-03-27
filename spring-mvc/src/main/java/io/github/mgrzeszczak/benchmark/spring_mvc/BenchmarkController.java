package io.github.mgrzeszczak.benchmark.spring_mvc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/benchmark")
public class BenchmarkController {

    @GetMapping("/static")
    public String staticResponse() {
        return "hello world";
    }

}
