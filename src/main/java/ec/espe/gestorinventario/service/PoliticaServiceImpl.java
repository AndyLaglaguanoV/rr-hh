package ec.espe.gestorinventario.service;

import ec.espe.gestorinventario.dto.request.PoliticaRequest;
import ec.espe.gestorinventario.dto.response.PoliticaResponse;
import ec.espe.gestorinventario.entity.PoliticaEmpresa;
import ec.espe.gestorinventario.exception.ConflictoException;
import ec.espe.gestorinventario.exception.RecursoNotFoundException;
import ec.espe.gestorinventario.exception.ReglaNegocioException;
import ec.espe.gestorinventario.repository.PoliticaEmpresaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PoliticaServiceImpl implements PoliticaService {

    private final PoliticaEmpresaRepository repository;
    private final PoliticaMapper mapper;

    public PoliticaServiceImpl(PoliticaEmpresaRepository repository, PoliticaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Page<PoliticaResponse> listar(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    public List<PoliticaResponse> listarActivas() {
        return repository.findByActivoTrue().stream().map(mapper::toResponse).toList();
    }

    @Override
    public Page<PoliticaResponse> buscarPorNombre(String nombre, Pageable pageable) {
        return repository.findByTituloContainingIgnoreCase(nombre, pageable).map(mapper::toResponse);
    }

    @Override
    public PoliticaResponse obtener(Long id) {
        return mapper.toResponse(buscar(id));
    }

    @Override
    @Transactional
    public PoliticaResponse crear(PoliticaRequest req) {
        if (repository.existsByTitulo(req.titulo().trim())) {
            throw new ConflictoException("Ya existe una política corporativa con el título: " + req.titulo());
        }
        validarReglas(req);
        PoliticaEmpresa guardada = repository.save(mapper.toEntity(req));
        return mapper.toResponse(guardada);
    }

    @Override
    @Transactional
    public PoliticaResponse actualizar(Long id, PoliticaRequest req) {
        PoliticaEmpresa entidad = buscar(id);
        if (repository.existsByTituloAndIdNot(req.titulo().trim(), id)) {
            throw new ConflictoException("Ya existe otra política corporativa con el título: " + req.titulo());
        }
        validarReglas(req);
        mapper.actualizar(entidad, req);
        return mapper.toResponse(repository.saveAndFlush(entidad));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        PoliticaEmpresa entidad = buscar(id);
        // Regla de negocio: no se puede eliminar una política que ya está inactiva
        if (Boolean.FALSE.equals(entidad.getActivo())) {
            throw new ReglaNegocioException("No se puede eliminar una política que ya se encuentra en estado INACTIVO.");
        }
        repository.delete(entidad);
    }

    private PoliticaEmpresa buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNotFoundException("No existe la política con id " + id));
    }

    private void validarReglas(PoliticaRequest req) {
        // Regla de negocio propia de RR.HH.:
        // La versión de una política no puede ser menor o igual a 0
        if (req.version() != null && req.version() <= 0) {
            throw new ReglaNegocioException("La versión de la política institucional debe ser mayor o igual a 1");
        }

        // Si la política está inactiva pero su categoría es 'CRITICA', no se permite
        if (Boolean.FALSE.equals(req.activo()) && "CRITICA".equalsIgnoreCase(req.categoria())) {
            throw new ReglaNegocioException("Una política clasificada como CRITICA no puede guardarse con estado inactivo");
        }
    }
}
