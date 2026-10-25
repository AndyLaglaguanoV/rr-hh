package ec.espe.gestorinventario.interceptor;

import ec.espe.gestorinventario.entity.AuditoriaLog;
import ec.espe.gestorinventario.repository.AuditoriaLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
public class AuditoriaInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaInterceptor.class);
    private final AuditoriaLogRepository auditoriaLogRepository;

    public AuditoriaInterceptor(AuditoriaLogRepository auditoriaLogRepository) {
        this.auditoriaLogRepository = auditoriaLogRepository;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (!request.getRequestURI().startsWith("/api/")) {
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String usuario = (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal()))
                ? auth.getName() : "ANONIMO";

        AuditoriaLog entry = new AuditoriaLog(
                usuario,
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr(),
                response.getStatus(),
                LocalDateTime.now(),
                ex != null ? "EXCEPCION: " + ex.getMessage() : "OK"
        );

        try {
            auditoriaLogRepository.save(entry);
        } catch (Exception persistenceEx) {
            log.error("Fallo registrando auditoría en BD: {}", persistenceEx.getMessage());
        }
    }
}
