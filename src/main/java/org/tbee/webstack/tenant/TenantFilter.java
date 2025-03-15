package org.tbee.webstack.tenant;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/// This is the filter that extracts the tenant information from the request,
/// and then lets the remaining request run with that information.
public class TenantFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(TenantFilter.class);

    private final FromUsernameTenantService tenantService;

    public TenantFilter(FromUsernameTenantService tenantService) {
        this.tenantService = tenantService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;

        String tenantId = tenantService.extractTenantId(httpServletRequest);
        String username = tenantService.extractUsername(httpServletRequest);
        if (tenantId.isBlank()) {
            httpServletResponse.sendError(HttpStatus.BAD_GATEWAY.value(), "Unknown tenant");
            return;
        }

        try {
            tenantService.runWithTenant(tenantId, username, () -> chain.doFilter(request, response));
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
