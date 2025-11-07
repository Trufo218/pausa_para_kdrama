package com.pausaparakdramas.PausaParaKdramas.Security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

@Component
public class FirebaseTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, jakarta.servlet.ServletException {

        String header = req.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                FirebaseToken decoded = FirebaseAuth.getInstance().verifyIdToken(token);
                req.setAttribute("uidFirebase", decoded.getUid());

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(decoded.getUid(), null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("Token inválido: " + e.getMessage());
                return;
            }
        }

        chain.doFilter(req, res);
    }
}
