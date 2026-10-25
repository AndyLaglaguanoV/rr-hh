package ec.espe.gestorinventario.repository;

import ec.espe.gestorinventario.entity.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    List<Solicitud> findByEmpleadoIdOrderByFechaCreacionDesc(Long empleadoId);
    List<Solicitud> findByAprobadorActualIdAndEstadoOrderByFechaCreacionDesc(Long aprobadorId, Solicitud.EstadoSolicitud estado);
    List<Solicitud> findByEstadoOrderByFechaCreacionDesc(Solicitud.EstadoSolicitud estado);
}
