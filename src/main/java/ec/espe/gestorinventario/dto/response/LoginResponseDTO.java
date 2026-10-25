package ec.espe.gestorinventario.dto.response;

import java.util.List;

public record LoginResponseDTO(
        String token,
        String email,
        String nombreCompleto,
        Integer diasVacacionesDisponibles,
        List<String> roles
) {}
