package ec.espe.gestorinventario.controller;

import ec.espe.gestorinventario.dto.request.LoginRequestDTO;
import ec.espe.gestorinventario.dto.response.LoginResponseDTO;
import ec.espe.gestorinventario.entity.Rol;
import ec.espe.gestorinventario.entity.Usuario;
import ec.espe.gestorinventario.repository.UsuarioRepository;
import ec.espe.gestorinventario.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElse(null);

        if (usuario == null || !passwordEncoder.matches(request.password(), usuario.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var authorities = usuario.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getNombre()))
                .collect(Collectors.toList());

        User userDetails = new User(usuario.getEmail(), usuario.getPasswordHash(), authorities);
        String token = jwtService.generarToken(userDetails);

        List<String> rolesStr = usuario.getRoles().stream().map(Rol::getNombre).toList();

        return ResponseEntity.ok(new LoginResponseDTO(
                token,
                usuario.getEmail(),
                usuario.getNombreCompleto(),
                usuario.getDiasVacacionesDisponibles(),
                rolesStr
        ));
    }
}
