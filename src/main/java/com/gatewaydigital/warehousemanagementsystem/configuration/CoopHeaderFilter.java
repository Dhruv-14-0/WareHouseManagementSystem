package com.gatewaydigital.warehousemanagementsystem.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CoopHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Cross-Origin-Opener-Policy", "unsafe-none");
        response.setHeader("Cross-Origin-Embedder-Policy", "unsafe-none");

        filterChain.doFilter(request, response);
    }
}
