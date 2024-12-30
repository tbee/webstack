package org.tbee.webstack.hsqldb;

import org.apache.commons.io.IOUtils;
import org.hsqldb.persist.HsqlProperties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;

/// Start an embedded HSQLDB.
///
/// Example:
/// ```java
///     new HsqlEmbedded()
///             .port(...)
///             .username(...)
///             .password(...)
///             .database(...)
///             .start();
/// ```
///
/// This takes care of a.o. of access control (default: localhost only), automatic clean shutdown upon JVM exit.
///
public class HsqlEmbedded {
    private static final System.Logger LOG = System.getLogger(HsqlEmbedded.class.getName());

    private Integer port;
    private List<String> databases = new ArrayList<>();
    private String username;
    private String password;
    // Per default only allow connections from localhost
    private List<String> allowConnectionsFrom = new ArrayList<>(List.of("localhost"));

    private org.hsqldb.Server server;
    private final Thread shutdownHook = new Thread(() -> stop());

    public HsqlEmbedded port(int v) {
        this.port = v;
        return this;
    }

    public HsqlEmbedded portFromUrl(String url) {
        if (!url.contains(":hsql:")) {
            port(0);
            return this;
        }
        String datasourceUrlSuffix = url.substring(url.lastIndexOf(":") + 1);
        int port = Integer.parseInt(datasourceUrlSuffix.substring(0, datasourceUrlSuffix.indexOf("/")));
        return port(port);
    }

    public HsqlEmbedded database(String v, String... vs) {
        this.databases.add(v);
        this.databases.addAll(Arrays.asList(vs));
        return this;
    }

    public HsqlEmbedded database(List<String> v) {
        this.databases.addAll(v);
        return this;
    }

    public HsqlEmbedded databaseFromUrl(String url) {
        String datasourceUrlSuffix = url.substring(url.lastIndexOf(":") + 1);
        String dbname = datasourceUrlSuffix.substring(datasourceUrlSuffix.indexOf("/") + 1);
        return database(dbname);
    }

    public HsqlEmbedded username(String v) {
        this.username = v;
        return this;
    }

    public HsqlEmbedded password(String v) {
        this.password = v;
        return this;
    }

    public HsqlEmbedded allowConnectionFrom(String v) {
        this.allowConnectionsFrom.add(v);
        return this;
    }

    public HsqlEmbedded start() {
        assertNotNull(port, "port");
        assertNotEmpty(databases, "databases");
        assertNotNull(username, "username");
        assertNotNull(password, "password");
        assertNotNull(allowConnectionsFrom, "allowConnectionFrom");

        // https://hsqldb.org/doc/guide/dbproperties-chapt.html
        HsqlProperties hsqlProperties = new HsqlProperties();
        hsqlProperties.setProperty("server.port", port);
        hsqlProperties.setProperty("server.acl", createHsqldbAclFile());
        hsqlProperties.setProperty("hsqldb.tx", "mvcc"); // multi version concurrency control (transactions see a consistent view of the data)
        LOG.log(INFO, "Starting HSQL on port: {0}", port);

        // If the dbname is <tenantId> then we will start tenants, otherwise just the one database
        for (int i = 0; i < databases.size(); i++) {
            String database = databases.get(i);
            hsqlProperties.setProperty("server.database." + i, "file:hsqldb/" + database + ";user=" + username + ";password=" + password);
            hsqlProperties.setProperty("server.dbname." + i, database);
            LOG.log(INFO, "Starting HSQL database: {0}", database);
        }

        // Setup the server and start
        server = new org.hsqldb.Server();
        try {
            server.setProperties(hsqlProperties);
        } catch (Exception e) {
            LOG.log(ERROR, e);
            return this;
        }
        server.setLogWriter(new PrintWriter(new ToLogger(s -> LOG.log(INFO, s))));
        server.setErrWriter(new PrintWriter(new ToLogger(s -> LOG.log(ERROR, s))));
        server.start();
        LOG.log(INFO, "HSQL started");

        // Handle clean shutdown
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        return this;
    }

    public HsqlEmbedded stop() {
        if (server == null) {
            LOG.log(INFO, "HSQL server is not started");
            return this;
        }
        if (server.isNotRunning()) {
            LOG.log(WARNING, "HSQL server is not running");
            return this;
        }

        LOG.log(INFO, "HSQL shutting down");
        server.shutdown();
        LOG.log(INFO, "HSQL has shutdown");

        // Shutdown complete
        Runtime.getRuntime().removeShutdownHook(shutdownHook);

        return this;
    }

    private void assertNotNull(Object object, String name) {
        if (object == null) {
            throw new IllegalStateException(name + " is null");
        }
    }

    private void assertNotEmpty(List<?> list, String name) {
        if (list.isEmpty()) {
            throw new IllegalStateException(name + " empty");
        }
    }

    private String createHsqldbAclFile() {
        try {
            String acl = """
                # https://www.hsqldb.org/doc/2.0/guide/listeners-chapt.html#lsc_acl
                
                %allowConnectionsFrom%
                """
                    .replace("%allowConnectionsFrom%", allowConnectionsFrom.stream()
                            .map(hostname -> "allow " + hostname)
                            .collect(Collectors.joining("\n")));

            File aclFile = File.createTempFile("hsqldb", "acl");
            aclFile.deleteOnExit();
            try (FileOutputStream fileOutputStream = new FileOutputStream(aclFile)) {
                IOUtils.write(acl, fileOutputStream, Charset.defaultCharset());
            }
            LOG.log(INFO, "HSQLDB using ACL in: {0}", aclFile.getAbsolutePath());
            return aclFile.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private class ToLogger extends Writer {
        private final Consumer<String> consumer;

        public ToLogger(Consumer<String> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            String s = new String(cbuf, off, len).replace("\n", "");
            if (!s.isBlank()) {
                consumer.accept(s);
            }
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
        }
    }
}
