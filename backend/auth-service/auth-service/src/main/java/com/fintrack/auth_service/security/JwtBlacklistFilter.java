package com.fintrack.auth_service.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtBlacklistFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenBlacklistService blacklistService;

    public JwtBlacklistFilter(JwtService jwtService, TokenBlacklistService blacklistService) {
        this.jwtService = jwtService;
        this.blacklistService = blacklistService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // login/register gibi public endpointleri bypass et
        return path.startsWith("/api/auth/login")
                || path.startsWith("/api/auth/register")
                || path.startsWith("/actuator");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = extractBearer(header);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // signature + expiration validate (exception fırlatır)
            jwtService.isSignatureValid(token);

            // blacklist kontrol
            if (blacklistService.isJtiBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token is blacklisted (logged out).");
                return;
            }

            // burada userDetails vs bağlamıyorsan minimum auth set edebilirsin
            String subject = jwtService.getSubject(token);

            var auth = new UsernamePasswordAuthenticationToken(subject, null, List.of());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expired.");
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token.");
        }
    }

    private String extractBearer(String authHeader) {
        if (authHeader == null) return null;
        if (!authHeader.startsWith("Bearer ")) return null;
        return authHeader.substring("Bearer ".length()).trim();
    }
}

