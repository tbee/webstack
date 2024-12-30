package org.tbee.webstack;

import org.apache.commons.io.FileUtils;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tbee.webstack.hsqldb.HsqlEmbedded;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class HsqlEmbeddedTest {

    private int port = -1;

    @BeforeEach
    public void beforeEach() throws IOException {
        FileUtils.deleteDirectory(new File("hsqldb"));

        try (ServerSocket serverSocket = new ServerSocket(0)) {
            port = serverSocket.getLocalPort();
        } catch (IOException e) {
            fail("Port is not available");
        }
    }

    @Test
    public void startUseStop() {

        // WHEN hsql is started
        HsqlEmbedded hsqlEmbedded = new HsqlEmbedded()
                .port(port)
                .username("username")
                .password("password")
                .database(this.getClass().getSimpleName())
                .start();

        // AND used
        Jdbi jdbi = Jdbi.create("jdbc:hsqldb:hsql://localhost:" + port + "/" + this.getClass().getSimpleName(), "username", "password");
        List<String> names = jdbi.withHandle(handle -> {
            handle.execute("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR)");
            handle.execute("INSERT INTO user (id, name) VALUES (?, ?)", 0, "Alice");
            return handle.createQuery("SELECT name FROM user").mapTo(String.class).list();
        });
        assertEquals(1, names.size());
        assertEquals("Alice", names.get(0));

        // AND shutdown
        hsqlEmbedded.stop();

        // THEN all should have works without issues
    }
}