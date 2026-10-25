package ec.espe.gestorinventario.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChatRequestDTO(
        @NotBlank(message = "El mensaje no puede estar vacío")
        String mensaje
) {}
