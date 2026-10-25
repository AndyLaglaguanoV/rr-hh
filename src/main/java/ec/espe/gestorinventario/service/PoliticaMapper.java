package ec.espe.gestorinventario.service;

import ec.espe.gestorinventario.dto.request.PoliticaRequest;
import ec.espe.gestorinventario.dto.response.PoliticaResponse;
import ec.espe.gestorinventario.entity.PoliticaEmpresa;
import org.springframework.stereotype.Component;

@Component
public class PoliticaMapper {

    public PoliticaEmpresa toEntity(PoliticaRequest req) {
        PoliticaEmpresa e = new PoliticaEmpresa();
        actualizar(e, req);
        return e;
    }

    public void actualizar(PoliticaEmpresa e, PoliticaRequest req) {
        e.setTitulo(req.titulo().trim());
        e.setCategoria(req.categoria().trim().toUpperCase());
        e.setContenido(req.contenido().trim());
        e.setActivo(req.activo() != null ? req.activo() : true);
        e.setVersion(req.version() != null ? req.version() : 1);
    }

    public PoliticaResponse toResponse(PoliticaEmpresa e) {
        return new PoliticaResponse(
                e.getId(),
                e.getTitulo(),
                e.getCategoria(),
                e.getContenido(),
                e.getActivo(),
                e.getVersion(),
                e.getCreadoEn(),
                e.getActualizadoEn()
        );
    }
}
