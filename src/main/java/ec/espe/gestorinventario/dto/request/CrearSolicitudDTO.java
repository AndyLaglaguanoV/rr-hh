package ec.espe.gestorinventario.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CrearSolicitudDTO(
        @NotBlank(message = "El código de tipo de solicitud es requerido")
        String codigoTipoSolicitud,

        @NotNull(message = "La fecha de inicio es requerida")
        LocalDate fechaInicio,

        LocalDate fechaFin,

        @NotNull(message = "La cantidad de días solicitados es requerida")
        @Min(value = 1, message = "Debe solicitar al menos 1 día")
        Integer diasSolicitados,

        @NotBlank(message = "El motivo es requerido")
        String motivo,

        BigDecimal montoReembolso
) {}
