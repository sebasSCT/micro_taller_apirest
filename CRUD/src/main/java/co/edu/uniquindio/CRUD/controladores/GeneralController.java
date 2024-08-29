package co.edu.uniquindio.CRUD.controladores;

import co.edu.uniquindio.CRUD.dtos.general.MensajeDTO;
import co.edu.uniquindio.CRUD.dtos.usuario.ItemUsuarioDTO;
import co.edu.uniquindio.CRUD.dtos.usuario.PageItemUsuarioDTO;
import co.edu.uniquindio.CRUD.excepciones.NoHayUsuariosException;
import co.edu.uniquindio.CRUD.servicios.interfaces.GeneralService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/general")
@Tag(name = "General", description = "Controlador para operaciones generales")
public class GeneralController {

    private final GeneralService generalService;

    /**
     Respuestas informativas (100-199):

     100 Continue
     101 Switching Protocols

     Respuestas satisfactorias (200-299):

     200 OK
     201 Created
     204 No Content

     Redirecciones (300-399):

     301 Moved Permanently
     302 Found
     304 Not Modified

     Errores del cliente (400-499):

     400 Bad Request
     401 Unauthorized
     403 Forbidden
     404 Not Found
     405 Method Not Allowed

     Errores del servidor (500-599):

     500 Internal Server Error
     501 Not Implemented
     503 Service Unavailable

     Pageable:

     pageNumber: 0 (indica que es la primera página)
     pageSize: 2 (muestra 2 elementos por página)
     sort: Información sobre el ordenamiento (en este caso, no se aplica ningún orden)
     offset: 0 (el desplazamiento desde el inicio de los datos)
     paged: true (indica que la respuesta está paginada)
     unpaged: false (lo contrario de paged)

     Información general de la paginación:

     last: true (indica que es la última página)
     totalElements: 2 (total de elementos en toda la colección)
     totalPages: 1 (número total de páginas)
     size: 2 (tamaño de la página actual)
     number: 0 (número de la página actual, empezando desde 0)
     first: true (indica que es la primera página)
     numberOfElements: 2 (número de elementos en la página actual)
     empty: false (indica que la página no está vacía)

     Sort:

     empty: true
     sorted: false
     unsorted: true
     (Estos campos indican que no se ha aplicado ningún ordenamiento a los resultados)

     */

    @Operation(summary = "Listar todos los usuarios",
            description = "Obtiene una lista paginada de los usuarios registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = PageItemUsuarioDTO.class)))
    @ApiResponse(responseCode = "404", description = "No se encontraron usuarios",
            content = @Content(schema = @Schema(implementation = MensajeDTO.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = MensajeDTO.class)))
    @GetMapping("/usuarios")
    public ResponseEntity<?> listarUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size) throws Exception {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ItemUsuarioDTO> usuariosPage = generalService.listarUsuarios(pageable);
            return ResponseEntity.ok(usuariosPage);
        } catch (NoHayUsuariosException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MensajeDTO<>(true, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MensajeDTO<>(true, e.getMessage()));
        }
    }
}