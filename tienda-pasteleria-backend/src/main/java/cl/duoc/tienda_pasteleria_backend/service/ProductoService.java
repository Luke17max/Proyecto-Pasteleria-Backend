package cl.duoc.tienda_pasteleria_backend.service;

import cl.duoc.tienda_pasteleria_backend.model.Categoria;
import cl.duoc.tienda_pasteleria_backend.model.Producto;
import cl.duoc.tienda_pasteleria_backend.repository.CategoriaRepository;
import cl.duoc.tienda_pasteleria_backend.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Optional<Producto> obtenerPorCodigo(String codigo) {
        return productoRepository.findByCodigo(codigo);
    }

    public List<Producto> obtenerPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    public List<Producto> obtenerOfertas() {
        return productoRepository.findByEnOfertaTrue();
    }

    public List<Producto> obtenerCriticos(Integer limite) {
        int umbral = (limite != null) ? limite : 5;
        return productoRepository.findByStockLessThanEqual(umbral);
    }

    public Producto crear(Producto producto) {
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            Categoria cat = categoriaRepository.findById(producto.getCategoria().getId()).orElse(null);
            producto.setCategoria(cat);
        }
        return productoRepository.save(producto);
    }

    public Optional<Producto> actualizar(Long id, Producto nuevo) {
        return productoRepository.findById(id).map(prod -> {
            prod.setCodigo(nuevo.getCodigo());
            prod.setNombre(nuevo.getNombre());
            prod.setDescripcion(nuevo.getDescripcion());
            prod.setPrecio(nuevo.getPrecio());
            prod.setStock(nuevo.getStock());
            prod.setStockCritico(nuevo.getStockCritico());
            prod.setEnOferta(nuevo.getEnOferta());
            prod.setPorcentajeDescuento(nuevo.getPorcentajeDescuento());
            prod.setImagen(nuevo.getImagen());
            if (nuevo.getCategoria() != null && nuevo.getCategoria().getId() != null) {
                categoriaRepository.findById(nuevo.getCategoria().getId()).ifPresent(prod::setCategoria);
            }
            return productoRepository.save(prod);
        });
    }

    public boolean eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            return false;
        }
        productoRepository.deleteById(id);
        return true;
    }
}
