package ec.espe.gestorinventario.controller;

import ec.espe.gestorinventario.dto.request.CrearSolicitudDTO;
import ec.espe.gestorinventario.dto.request.DecisionSolicitudDTO;
import ec.espe.gestorinventario.dto.response.SolicitudResponseDTO;
import ec.espe.gestorinventario.interceptor.annotation.RequiereRol;
import ec.espe.gestorinventario.service.SolicitudService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    @RequiereRol({"ROLE_EMPLEADO", "ROLE_JEFE", "ROLE_ADMIN_RRHH"})
    public ResponseEntity<SolicitudResponseDTO> crearSolicitud(
            @Valid @RequestBody CrearSolicitudDTO request,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(solicitudService.crearSolicitud(request, auth.getName()));
    }

    @GetMapping("/mis-solicitudes")
    @RequiereRol({"ROLE_EMPLEADO", "ROLE_JEFE", "ROLE_ADMIN_RRHH"})
    public ResponseEntity<List<SolicitudResponseDTO>> listarMisSolicitudes(Authentication auth) {
        return ResponseEntity.ok(solicitudService.listarMisSolicitudes(auth.getName()));
    }

    @GetMapping("/pendientes-aprobacion")
    @RequiereRol({"ROLE_JEFE", "ROLE_ADMIN_RRHH"})
    public ResponseEntity<List<SolicitudResponseDTO>> listarPendientesAprobador(Authentication auth) {
        return ResponseEntity.ok(solicitudService.listarPendientesAprobador(auth.getName()));
    }

    @PatchMapping("/{id}/decision")
    @RequiereRol({"ROLE_JEFE", "ROLE_ADMIN_RRHH"})
    public ResponseEntity<SolicitudResponseDTO> resolverSolicitud(
            @PathVariable Long id,
            @Valid @RequestBody DecisionSolicitudDTO decision,
            Authentication auth) {
        return ResponseEntity.ok(solicitudService.resolverSolicitud(id, decision, auth.getName()));
    }
}
