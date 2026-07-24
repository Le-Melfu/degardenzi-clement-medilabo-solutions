package com.medilabo.patient.logging;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI() + querySuffix(request);
        String method = request.getMethod();
        String user = currentUsername();

        log.info(formatRequest(method, path, user));

        long startedAt = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        long durationMs = System.currentTimeMillis() - startedAt;

        log.info(formatResponse(method, path, response.getStatus(), durationMs, user));
    }

    private static String formatRequest(String method, String path, String user) {
        return AnsiOutput.toString(
                AnsiColor.BRIGHT_GREEN, AnsiStyle.BOLD, "[Patient]",
                AnsiColor.DEFAULT, " ",
                AnsiColor.BRIGHT_YELLOW, AnsiStyle.BOLD, "REQUEST",
                AnsiColor.DEFAULT, " --> ",
                AnsiColor.BRIGHT_CYAN, method,
                AnsiColor.DEFAULT, " ",
                AnsiColor.WHITE, path,
                AnsiColor.DEFAULT, " user=",
                AnsiColor.BRIGHT_MAGENTA, user);
    }

    private static String formatResponse(String method, String path, int status, long durationMs, String user) {
        return AnsiOutput.toString(
                AnsiColor.BRIGHT_GREEN, AnsiStyle.BOLD, "[Patient]",
                AnsiColor.DEFAULT, " ",
                AnsiColor.BRIGHT_GREEN, AnsiStyle.BOLD, "RESPONSE",
                AnsiColor.DEFAULT, " <-- ",
                AnsiColor.BRIGHT_CYAN, method,
                AnsiColor.DEFAULT, " ",
                AnsiColor.WHITE, path,
                AnsiColor.DEFAULT, " -> ",
                statusColor(status), AnsiStyle.BOLD, String.valueOf(status),
                AnsiColor.DEFAULT, " (",
                durationColor(durationMs), durationMs + " ms",
                AnsiColor.DEFAULT, ") user=",
                AnsiColor.BRIGHT_MAGENTA, user);
    }

    private static AnsiColor statusColor(int status) {
        if (status >= 500) {
            return AnsiColor.BRIGHT_RED;
        }
        if (status >= 400) {
            return AnsiColor.BRIGHT_YELLOW;
        }
        return AnsiColor.BRIGHT_GREEN;
    }

    private static AnsiColor durationColor(long durationMs) {
        if (durationMs >= 1000) {
            return AnsiColor.BRIGHT_RED;
        }
        if (durationMs >= 300) {
            return AnsiColor.BRIGHT_YELLOW;
        }
        return AnsiColor.BRIGHT_GREEN;
    }

    private static String querySuffix(HttpServletRequest request) {
        String query = request.getQueryString();
        return query == null || query.isBlank() ? "" : "?" + query;
    }

    private static String currentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return "anonymous";
        }
        return authentication.getName();
    }
}
