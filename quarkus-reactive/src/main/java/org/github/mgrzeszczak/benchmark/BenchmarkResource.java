package org.github.mgrzeszczak.benchmark;

import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/api/benchmark")
public class BenchmarkResource {

    private final Vertx vertx;
    private final String filePath;

    public BenchmarkResource(
            @ConfigProperty(name = "benchmark.file") String filePath,
            Vertx vertx
    ) {
        this.vertx = vertx;
        this.filePath = filePath;
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
                    .readFile(filePath)
                    .onSuccess(it -> emitter.complete(it.length()))
                    .onFailure(emitter::fail);
        });
    }

}
