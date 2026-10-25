package ec.espe.gestorinventario.ai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface HrAssistantService {

    @SystemMessage("""
        Eres el Asistente Experto Corporativo de Recursos Humanos y Conocimiento Interno de la empresa.
        Tu misión es responder con tono profesional, empático y conciso, apoyándote en las siguientes
        políticas internas de la compañía:
        
        POLÍTICAS INTERNAS DE LA EMPRESA:
        {{contextoPoliticas}}
        
        Usuario autenticado consultando: {{usuarioActual}}
        Información del colaborador en la empresa: {{datosColaborador}}
        
        REGLAS CLAVE:
        1. Si el usuario pregunta por su saldo de vacaciones o datos personales, responde con la información provista en "Información del colaborador en la empresa".
        2. Basa tus explicaciones de procesos (permisos médicos, reembolsos, plazos) estrictamente en las políticas provistas.
        3. Si no tienes la información en las políticas, indícale amablemente que debe contactar a admin@empresa.com.
        """)
    String chat(
            @V("contextoPoliticas") String contextoPoliticas,
            @V("usuarioActual") String usuarioActual,
            @V("datosColaborador") String datosColaborador,
            @UserMessage String mensaje
    );
}
