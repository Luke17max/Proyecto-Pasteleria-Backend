package cl.duoc.tienda_pasteleria_backend.service;

import cl.duoc.tienda_pasteleria_backend.model.DetalleOrden;
import cl.duoc.tienda_pasteleria_backend.model.Orden;
import cl.duoc.tienda_pasteleria_backend.model.Usuario;
import cl.duoc.tienda_pasteleria_backend.repository.OrdenRepository;
import cl.duoc.tienda_pasteleria_backend.repository.ProductoRepository;
import cl.duoc.tienda_pasteleria_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public List<Orden> obtenerTodas() {
        return ordenRepository.findAllByOrderByIdDesc();
    }

    public Optional<Orden> obtenerPorId(Long id) {
        return ordenRepository.findById(id);
    }

    public Optional<Orden> obtenerPorFolio(String folio) {
        return ordenRepository.findByFolio(folio);
    }

    public List<Orden> obtenerPorUsuario(Long usuarioId) {
        return ordenRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }

    @Transactional
    public Orden crearOrden(Orden orden) {
        // Generar folio automático si no viene especificado
        if (orden.getFolio() == null || orden.getFolio().isBlank()) {
            long aleatorio = (long) (Math.random() * 900000) + 100000;
            orden.setFolio("MS-" + aleatorio);
        }

        // Asignar fecha y hora actual
        if (orden.getFecha() == null || orden.getFecha().isBlank()) {
            orden.setFecha(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        if (orden.getHora() == null || orden.getHora().isBlank()) {
            orden.setHora(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        }

        // Asociar usuario registrado si viene su ID
        if (orden.getUsuario() != null && orden.getUsuario().getId() != null) {
            Usuario u = usuarioRepository.findById(orden.getUsuario().getId()).orElse(null);
            orden.setUsuario(u);
        }

        // Asociar ítems y descontar inventario en base de datos
        if (orden.getItems() != null) {
            for (DetalleOrden item : orden.getItems()) {
                item.setOrden(orden);
                if (item.getCodigoProducto() != null) {
                    productoRepository.findByCodigo(item.getCodigoProducto()).ifPresent(prod -> {
                        int nuevoStock = Math.max(0, prod.getStock() - item.getCantidad());
                        prod.setStock(nuevoStock);
                        productoRepository.save(prod);
                    });
                }
            }
        }

        return ordenRepository.save(orden);
    }

    public Optional<Orden> cambiarEstado(Long id, String nuevoEstado) {
        return ordenRepository.findById(id).map(orden -> {
            if (nuevoEstado != null && !nuevoEstado.isBlank()) {
                orden.setEstado(nuevoEstado);
            }
            return ordenRepository.save(orden);
        });
    }

    public boolean eliminar(Long id) {
        if (!ordenRepository.existsById(id)) {
            return false;
        }
        ordenRepository.deleteById(id);
        return true;
    }
}
