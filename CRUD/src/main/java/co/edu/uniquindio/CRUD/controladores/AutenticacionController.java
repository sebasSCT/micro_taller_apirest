package co.edu.uniquindio.CRUD.controladores;

import co.edu.uniquindio.CRUD.dtos.autenticacionJwt.TokenDTO;
import co.edu.uniquindio.CRUD.dtos.general.CambioPasswordDTO;
import co.edu.uniquindio.CRUD.dtos.general.LoginDTO;
import co.edu.uniquindio.CRUD.dtos.general.MensajeDTO;
import co.edu.uniquindio.CRUD.dtos.usuario.RegistroUsuarioDTO;
import co.edu.uniquindio.CRUD.servicios.interfaces.AutenticacionService;
import co.edu.uniquindio.CRUD.servicios.interfaces.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticacion", description = "Controlador para operaciones de autenticacion y de registro")
public class AutenticacionController {

    private final AutenticacionService autenticacionServicio;
    private final UsuarioService usuarioService;

    @Operation(summary = "Iniciar sesión",
            description = "Autentica un usuario y devuelve un token JWT")
    @ApiResponse(responseCode = "200", description = "Login exitoso",
            content = @Content(schema = @Schema(implementation = MensajeDTO.class)))
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @PostMapping("/login")
    public ResponseEntity<MensajeDTO<TokenDTO>> login(
            @Parameter(description = "Credenciales de login", required = true)
            @Valid @RequestBody LoginDTO loginDTO) throws Exception {
        TokenDTO tokenDTO = autenticacionServicio.login(loginDTO);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, tokenDTO));
    }

    @Operation(summary = "Registrar nuevo usuario",
            description = "Crea un nuevo usuario en el sistema")
    @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente",
            content = @Content(schema = @Schema(implementation = MensajeDTO.class)))
    @ApiResponse(responseCode = "400", description = "Datos de registro inválidos")
    @ApiResponse(responseCode = "409", description = "El usuario ya existe")
    @PostMapping("/registrar-usuario")
    public ResponseEntity<MensajeDTO<String>> registrarse(
            @Parameter(description = "Datos del nuevo usuario", required = true)
            @Valid @RequestBody RegistroUsuarioDTO usuarioDTO) throws Exception {
        usuarioService.registrarUsuario(usuarioDTO);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Usuario registrado correctamente"));
    }

    @Operation(summary = "Cambiar contraseña",
            description = "Cambia la contraseña de un usuario existente")
    @ApiResponse(responseCode = "200", description = "Contraseña actualizada con éxito",
            content = @Content(schema = @Schema(implementation = MensajeDTO.class)))
    @ApiResponse(responseCode = "400", description = "Datos de cambio de contraseña inválidos")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @PutMapping("/cambiar-password")
    public ResponseEntity<MensajeDTO<String>> cambiarPassword(
            @Parameter(description = "Datos para cambio de contraseña", required = true)
            @Valid @RequestBody CambioPasswordDTO cambioPasswordDTO) throws Exception {
        usuarioService.cambiarPassword(cambioPasswordDTO);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Contraseña actualizada con éxito"));
    }
}