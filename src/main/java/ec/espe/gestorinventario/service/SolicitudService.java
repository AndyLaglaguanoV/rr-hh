package ec.espe.gestorinventario.service;

import ec.espe.gestorinventario.dto.request.CrearSolicitudDTO;
import ec.espe.gestorinventario.dto.request.DecisionSolicitudDTO;
import ec.espe.gestorinventario.dto.response.SolicitudResponseDTO;
import ec.espe.gestorinventario.entity.Solicitud;
import ec.espe.gestorinventario.entity.TipoSolicitud;
import ec.espe.gestorinventario.entity.Usuario;
import ec.espe.gestorinventario.repository.SolicitudRepository;
import ec.espe.gestorinventario.repository.TipoSolicitudRepository;
import ec.espe.gestorinventario.repository.UsuarioRepository;
import ec.espe.gestorinventario.service.notification.NotificadorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoSolicitudRepository tipoSolicitudRepository;
    private final AprobadorService aprobadorService;
    private final NotificadorService notificadorService; // Demostración de IoC / DI

    /**
     * Inyección por constructor con resolución de interfaz
     */
    public SolicitudService(
            SolicitudRepository solicitudRepository,
            UsuarioRepository usuarioRepository,
            TipoSolicitudRepository tipoSolicitudRepository,
            AprobadorService aprobadorService,
            @Qualifier("emailNotificadorService") NotificadorService notificadorService) {
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoSolicitudRepository = tipoSolicitudRepository;
        this.aprobadorService = aprobadorService;
        this.notificadorService = notificadorService;
    }

    @Transactional
    public SolicitudResponseDTO crearSolicitud(CrearSolicitudDTO dto, String emailEmpleado) {
        Usuario empleado = usuarioRepository.findByEmail(emailEmpleado)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con email: " + emailEmpleado));

        TipoSolicitud tipo = tipoSolicitudRepository.findByCodigo(dto.codigoTipoSolicitud())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de solicitud no válido: " + dto.codigoTipoSolicitud()));

        if ("VACACIONES".equalsIgnoreCase(tipo.getCodigo())) {
            if (empleado.getDiasVacacionesDisponibles() < dto.diasSolicitados()) {
                throw new IllegalStateException("Saldo de vacaciones insuficiente. Disponibles: "
                        + empleado.getDiasVacacionesDisponibles() + ", Solicitados: " + dto.diasSolicitados());
            }
        }

        Solicitud solicitud = new Solicitud();
        solicitud.setEmpleado(empleado);
        solicitud.setTipoSolicitud(tipo);
        solicitud.setFechaInicio(dto.fechaInicio());
        solicitud.setFechaFin(dto.fechaFin());
        solicitud.setDiasSolicitados(dto.diasSolicitados());
        solicitud.setMotivo(dto.motivo());
        solicitud.setMontoReembolso(dto.montoReembolso());
        solicitud.setEstado(Solicitud.EstadoSolicitud.PENDIENTE);

        Usuario aprobador = aprobadorService.determinarSiguienteAprobador(solicitud);
        solicitud.setAprobadorActual(aprobador);

        Solicitud guardada = solicitudRepository.save(solicitud);

        // Desacoplado: NotificadorService abstracto
        if (aprobador != null) {
            notificadorService.notificar(
                    aprobador.getEmail(),
                    "Nueva solicitud pendiente: #" + guardada.getId(),
                    "El colaborador " + empleado.getNombreCompleto() + " ha solicitado "
                            + tipo.getNombre() + " (" + dto.diasSolicitados() + " días).",
                    guardada.getId()
            );
        }

        return SolicitudResponseDTO.fromEntity(guardada);
    }

    @Transactional(readOnly = true)
    public List<SolicitudResponseDTO> listarMisSolicitudes(String emailEmpleado) {
        Usuario empleado = usuarioRepository.findByEmail(emailEmpleado)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return solicitudRepository.findByEmpleadoIdOrderByFechaCreacionDesc(empleado.getId())
                .stream().map(SolicitudResponseDTO::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<SolicitudResponseDTO> listarPendientesAprobador(String emailAprobador) {
        Usuario aprobador = usuarioRepository.findByEmail(emailAprobador)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return solicitudRepository.findByAprobadorActualIdAndEstadoOrderByFechaCreacionDesc(
                        aprobador.getId(), Solicitud.EstadoSolicitud.PENDIENTE)
                .stream().map(SolicitudResponseDTO::fromEntity).toList();
    }

    @Transactional
    public SolicitudResponseDTO resolverSolicitud(Long solicitudId, DecisionSolicitudDTO decision, String emailAprobador) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada con ID: " + solicitudId));

        if (solicitud.getEstado() != Solicitud.EstadoSolicitud.PENDIENTE) {
            throw new IllegalStateException("La solicitud ya fue procesada anteriormente.");
        }

        solicitud.setObservacionAprobador(decision.observacion());

        if (Boolean.TRUE.equals(decision.aprobada())) {
            solicitud.setEstado(Solicitud.EstadoSolicitud.APROBADA_JEFE);

            // Si es vacaciones, descontar saldo de la base de datos
            if ("VACACIONES".equalsIgnoreCase(solicitud.getTipoSolicitud().getCodigo())) {
                Usuario emp = solicitud.getEmpleado();
                emp.setDiasVacacionesDisponibles(emp.getDiasVacacionesDisponibles() - solicitud.getDiasSolicitados());
                usuarioRepository.save(emp);
            }

            notificadorService.notificar(
                    solicitud.getEmpleado().getEmail(),
                    "Solicitud Aprobada #" + solicitud.getId(),
                    "Tu solicitud de " + solicitud.getTipoSolicitud().getNombre() + " ha sido aprobada.",
                    solicitud.getId()
            );
        } else {
            solicitud.setEstado(Solicitud.EstadoSolicitud.RECHAZADA);
            notificadorService.notificar(
                    solicitud.getEmpleado().getEmail(),
                    "Solicitud Rechazada #" + solicitud.getId(),
                    "Motivo: " + decision.observacion(),
                    solicitud.getId()
            );
        }

        return SolicitudResponseDTO.fromEntity(solicitudRepository.save(solicitud));
    }
}
