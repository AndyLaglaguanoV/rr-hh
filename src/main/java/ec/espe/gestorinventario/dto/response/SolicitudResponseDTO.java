package ec.espe.gestorinventario.dto.response;

import ec.espe.gestorinventario.entity.Solicitud;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record SolicitudResponseDTO(
        Long id,
        Long empleadoId,
        String empleadoNombre,
        String empleadoEmail,
        String tipoSolicitudCodigo,
        String tipoSolicitudNombre,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Integer diasSolicitados,
        String motivo,
        BigDecimal montoReembolso,
        String estado,
        String aprobadorNombre,
        String observacionAprobador,
        LocalDateTime fechaCreacion
) {
    public static SolicitudResponseDTO fromEntity(Solicitud s) {
        return new SolicitudResponseDTO(
                s.getId(),
                s.getEmpleado().getId(),
                s.getEmpleado().getNombreCompleto(),
                s.getEmpleado().getEmail(),
                s.getTipoSolicitud().getCodigo(),
                s.getTipoSolicitud().getNombre(),
                s.getFechaInicio(),
                s.getFechaFin(),
                s.getDiasSolicitados(),
                s.getMotivo(),
                s.getMontoReembolso(),
                s.getEstado().name(),
                s.getAprobadorActual() != null ? s.getAprobadorActual().getNombreCompleto() : null,
                s.getObservacionAprobador(),
                s.getFechaCreacion()
        );
    }
}
