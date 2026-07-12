package org.tbee.webstack.postgres;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class PostgresTestContainer {

    /**
     * Registry of running containers keyed by database name, so that a running
     * application can trigger a dump without holding a reference to the container
     * (which is relevant with Spring DevTools hot-reload, where the launcher's
     * main() is not re-run but the application classloader is recreated).
     */
    private static final Map<String, PostgreSQLContainer<?>> RUNNING = new ConcurrentHashMap<>();

    private String containerName = "postgres:18";
    private String database;
    private String username;
    private String password;
    private boolean preventDoubleStart = true;
    private boolean configureSpringDatasource = true;
    private Consumer<String> log = System.out::println;
    private File restoreFile = null;
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
            log.accept("- Create dump: pg_dump" +
                    " --host=" + postgreSQLContainer.getHost() +
                    " --port=" + postgreSQLContainer.getFirstMappedPort() +
                    " --username=" + postgreSQLContainer.getUsername() +
                    " --dbname=" + postgreSQLContainer.getDatabaseName() +
                    " > " + postgreSQLContainer.getDatabaseName() + "_" + DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now()) + ".sql");
            log.accept("- Add --schema-only to export without data.");

            // Register so dump(...) can find this container by database name
            RUNNING.put(database, postgreSQLContainer);

            // Restore a database dump first, then run any additional load scripts on top
            if (restoreFile != null) {
                importSqlFile(postgreSQLContainer, restoreFile, "restoring");
            }
            for (File file : loadFiles) {
                importSqlFile(postgreSQLContainer, file, "loading");
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

    private void importSqlFile(PostgreSQLContainer<?> postgreSQLContainer, File file, String action) throws Exception {
        log.accept(action.substring(0, 1).toUpperCase() + action.substring(1) + " " + file);
        String containerPath = "/tmp/import.sql";
        postgreSQLContainer.copyFileToContainer(MountableFile.forHostPath(Path.of(file.getAbsolutePath())), containerPath);
        var result = postgreSQLContainer.execInContainer("psql", "-U", username, "-d", database, "-f", containerPath);
        if (result.getExitCode() != 0) {
            throw new IllegalStateException("Error " + action + " " + file + ":\n" + result.getStderr());
        }
    }

    // =================================
    // DUMP

    /**
     * Create a dump of the running container's database using pg_dump run inside the container,
     * then copy the resulting file out to the given target on the host.
     * <p>
     * This is a static method (using the single running container) so that a running application
     * can trigger a dump without holding a reference to the container, which is relevant with
     * Spring DevTools hot-reload where the launcher's main() is not re-run but the application
     * classloader is recreated.
     * <p>
     * Must be called after {@link #start()}.
     */
    public static File dump(File target) {
        return dump(target, System.out::println);
    }

    /**
     * Create a timestamped dump ({@code <database>_yyyyMMdd_HHmmss.sql}) in the current working directory.
     */
    public static File dump() {
        return dump(defaultDumpFile(runningContainer().getDatabaseName()));
    }

    private static PostgreSQLContainer<?> runningContainer() {
        return RUNNING.values().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No running Postgres container registered; call start() first."));
    }

    private static File dump(File target, Consumer<String> log) {
        PostgreSQLContainer<?> postgreSQLContainer = runningContainer();
        try {
            String containerPath = "/tmp/dump.sql";
            log.accept("Dumping database '" + postgreSQLContainer.getDatabaseName() + "' to " + target.getAbsolutePath());
            var result = postgreSQLContainer.execInContainer("pg_dump",
                    "-U", postgreSQLContainer.getUsername(),
                    "-d", postgreSQLContainer.getDatabaseName(),
                    "-f", containerPath);
            if (result.getExitCode() != 0) {
                throw new IllegalStateException("pg_dump failed:\n" + result.getStderr());
            }
            File parent = target.getAbsoluteFile().getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            postgreSQLContainer.copyFileFromContainer(containerPath, target.getAbsolutePath());
            log.accept("Dump written to " + target.getAbsolutePath());
            return target;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static File defaultDumpFile(String database) {
        return new File(database + "_" + DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now()) + ".sql");
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

    public File restore() {
        return restoreFile;
    }

    /**
     * Register a dump file to restore into the database on {@link #start()}.
     * Restore files are imported before any {@link #load(File)} scripts.
     */
    public PostgresTestContainer restore(File file) {
        if (!file.exists()) {
            throw new IllegalStateException("file does not exist: " + file.getAbsolutePath());
        }
        this.restoreFile = file;
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
