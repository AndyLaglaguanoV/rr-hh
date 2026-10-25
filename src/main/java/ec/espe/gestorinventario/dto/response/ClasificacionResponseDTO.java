package ec.espe.gestorinventario.dto.response;

public record ClasificacionResponseDTO(
        String tipoSolicitud,
        String nivelUrgencia,
        String responsableAsignado,
        String justificacionBreve
) {}
