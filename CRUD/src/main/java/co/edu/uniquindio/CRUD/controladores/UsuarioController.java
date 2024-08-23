package co.edu.uniquindio.CRUD.controladores;

import co.edu.uniquindio.CRUD.dtos.general.MensajeDTO;
import co.edu.uniquindio.CRUD.dtos.usuario.ActualizarUsuarioDTO;
import co.edu.uniquindio.CRUD.dtos.usuario.DetalleUsuarioDTO;
import co.edu.uniquindio.CRUD.servicios.interfaces.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "Usuario", description = "Controlador para operaciones propias del usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Actualizar perfil de usuario",
            description = "Actualiza la información del perfil de un usuario existente")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente",
            content = @Content(schema = @Schema(implementation = MensajeDTO.class)))
    @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @PutMapping("/editar-perfil")
    public ResponseEntity<MensajeDTO<String>> actualizarUsuario(
            @Parameter(description = "Datos actualizados del usuario", required = true)
            @Valid @RequestBody ActualizarUsuarioDTO actualizarUsuarioDTO) throws Exception {
        usuarioService.actualizarUsuario(actualizarUsuarioDTO);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Usuario actualizado correctamente"));
    }

    @Operation(summary = "Eliminar cuenta de usuario",
            description = "Elimina la cuenta de un usuario del sistema")
    @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente",
            content = @Content(schema = @Schema(implementation = MensajeDTO.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "403", description = "No autorizado para eliminar este usuario")
    @DeleteMapping("/eliminar/{codigo}")
    public ResponseEntity<MensajeDTO<String>> eliminarCuenta(
            @Parameter(description = "Código del usuario a eliminar", required = true)
            @PathVariable String codigo) throws Exception {
        usuarioService.eliminarUsuario(codigo);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Usuario eliminado correctamente"));
    }

    @Operation(summary = "Obtener detalles de usuario",
            description = "Obtiene los detalles de un usuario específico")
    @ApiResponse(responseCode = "200", description = "Detalles de usuario obtenidos exitosamente",
            content = @Content(schema = @Schema(implementation = MensajeDTO.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "403", description = "No autorizado para ver este usuario")
    @GetMapping("/obtener/{codigo}")
    public ResponseEntity<MensajeDTO<DetalleUsuarioDTO>> obtenerCliente(
            @Parameter(description = "Código del usuario", required = true)
            @PathVariable String codigo) throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, usuarioService.obtenerUsuario(codigo)));
    }
}