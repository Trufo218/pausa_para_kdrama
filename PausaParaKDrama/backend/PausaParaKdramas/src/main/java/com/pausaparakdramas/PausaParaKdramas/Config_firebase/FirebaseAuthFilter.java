package com.pausaparakdramas.PausaParaKdramas.Config_firebase;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro para validar JWT de Firebase en cada petición
 */
@Component
public class FirebaseAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String idToken = authHeader.substring(7);
            try {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
                // Guardamos UID en la request para usar en el controller
                request.setAttribute("uidFirebase", decodedToken.getUid());
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Token no válido");
                return;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Token no proporcionado");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
