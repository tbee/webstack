package org.tbee.webstack.spring;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SpringUtils {

    static public String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails userDetails) ? userDetails.getUsername() : principal.toString();
        return username;
    }

    public static HttpSession session() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return servletRequestAttributes.getRequest().getSession(true); // true == allow create
    }
}
