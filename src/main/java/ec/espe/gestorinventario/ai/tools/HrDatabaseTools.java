package ec.espe.gestorinventario.ai.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import ec.espe.gestorinventario.entity.Usuario;
import ec.espe.gestorinventario.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HrDatabaseTools {

    private static final Logger log = LoggerFactory.getLogger(HrDatabaseTools.class);
    private final UsuarioRepository usuarioRepository;

    public HrDatabaseTools(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Tool("Consulta en tiempo real en la base de datos el saldo de días hábiles de vacaciones disponibles de un colaborador de la empresa.")
    public String consultarSaldoVacaciones(
            @P("El correo electrónico del empleado para consultar su saldo. Si no se especifica, consultar el del usuario actual")
            String emailEmpleado) {

        log.info("[AI TOOL EXECUTION] Consultando balance de vacaciones con parámetro email: {}", emailEmpleado);

        String emailFinal = emailEmpleado;
        if (emailFinal == null || emailFinal.isBlank() || emailFinal.equalsIgnoreCase("null") || emailFinal.equalsIgnoreCase("actual")) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                emailFinal = auth.getName();
            }
        }

        if (emailFinal == null || emailFinal.isBlank()) {
            return "No se pudo determinar el correo del colaborador para consultar sus vacaciones.";
        }

        Optional<Usuario> userOpt = usuarioRepository.findByEmail(emailFinal);
        if (userOpt.isPresent()) {
            Usuario u = userOpt.get();
            return String.format("El colaborador %s (%s) dispone actualmente de %d días de vacaciones hábiles.",
                    u.getNombreCompleto(), u.getEmail(), u.getDiasVacacionesDisponibles());
        } else {
            return "No se encontró registro para el usuario con email: " + emailFinal;
        }
    }
}
