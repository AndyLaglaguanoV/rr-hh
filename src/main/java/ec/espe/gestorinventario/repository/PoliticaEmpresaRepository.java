package ec.espe.gestorinventario.repository;

import ec.espe.gestorinventario.entity.PoliticaEmpresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoliticaEmpresaRepository extends JpaRepository<PoliticaEmpresa, Long> {
    List<PoliticaEmpresa> findByActivoTrue();
    boolean existsByTitulo(String titulo);
    boolean existsByTituloAndIdNot(String titulo, Long id);
    Page<PoliticaEmpresa> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
}
