package ec.espe.gestorinventario.security;

import ec.espe.gestorinventario.entity.Usuario;
import ec.espe.gestorinventario.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        if (jwtService.validarToken(token)) {
            String email = jwtService.obtenerEmailDelToken(token);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
                if (usuarioOpt.isPresent()) {
                    Usuario u = usuarioOpt.get();
                    var authorities = u.getRoles().stream()
                            .map(r -> new SimpleGrantedAuthority(r.getNombre()))
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            u.getEmail(), null, authorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
