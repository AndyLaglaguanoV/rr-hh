package ec.espe.gestorinventario.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DecisionSolicitudDTO(
        @NotNull(message = "Debe especificar si aprueba o rechaza")
        Boolean aprobada,

        @NotBlank(message = "La observación o justificación es requerida")
        String observacion
) {}
