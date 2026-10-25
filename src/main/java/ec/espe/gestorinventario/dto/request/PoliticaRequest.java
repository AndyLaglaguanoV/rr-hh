package ec.espe.gestorinventario.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PoliticaRequest(
        @NotBlank(message = "El título es obligatorio")
        @Size(min = 3, max = 150, message = "El título debe tener entre 3 y 150 caracteres")
        String titulo,

        @NotBlank(message = "La categoría es obligatoria")
        @Size(max = 60, message = "La categoría admite máximo 60 caracteres")
        String categoria,

        @NotBlank(message = "El contenido es obligatorio")
        @Size(min = 10, message = "El contenido debe tener al menos 10 caracteres")
        String contenido,

        @NotNull(message = "El estado activo es obligatorio")
        Boolean activo,

        Integer version
) {}
