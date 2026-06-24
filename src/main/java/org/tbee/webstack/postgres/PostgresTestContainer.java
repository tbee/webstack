package org.tbee.webstack.postgres;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class PostgresTestContainer {

    private String containerName = "postgres:18";
    private String database;
    private String username;
    private String password;
    private boolean preventDoubleStart = true;
    private boolean configureSpringDatasource = true;
    private Consumer<String> log = System.out::println;
    private final List<File> loadFiles = new ArrayList<>();

    public record Info(String containerId, String url) {}

    public Info start() {
        try {
            String startMarker = PostgresTestContainer.class.getName() + "@" + database;
            if (preventDoubleStart && System.getProperty(startMarker) != null) {
                log.accept("Postgres container is already running");
                return null;
            }

            // Start postgres container
            PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(containerName)
                    .withDatabaseName(database)
                    .withUsername(username)
                    .withPassword(password)
                    .withLogConsumer(frame -> {
                        String line = frame.getUtf8String();
                        if (line != null && !line.isEmpty()) {
                            log.accept("{postgres " + database + "} " + line.replaceAll("\\r?\\n", ""));
                        }
                    })
                    .waitingFor(org.testcontainers.containers.wait.strategy.Wait.forListeningPort());
            postgreSQLContainer.start();
            log.accept("Postgres container started on " + postgreSQLContainer.getJdbcUrl());

            // Restore a database, run an init script, ...
            for (File file : loadFiles) {
                log.accept("Importing " + file);
                String containerPath = "/tmp/dump.sql";
                postgreSQLContainer.copyFileToContainer(MountableFile.forHostPath(Path.of(file.getAbsolutePath())), containerPath);
                postgreSQLContainer.execInContainer("psql", "-U", username, "-d", database, "-f", containerPath);
            }

            // Setup spring
            if (configureSpringDatasource) {
                log.accept("Autoconfiguring spring datasource:");
                log.accept("* spring.datasource.url=" + postgreSQLContainer.getJdbcUrl());
                log.accept("* spring.datasource.username=" + postgreSQLContainer.getUsername());
                log.accept("* spring.datasource.password=" + postgreSQLContainer.getPassword());
                log.accept("* spring.datasource.driver-class-name=" + postgreSQLContainer.getDriverClassName());

                System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
                System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
                System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
                System.setProperty("spring.datasource.driver-class-name", postgreSQLContainer.getDriverClassName());
            }

            // Mark as started
            System.setProperty(startMarker, "started");

            return new Info(postgreSQLContainer.getContainerId(), postgreSQLContainer.getJdbcUrl());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // =================================


    public String containerName() {
        return containerName;
    }

    public PostgresTestContainer containerName(String containerName) {
        this.containerName = containerName;
        return this;
    }

    public String database() {
        return database;
    }

    public PostgresTestContainer database(String database) {
        this.database = database;
        return this;
    }

    public String username() {
        return username;
    }

    public PostgresTestContainer username(String username) {
        this.username = username;
        return this;
    }

    public String password() {
        return password;
    }

    public PostgresTestContainer password(String password) {
        this.password = password;
        return this;
    }

    public boolean preventDoubleStart() {
        return preventDoubleStart;
    }

    public PostgresTestContainer preventDoubleStart(boolean preventDoubleStart) {
        this.preventDoubleStart = preventDoubleStart;
        return this;
    }

    public Consumer<String> log() {
        return log;
    }

    public PostgresTestContainer log(Consumer<String> log) {
        this.log = log;
        return this;
    }

    public List<File> load() {
        return Collections.unmodifiableList(loadFiles);
    }

    public PostgresTestContainer load(File file) {
        if (!file.exists()) {
            throw new IllegalStateException("file does not exist: " + file.getAbsolutePath());
        }
        this.loadFiles.add(file);
        return this;
    }

    public boolean configureSpringDatasource() {
        return configureSpringDatasource;
    }

    public PostgresTestContainer configureSpringDatasource(boolean configureSpringDatasource) {
        this.configureSpringDatasource = configureSpringDatasource;
        return this;
    }
}
