package org.github.mgrzeszczak.benchmark;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

public class DbConfiguration {

    @ConfigProperty(name = "db.host")
    String host;
    @ConfigProperty(name = "db.port")
    int port;
    @ConfigProperty(name = "db.username")
    String username;
    @ConfigProperty(name = "db.password")
    String password;
    @ConfigProperty(name = "db.database")
    String database;

    @Produces
    @ApplicationScoped
    public DSLContext dslContext(ConnectionFactory connectionFactory) {
        return DSL.using(connectionFactory);
    }

    @Produces
    @ApplicationScoped
    public ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host(host)
                        .port(port)
                        .username(username)
                        .password(password)
                        .database(database)
                        .build()
        );
    }

}
