package com.project.messenger.security.componets;

import com.project.messenger.services.VerificationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final VerificationService verificationService;

    public CustomAuthenticationSuccessHandler(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        String username = authentication.getName();

        if (!verificationService.isUserVerified(username)) {
            SecurityContextHolder.clearContext();
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/login?notVerified=true");
        }
        else {
            response.sendRedirect("http://localhost:3000");
        }
    }
}
