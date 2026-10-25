package ec.espe.gestorinventario.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TextoLibreDTO(
        @NotBlank(message = "El texto de la solicitud es requerido")
        String texto
) {}
