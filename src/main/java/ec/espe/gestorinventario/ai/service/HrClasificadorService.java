package ec.espe.gestorinventario.ai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface HrClasificadorService {

    @SystemMessage("""
        Eres un clasificador experto de solicitudes de Recursos Humanos.
        Analiza el texto libre que envía un empleado y responde ÚNICAMENTE un objeto JSON válido con los siguientes campos:
        {
          "tipoSolicitud": "VACACIONES" | "PERMISO_MEDICO" | "REEMBOLSO" | "CERTIFICADO_LABORAL" | "OTRO",
          "nivelUrgencia": "BAJA" | "MEDIA" | "ALTA",
          "responsableAsignado": "JEFE_DIRECTO" | "ADMIN_RRHH",
          "justificacionBreve": "Resumen conciso en una línea del motivo expresado por el empleado"
        }
        No incluyas explicaciones adicionales, ni bloques markdown con comillas invertidas, solo el JSON puro.
        """)
    String clasificar(@UserMessage String textoSolicitud);
}
