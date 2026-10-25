package ec.espe.gestorinventario.controller;

import ec.espe.gestorinventario.entity.AuditoriaLog;
import ec.espe.gestorinventario.interceptor.annotation.RequiereRol;
import ec.espe.gestorinventario.repository.AuditoriaLogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
public class PoliticaYAuditoriaController {

    private final AuditoriaLogRepository auditoriaLogRepository;

    public PoliticaYAuditoriaController(AuditoriaLogRepository auditoriaLogRepository) {
        this.auditoriaLogRepository = auditoriaLogRepository;
    }

    @GetMapping("/logs")
    @RequiereRol({"ROLE_ADMIN_RRHH"})
    public ResponseEntity<List<AuditoriaLog>> listarLogs() {
        return ResponseEntity.ok(auditoriaLogRepository.findTop50ByOrderByTimestampDesc());
    }
}
