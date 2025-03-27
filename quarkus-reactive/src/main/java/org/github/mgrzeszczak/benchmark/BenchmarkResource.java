package org.github.mgrzeszczak.benchmark;

import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jooq.DSLContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Path("/api/benchmark")
public class BenchmarkResource {

    private final Vertx vertx;

    public BenchmarkResource(Vertx vertx) {
        this.vertx = vertx;
    }

    @Path("/static")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> hello() {
        return Uni.createFrom().item("OK");
    }

    @Path("/file-system-read")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Integer> fileSystemRead() {
//        return Uni.createFrom().<byte[]>item(() -> {
//            try {
//                return Files.readAllBytes(Paths.get("../file.dat"));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }).map(result -> result.length);
        return Uni.createFrom().<Integer>emitter(emitter -> {
            vertx.fileSystem()
                    .readFile("../file.dat")
                    .onSuccess(it -> emitter.complete(it.length()))
                    .onFailure(emitter::fail);
        });
    }

}
