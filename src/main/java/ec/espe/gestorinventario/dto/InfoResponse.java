package ec.espe.gestorinventario.dto;

import java.time.Instant;

public record InfoResponse(
        String name,
        String description,
        String version,
        String environment,
        DeveloperInfo developer,
        Instant timestamp) {

    public record DeveloperInfo(String name, String email) {}
}