package co.edu.uniquindio.CRUD.controladores;

import co.edu.uniquindio.CRUD.dtos.general.MensajeDTO;
import co.edu.uniquindio.CRUD.dtos.usuario.ItemUsuarioDTO;
import co.edu.uniquindio.CRUD.servicios.interfaces.GeneralService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/general")
@Tag(name = "General", description = "Controlador para operaciones generales")
public class GeneralController {

    private final GeneralService generalService;

    @Operation(summary = "Listar todos los usuarios",
            description = "Obtiene una lista de todos los usuarios registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = MensajeDTO.class)))
    @ApiResponse(responseCode = "404", description = "No se encontraron usuarios")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @GetMapping("/listar-usuarios")
    public ResponseEntity<MensajeDTO<List<ItemUsuarioDTO>>> listarTodos() throws Exception {
        return ResponseEntity.ok().body(new MensajeDTO<>(false, generalService.listarUsuarios()));
    }
}