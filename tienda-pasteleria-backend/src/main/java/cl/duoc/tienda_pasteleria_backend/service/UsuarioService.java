package cl.duoc.tienda_pasteleria_backend.service;

import cl.duoc.tienda_pasteleria_backend.model.Usuario;
import cl.duoc.tienda_pasteleria_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email.trim().toLowerCase());
    }

    public boolean existePorEmail(String email) {
        return usuarioRepository.findByEmail(email.trim().toLowerCase()).isPresent();
    }


    public Usuario registrar(Usuario usuario) {
        usuario.setEmail(usuario.getEmail().trim().toLowerCase());

        if (usuario.getRol() == null || usuario.getRol().isBlank()) {
            usuario.setRol("CLIENTE");
        }

        // Regla de Negocio: Descuento Senior
        if (usuario.getEdad() != null && usuario.getEdad() >= 50) {
            usuario.setDescuentoSenior(true);
        } else if (usuario.getDescuentoSenior() == null) {
            usuario.setDescuentoSenior(false);
        }

        // Regla de Negocio: Comunidad Duoc UC
        if (usuario.getEmail().endsWith("@duocuc.cl")) {
            usuario.setEsEstudianteDuoc(true);
        } else if (usuario.getEsEstudianteDuoc() == null) {
            usuario.setEsEstudianteDuoc(false);
        }

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> actualizar(Long id, Usuario datos) {
        return usuarioRepository.findById(id).map(u -> {
            if (datos.getNombre() != null) u.setNombre(datos.getNombre());
            if (datos.getTelefono() != null) u.setTelefono(datos.getTelefono());
            if (datos.getDireccion() != null) u.setDireccion(datos.getDireccion());
            if (datos.getFechaNacimiento() != null) u.setFechaNacimiento(datos.getFechaNacimiento());
            if (datos.getEdad() != null) {
                u.setEdad(datos.getEdad());
                u.setDescuentoSenior(datos.getEdad() >= 50);
            }
            if (datos.getRol() != null && !datos.getRol().isBlank()) {
                u.setRol(datos.getRol());
            }
            if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
                u.setPassword(datos.getPassword());
            }
            return usuarioRepository.save(u);
        });
    }

    public boolean eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            return false;
        }
        usuarioRepository.deleteById(id);
        return true;
    }
}
