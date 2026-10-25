package ec.espe.gestorinventario.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "El email es requerido")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "La contraseña es requerida")
        String password
) {}
