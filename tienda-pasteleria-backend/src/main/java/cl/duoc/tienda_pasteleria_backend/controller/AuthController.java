package cl.duoc.tienda_pasteleria_backend.controller;

import cl.duoc.tienda_pasteleria_backend.model.Usuario;
import cl.duoc.tienda_pasteleria_backend.repository.UsuarioRepository;
import cl.duoc.tienda_pasteleria_backend.security.JwtService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Email y contraseña son obligatorios");
            return ResponseEntity.badRequest().body(error);
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailAndPassword(
                request.getEmail().trim().toLowerCase(),
                request.getPassword()
        );

        if (usuarioOpt.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Correo o contraseña incorrectos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        Usuario u = usuarioOpt.get();

        // Generamos el token JWT firmado con el email, rol e ID del usuario
        String token = jwtService.generateToken(u.getEmail(), u.getRol(), u.getId());

        Map<String, Object> usuarioData = new HashMap<>();
        usuarioData.put("id", u.getId());
        usuarioData.put("nombre", u.getNombre());
        usuarioData.put("email", u.getEmail());
        usuarioData.put("rol", u.getRol());
        usuarioData.put("telefono", u.getTelefono());
        usuarioData.put("direccion", u.getDireccion());
        usuarioData.put("fechaNacimiento", u.getFechaNacimiento());
        usuarioData.put("edad", u.getEdad());
        usuarioData.put("descuentoSenior", u.getDescuentoSenior());
        usuarioData.put("esEstudianteDuoc", u.getEsEstudianteDuoc());

        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("usuario", usuarioData);

        return ResponseEntity.ok(resp);
    }
}
