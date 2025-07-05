package com.github.son_daehyeon.global.util;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class Logger extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {

        long start = System.currentTimeMillis();

        filterChain.doFilter(request, response);

        long end = System.currentTimeMillis();
        long elapsed = end - start;

        log.info("{} {} {} {}ms", request.getMethod(), request.getRequestURI(), getIp(request), elapsed);
    }

    private String getIp(HttpServletRequest request) {

        return Optional.ofNullable(request.getHeader("X-Forwarded-For"))
                .map(header -> header.split(","))
                .map(ips -> ips[0])
                .orElse(request.getRemoteAddr());
    }
}
