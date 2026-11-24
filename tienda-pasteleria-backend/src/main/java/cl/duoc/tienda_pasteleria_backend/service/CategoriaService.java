package cl.duoc.tienda_pasteleria_backend.service;

import cl.duoc.tienda_pasteleria_backend.model.Categoria;
import cl.duoc.tienda_pasteleria_backend.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> obtenerPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    public Categoria crear(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Optional<Categoria> actualizar(Long id, Categoria nueva) {
        return categoriaRepository.findById(id).map(cat -> {
            cat.setNombre(nueva.getNombre());
            cat.setDescripcion(nueva.getDescripcion());
            cat.setImagen(nueva.getImagen());
            return categoriaRepository.save(cat);
        });
    }

    public boolean eliminar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            return false;
        }
        categoriaRepository.deleteById(id);
        return true;
    }
}
