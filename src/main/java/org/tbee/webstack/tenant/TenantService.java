package org.tbee.webstack.tenant;

import jakarta.servlet.ServletException;

import java.io.IOException;
import java.util.concurrent.Callable;

abstract public class TenantService {
    public static final String ANONYMOUS_USER = "anonymousUser";
    public static final String SUPERUSER = "superuser";

    public boolean isAnonymousUser() {
        return ANONYMOUS_USER.equals(TenantContext.getTenantId());
    }

    public void runWithTenant(String tenantId, String username, RunnableWithException runnable) throws Exception {
        runWithTenant(tenantId, username, (Callable<Void>) () -> {
            runnable.run();
            return null;
        });
    }

    public <T> T runWithTenant(String tenantId, String username, Callable<T> callable) throws Exception {
        TenantContext.setTenant(tenantId);
        TenantContext.setUsername(username);
        try {
            return callable.call();
        } finally {
            TenantContext.clearTenant();
            TenantContext.clearUsername();
        }
    }

    public interface RunnableWithException {
        void run() throws IOException, ServletException;
    }
}
