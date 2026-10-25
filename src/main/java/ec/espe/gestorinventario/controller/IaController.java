package ec.espe.gestorinventario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.espe.gestorinventario.ai.service.HrAssistantService;
import ec.espe.gestorinventario.ai.service.HrClasificadorService;
import ec.espe.gestorinventario.dto.request.ChatRequestDTO;
import ec.espe.gestorinventario.dto.request.TextoLibreDTO;
import ec.espe.gestorinventario.dto.response.ChatResponseDTO;
import ec.espe.gestorinventario.dto.response.ClasificacionResponseDTO;
import ec.espe.gestorinventario.entity.PoliticaEmpresa;
import ec.espe.gestorinventario.repository.PoliticaEmpresaRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ia")
public class IaController {

    private static final Logger log = LoggerFactory.getLogger(IaController.class);

    private final HrAssistantService hrAssistantService;
    private final HrClasificadorService hrClasificadorService;
    private final PoliticaEmpresaRepository politicaRepository;
    private final ec.espe.gestorinventario.repository.UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public IaController(HrAssistantService hrAssistantService,
                        HrClasificadorService hrClasificadorService,
                        PoliticaEmpresaRepository politicaRepository,
                        ec.espe.gestorinventario.repository.UsuarioRepository usuarioRepository) {
        this.hrAssistantService = hrAssistantService;
        this.hrClasificadorService = hrClasificadorService;
        this.politicaRepository = politicaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponseDTO> chat(@Valid @RequestBody ChatRequestDTO request, Authentication auth) {
        String usuarioActual = (auth != null && auth.isAuthenticated()) ? auth.getName() : "empleado@empresa.com";

        // Obtener datos del colaborador en la base de datos
        String datosColaborador = usuarioRepository.findByEmail(usuarioActual)
                .map(u -> String.format("Nombre: %s, Email: %s, Días de vacaciones hábiles disponibles: %d",
                        u.getNombreCompleto(), u.getEmail(), u.getDiasVacacionesDisponibles()))
                .orElse("Colaborador registrado con email: " + usuarioActual);

        // Obtener conocimiento interno de políticas empresariales
        List<PoliticaEmpresa> politicas = politicaRepository.findByActivoTrue();
        String contextoPoliticas = politicas.stream()
                .map(p -> String.format("### %s (%s):\n%s", p.getTitulo(), p.getCategoria(), p.getContenido()))
                .collect(Collectors.joining("\n\n"));

        if (contextoPoliticas.isBlank()) {
            contextoPoliticas = "No hay políticas cargadas actualmente en el sistema corporativo.";
        }

        try {
            // Llamada a la IA con contexto de políticas y datos del colaborador
            String respuesta = hrAssistantService.chat(contextoPoliticas, usuarioActual, datosColaborador, request.mensaje());
            return ResponseEntity.ok(new ChatResponseDTO(respuesta, !politicas.isEmpty()));
        } catch (Exception e) {
            log.error("Error al comunicarse con la API de IA (Gemini): {}", e.getMessage());
            String errorMsg = "⚠️ No se pudo conectar con el motor de IA de Google Gemini: " + e.getMessage()
                    + ".\n\nPor favor, asegúrate de colocar tu API Key válida en 'src/main/resources/application.properties' "
                    + "(propiedad langchain4j.open-ai.chat-model.api-key) o en la variable de entorno GEMINI_API_KEY.";
            return ResponseEntity.ok(new ChatResponseDTO(errorMsg, false));
        }
    }

    @PostMapping("/clasificar")
    public ResponseEntity<ClasificacionResponseDTO> clasificar(@Valid @RequestBody TextoLibreDTO request) {
        try {
            String jsonRaw = hrClasificadorService.clasificar(request.texto());
            log.info("[AI CLASIFICADOR RAW OUTPUT]: {}", jsonRaw);

            // Limpieza si el modelo incluye formato markdown ```json
            String cleaned = jsonRaw.replaceAll("(?s)```json\\s*", "").replaceAll("```", "").trim();
            ClasificacionResponseDTO dto = objectMapper.readValue(cleaned, ClasificacionResponseDTO.class);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error en clasificación con IA: {}", e.getMessage());
            return ResponseEntity.ok(new ClasificacionResponseDTO(
                    "OTRO",
                    "MEDIA",
                    "ADMIN_RRHH",
                    "Solicitud clasificada por fallback debido a: " + e.getMessage()
            ));
        }
    }
}