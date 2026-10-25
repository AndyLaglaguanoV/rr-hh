package ec.espe.gestorinventario.interceptor;

import ec.espe.gestorinventario.exception.AccesoDenegadoException;
import ec.espe.gestorinventario.interceptor.annotation.RequiereRol;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AutorizacionRolInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AutorizacionRolInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        RequiereRol anotacion = handlerMethod.getMethodAnnotation(RequiereRol.class);
        if (anotacion == null) {
            anotacion = handlerMethod.getBeanType().getAnnotation(RequiereRol.class);
        }

        if (anotacion == null) {
            return true;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new AccesoDenegadoException("Petición no autenticada para recurso restringido.");
        }

        Set<String> rolesUsuario = auth.getAuthorities().stream()
                .map(granted -> granted.getAuthority())
                .collect(Collectors.toSet());

        boolean autorizado = Arrays.stream(anotacion.value())
                .anyMatch(rolesUsuario::contains);

        if (!autorizado) {
            log.warn("[RBAC VIOLATION] Usuario '{}' intentó acceder a {} sin roles requeridos: {}",
                    auth.getName(), request.getRequestURI(), Arrays.toString(anotacion.value()));
            throw new AccesoDenegadoException("No posee privilegios suficientes para esta operación de RR.HH. Requerido: "
                    + Arrays.toString(anotacion.value()));
        }

        return true;
    }
}
