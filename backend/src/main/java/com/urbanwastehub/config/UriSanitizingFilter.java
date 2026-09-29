package com.urbanwastehub.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UriSanitizingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest && response instanceof HttpServletResponse httpResponse) {
            String uri = httpRequest.getRequestURI();
            if (uri != null && (uri.contains("\n") || uri.contains("\r") || uri.contains("%0A") || uri.contains("%0a") || uri.contains("%0D") || uri.contains("%0d"))) {
                String cleanUri = uri.replaceAll("[\\r\\n]+$", "")
                                     .replaceAll("(?i)%0a", "")
                                     .replaceAll("(?i)%0d", "")
                                     .trim();
                httpRequest.getRequestDispatcher(cleanUri).forward(request, response);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
