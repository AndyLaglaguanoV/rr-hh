package ec.espe.gestorinventario.repository;

import ec.espe.gestorinventario.entity.TipoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoSolicitudRepository extends JpaRepository<TipoSolicitud, Long> {
    Optional<TipoSolicitud> findByCodigo(String codigo);
}
