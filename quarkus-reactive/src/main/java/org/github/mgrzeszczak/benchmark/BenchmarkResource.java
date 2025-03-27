package org.github.mgrzeszczak.benchmark;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

@Path("/api/benchmark")
public class BenchmarkResource {

    @Path("/static")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

//        ctx.transactionPublisher(it -> {
//            it.dsl()
//        })
        PostgresqlConnectionFactory connectionFactory = new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host("localhost")
                        .port(5432)
                        .username("postgres")
                        .password("thirumal")
                        .database("sample")
                        .build());

        DSLContext ctx = DSL.using(connectionFactory);

        Uni<Record1<Integer>> result = Uni.createFrom()
                .publisher(ctx.transactionPublisher(it -> it.dsl().select(DSL.val(1))));

//        ctx.transactionPublisher(trx -> trx.dsl())



        return "hello world";
    }

}
