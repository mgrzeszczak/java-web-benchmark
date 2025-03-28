package io.github.mgrzeszczak.benchmark.spring_reactive;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/benchmark")
public class BenchmarkController {

    private final DSLContext dslContext;
    private final String filePath;

    public BenchmarkController(DSLContext dslContext, @Value("${benchmark.file}") String filePath) {
        this.dslContext = dslContext;
        this.filePath = filePath;
    }

    @GetMapping("/static")
    public Mono<String> staticResponse() {
        return Mono.just("OK");
    }

    @GetMapping("/file-system-read")
    public Mono<Integer> fileSystemRead() throws IOException {
        return Mono.fromCallable(() -> Files.readAllBytes(Paths.get(filePath)).length)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/db-read")
    @Transactional
    public Mono<Integer> dbRead() {
        return Mono.from(dslContext.select(DSL.val(1)))
                .map(Record1::component1);
    }

}
