package ec.espe.gestorinventario.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timestamp,
        Integer status,
        String error,
        String message,
        String path,
        List<String> errors
) {}
