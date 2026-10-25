package ec.espe.gestorinventario.dto.response;

import java.time.LocalDateTime;

public record PoliticaResponse(
        Long id,
        String titulo,
        String categoria,
        String contenido,
        Boolean activo,
        Integer version,
        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn
) {}
