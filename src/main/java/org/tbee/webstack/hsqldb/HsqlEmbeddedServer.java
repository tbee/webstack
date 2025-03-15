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
public class HsqlEmbeddedServer {
    private static final System.Logger LOG = System.getLogger(HsqlEmbeddedServer.class.getName());
    public static final String URL_PREFIX = "jdbc:hsqldb:hsql://";

    private Integer port;
    private String folder = "hsqldb";
    private List<String> databases = new ArrayList<>();
    private String username;
    private String password;
    private boolean shutdownOnLastConnectionClose = false;

    // Per default only allow connections from localhost
    private List<String> allowConnectionsFrom = new ArrayList<>(List.of("localhost"));

    private org.hsqldb.Server server;
    private final Thread shutdownHook = new Thread(() -> stop());

    public HsqlEmbeddedServer port(int v) {
        this.port = v;
        return this;
    }
    public int port() {
        return port;
    }

    public HsqlEmbeddedServer portFromUrl(String url) {
        if (!url.startsWith(URL_PREFIX)) {
            throw new IllegalArgumentException("An embedded HSQLDB server must use an URL starting with " + URL_PREFIX);
        }
        String remainder = url.substring(URL_PREFIX.length());
        String datasourceUrlSuffix = remainder.substring(remainder.indexOf(":") + 1);
        int port = Integer.parseInt(datasourceUrlSuffix.substring(0, datasourceUrlSuffix.indexOf("/")));
        return port(port);
    }

    public HsqlEmbeddedServer database(String v, String... vs) {
        this.databases.add(v);
        this.databases.addAll(Arrays.asList(vs));
        return this;
    }

    public HsqlEmbeddedServer database(List<String> v) {
        this.databases.addAll(v);
        return this;
    }
    public List<String> databases() {
        return databases;
    }

    public HsqlEmbeddedServer databaseFromUrl(String url) {
        if (!url.startsWith(URL_PREFIX)) {
            throw new IllegalArgumentException("An embedded HSQLDB server must use an URL starting with " + URL_PREFIX);
        }
        String datasourceUrlSuffix = url.substring(url.lastIndexOf(":") + 1);
        String dbname = datasourceUrlSuffix.substring(datasourceUrlSuffix.indexOf("/") + 1);
        return database(dbname);
    }

    public HsqlEmbeddedServer username(String v) {
        this.username = v;
        return this;
    }
    public String username() {
        return username;
    }

    public HsqlEmbeddedServer password(String v) {
        this.password = v;
        return this;
    }
    public String password() {
        return password;
    }

    public HsqlEmbeddedServer folder(String v) {
        this.folder = v;
        return this;
    }
    public String folder() {
        return folder;
    }

    public HsqlEmbeddedServer allowConnectionFrom(String v) {
        this.allowConnectionsFrom.add(v);
        return this;
    }
    public List<String> allowConnectionsFrom() {
        return allowConnectionsFrom;
    }

    public HsqlEmbeddedServer shutdownOnLastConnectionClose(boolean v) {
        this.shutdownOnLastConnectionClose = v;
        return this;
    }
    public boolean shutdownOnLastConnectionClose() {
        return shutdownOnLastConnectionClose;
    }


    public HsqlEmbeddedServer start() {
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
        LOG.log(INFO, "Starting HSQL on port {0} with databases in {1}", port, new File(folder).getAbsolutePath());

        // If the dbname is <tenantId> then we will start tenants, otherwise just the one database
        for (int i = 0; i < databases.size(); i++) {
            String database = databases.get(i);
            hsqlProperties.setProperty("server.database." + i, "file:" + folder + "/" + database + ";user=" + username + ";password=" + password + (shutdownOnLastConnectionClose ? ";shutdown=true" : ""));
            hsqlProperties.setProperty("server.dbname." + i, database);
            LOG.log(INFO, "Starting HSQL database {0} as {1}", database, username);
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

    public HsqlEmbeddedServer stop() {
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
        try {
            Runtime.getRuntime().removeShutdownHook(shutdownHook);
        }
        catch (IllegalStateException e) {
            // ignore, because this is expected when the JVM is shutting down
        }

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
