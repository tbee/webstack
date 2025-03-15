package org.tbee.webstack.tenant;

//import org.apache.logging.log4j.ThreadContext;
import org.slf4j.MDC;

/// The tenant context holds the active tenant information.
public class TenantContext {

    public static final String NO_TENTANT_ID = "main";

    private static final ThreadLocal<String> CURRENT_TENANT_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_USERNAME = new ThreadLocal<>();

    public static String getTenantId() {
        // During startup this method is called before any tenant is setup, assume default
        return CURRENT_TENANT_ID.get() == null ? NO_TENTANT_ID : CURRENT_TENANT_ID.get();
    }

    public static void setTenant(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalStateException("TenantId is null or empty");
        }
        CURRENT_TENANT_ID.set(tenantId);
//        ThreadContext.put("tenant", tenantId); // log4j2
        MDC.put("tenant", tenantId); // Logback and slf4j
    }

    public static void clearTenant() {
        CURRENT_TENANT_ID.remove();
//        ThreadContext.put("tenant", "");
        MDC.put("tenant", "");
    }

    public static String getUsername() {
        return CURRENT_USERNAME.get();
    }

    public static void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalStateException("Username is null or empty");
        }
        CURRENT_USERNAME.set(username);
//        ThreadContext.put("username", username); // log4j2
        MDC.put("username", username); // Logback and slf4j
    }

    public static void clearUsername() {
        CURRENT_USERNAME.remove();
//        ThreadContext.put("username", "");
        MDC.put("username", "");
    }
}
