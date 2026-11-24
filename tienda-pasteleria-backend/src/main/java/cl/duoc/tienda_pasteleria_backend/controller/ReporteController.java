package cl.duoc.tienda_pasteleria_backend.controller;

import cl.duoc.tienda_pasteleria_backend.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/productos")
    public Map<String, Object> reporteProductos() {
        return reporteService.generarReporteProductos();
    }

    @GetMapping("/generales")
    public Map<String, Object> reporteGenerales() {
        return reporteService.generarReporteGenerales();
    }
}
