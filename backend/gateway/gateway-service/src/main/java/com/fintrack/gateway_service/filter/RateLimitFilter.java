package com.fintrack.gateway_service.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS_PER_SECOND = 5; // sınır: saniyede 5 istek
    private static final Map<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();
        long currentSecond = Instant.now().getEpochSecond();

        RequestCounter counter = requestCounts.computeIfAbsent(clientIp, ip -> new RequestCounter(currentSecond, 0));

        synchronized (counter) {
            if (counter.second != currentSecond) {
                counter.second = currentSecond;
                counter.count = 0;
            }

            counter.count++;

            if (counter.count > MAX_REQUESTS_PER_SECOND) {
                response.setStatus(429);
                response.getWriter().write("Too Many Requests - Rate limit exceeded");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private static class RequestCounter {
        long second;
        int count;
        RequestCounter(long second, int count) {
            this.second = second;
            this.count = count;
        }
    }
}