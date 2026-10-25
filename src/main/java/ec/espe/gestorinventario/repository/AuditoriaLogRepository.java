package ec.espe.gestorinventario.repository;

import ec.espe.gestorinventario.entity.AuditoriaLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditoriaLogRepository extends JpaRepository<AuditoriaLog, Long> {
    List<AuditoriaLog> findTop50ByOrderByTimestampDesc();
}
