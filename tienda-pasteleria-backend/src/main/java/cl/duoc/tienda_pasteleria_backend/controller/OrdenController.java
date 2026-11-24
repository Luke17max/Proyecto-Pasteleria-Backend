package cl.duoc.tienda_pasteleria_backend.controller;

import cl.duoc.tienda_pasteleria_backend.model.Orden;
import cl.duoc.tienda_pasteleria_backend.service.OrdenService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes")
@RequiredArgsConstructor
public class OrdenController {

    private final OrdenService ordenService;

    @GetMapping
    public List<Orden> obtenerTodas() {
        return ordenService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orden> obtenerPorId(@PathVariable Long id) {
        return ordenService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/folio/{folio}")
    public ResponseEntity<Orden> obtenerPorFolio(@PathVariable String folio) {
        return ordenService.obtenerPorFolio(folio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Orden> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ordenService.obtenerPorUsuario(usuarioId);
    }

    @PostMapping
    public ResponseEntity<Orden> crear(@Valid @RequestBody Orden orden) {
        Orden guardada = ordenService.crearOrden(orden);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    @Data
    public static class EstadoRequest {
        private String estado;
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Orden> cambiarEstado(@PathVariable Long id, @RequestBody EstadoRequest req) {
        return ordenService.cambiarEstado(id, req.getEstado())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!ordenService.eliminar(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
