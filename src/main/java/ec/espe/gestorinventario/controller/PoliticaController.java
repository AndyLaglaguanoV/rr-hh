package ec.espe.gestorinventario.controller;

import ec.espe.gestorinventario.dto.request.PoliticaRequest;
import ec.espe.gestorinventario.dto.response.PoliticaResponse;
import ec.espe.gestorinventario.service.PoliticaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/politicas")
public class PoliticaController {

    private final PoliticaService service;

    public PoliticaController(PoliticaService service) {
        this.service = service;
    }

    @GetMapping
    public Page<PoliticaResponse> listar(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return service.listar(pageable);
    }

    @GetMapping("/activas")
    public List<PoliticaResponse> listarActivas() {
        return service.listarActivas();
    }

    @GetMapping("/buscar")
    public Page<PoliticaResponse> buscar(@RequestParam String nombre, Pageable pageable) {
        return service.buscarPorNombre(nombre, pageable);
    }

    @GetMapping("/{id}")
    public PoliticaResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    public ResponseEntity<PoliticaResponse> crear(@Valid @RequestBody PoliticaRequest req) {
        PoliticaResponse creado = service.crear(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.id())
                .toUri();
        return ResponseEntity.created(location).body(creado);
    }

    @PutMapping("/{id}")
    public PoliticaResponse actualizar(@PathVariable Long id, @Valid @RequestBody PoliticaRequest req) {
        return service.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
