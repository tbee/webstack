package org.tbee.webstack.tenant;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tbee.webstack.spring.SpringUtils;

import java.util.Arrays;
import java.util.List;

/// This service contains all code that is needed for multi tenancy.
/// Like extracting tenant id from the request, running a thread under specific tenant.
/// In the example this usually is put into the filter or the context, but making it a separate service makes it injectable/usable in many places.
@Service
public class FromUsernameTenantService extends TenantService {
    private static final Logger LOG = LoggerFactory.getLogger(FromUsernameTenantService.class);

    public String extractTenantId(HttpServletRequest httpServletRequest) {
        return extractTenantId(SpringUtils.getLoggedInUsername());
    }

    public String extractTenantId(String loggedInUsername) {
        if (ANONYMOUS_USER.equals(loggedInUsername)) {
            return ANONYMOUS_USER;
        }
        if (loggedInUsername == null) {
            throw new IllegalStateException("LoggedInUsername is null");
        }
        return loggedInUsername.split("/")[0].trim();
    }

    public String extractUsername(HttpServletRequest httpServletRequest) {
        return extractUsername(SpringUtils.getLoggedInUsername());
    }

    public String extractUsername(String loggedInUsername) {
        if (ANONYMOUS_USER.equals(loggedInUsername)) {
            return loggedInUsername;
        }
        if (loggedInUsername == null) {
            throw new IllegalStateException("LoggedInUsername is null");
        }

        if (!loggedInUsername.contains("/")) {
            return loggedInUsername;
        }
        return loggedInUsername.split("/")[1].trim();
    }

    public String getLoggedInUsername() {
        return constructLoggedInUsername(TenantContext.getTenantId(), TenantContext.getUsername());
    }

    public String constructLoggedInUsername(String username) {
        return constructLoggedInUsername(TenantContext.getTenantId(), username);
    }

    public String constructLoggedInUsername(String tenantId, String username) {
        return tenantId + "/" + username;
    }

    public String getTenantTopic() {
        // TODO we could enhance obfuscation by using a static map of generated tenantId-to-uuid codes.
        return "" + TenantContext.getTenantId().hashCode();
    }

    static public List<String> extractTenantIdsFromConfigValue(String configValue) {
        return Arrays.stream(configValue.split(","))
                .map(s -> s.trim())
                .filter(s -> !s.isBlank())
                .toList();
    }
}
