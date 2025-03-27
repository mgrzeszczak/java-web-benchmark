package io.github.mgrzeszczak.benchmark.spring_mvc_virtual_threads;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/benchmark")
public class BenchmarkController {

    @GetMapping("/static")
    public String staticResponse() {
        return "hello world";
    }

    @GetMapping("/file-system-read")
    public int fileSystemRead() throws IOException {
        return Files.readAllBytes(Paths.get("file.dat")).length;
    }

}
