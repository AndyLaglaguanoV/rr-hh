package ec.espe.gestorinventario.service;

import ec.espe.gestorinventario.dto.request.PoliticaRequest;
import ec.espe.gestorinventario.dto.response.PoliticaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PoliticaService {
    Page<PoliticaResponse> listar(Pageable pageable);
    List<PoliticaResponse> listarActivas();
    Page<PoliticaResponse> buscarPorNombre(String nombre, Pageable pageable);
    PoliticaResponse obtener(Long id);
    PoliticaResponse crear(PoliticaRequest request);
    PoliticaResponse actualizar(Long id, PoliticaRequest request);
    void eliminar(Long id);
}
