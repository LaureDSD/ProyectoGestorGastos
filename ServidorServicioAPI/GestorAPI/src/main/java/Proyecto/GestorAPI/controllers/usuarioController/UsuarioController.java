package Proyecto.GestorAPI.controllers.usuarioController;

import Proyecto.GestorAPI.controllers.securityController.CensorController;
import Proyecto.GestorAPI.models.usuario.TipoUsuario;
import Proyecto.GestorAPI.models.usuario.Usuario;
import Proyecto.GestorAPI.modelsDTO.UsuarioDTO;
import Proyecto.GestorAPI.services.UsuarioService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuario")
@Tag(name = "Gestión de Usuarios", description = "Operaciones CRUD para la gestión de usuarios")
@SecurityRequirement(name = "Bearer Authentication")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(
            summary = "Listar usuarios",
            description = "Obtiene todos los usuarios o filtra por tipo si se especifica",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UsuarioDTO.class)))),
                    @ApiResponse(responseCode = "401", description = "No autorizado"),
                    @ApiResponse(responseCode = "403", description = "Permisos insuficientes")
            }
    )
    public ResponseEntity<Object> obtenerListaUsuarios(
            @Parameter(description = "Tipo de usuario para filtrar",
                    example = "ROLE_USUARIO",
                    schema = @Schema(implementation = TipoUsuario.class))
            @RequestParam(required = false) TipoUsuario tipo) {
        try {
            if (tipo == null) {
                return ResponseEntity.ok(conversorListaUsuarioDTO(usuarioService.getAll()));
            } else {
                return ResponseEntity.ok(conversorListaUsuarioDTO(usuarioService.getByTipoUsuario(tipo)));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener usuarios: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener usuario por ID",
            description = "Recupera un usuario específico por su ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                            content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
                    @ApiResponse(responseCode = "401", description = "No autorizado")
            }
    )
    public ResponseEntity<Object> obtenerUsuario(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Long id) {
        try {
            return ResponseEntity.ok(conversorUsuarioDTO(usuarioService.getByID(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener usuario: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(
            summary = "Crear usuario",
            description = "Crea un nuevo usuario (Requiere permisos de administrador)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario creado",
                            content = @Content(schema = @Schema(implementation = Usuario.class))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "401", description = "No autorizado"),
                    @ApiResponse(responseCode = "403", description = "Permisos insuficientes")
            }
    )
    public ResponseEntity<Object> guardarUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del usuario a crear",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Usuario.class)))
            @RequestBody Usuario usuarioGuardar) {
        try {
            return ResponseEntity.ok(usuarioService.setItem(usuarioGuardar));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear usuario: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza los datos de un usuario existente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario actualizado",
                            content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "401", description = "No autorizado"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }
    )
    public ResponseEntity<Object> actualizarUsuario(
            @Parameter(description = "ID del usuario a actualizar", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del usuario",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Usuario.class)))
            @RequestBody Usuario usuarioActualizar) {
        if (usuarioActualizar.getUsuario_id().equals(id)) {
            try {
                return ResponseEntity.ok(conversorUsuarioDTO(usuarioService.setItem(usuarioActualizar)));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error al actualizar usuario: " + e.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("El ID proporcionado no coincide con el ID del usuario.");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario del sistema (Requiere permisos de administrador)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario eliminado"),
                    @ApiResponse(responseCode = "401", description = "No autorizado"),
                    @ApiResponse(responseCode = "403", description = "Permisos insuficientes"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }
    )
    public ResponseEntity<Object> borrarUsuario(
            @Parameter(description = "ID del usuario a eliminar", example = "1")
            @PathVariable Long id) {
        try {
            usuarioService.deleteByID(id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar usuario: " + e.getMessage());
        }
    }

    // Conversor de lista de Usuario a lista de UsuarioDTO
    public  List<UsuarioDTO> conversorListaUsuarioDTO(List<Usuario> listaUsuarios) {
        return listaUsuarios.stream()
                .map(this::conversorUsuarioDTO)
                .collect(Collectors.toList());
    }

    // Conversor de Usuario a UsuarioDTO
    public  UsuarioDTO conversorUsuarioDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getUsuario_id());
        usuarioDTO.setImagen(usuario.getImagen_perfil());
        usuarioDTO.setNombrePublico(usuario.getNombre_usuario_pub());
        usuarioDTO.setNombrePrivado(CensorController.ocultarNumero(usuario.getNombre_usuario_priv(), 2));
        usuarioDTO.setCorreo(CensorController.ocultarEmail(usuario.getCorreo(), 3));
        usuarioDTO.setContrasena(CensorController.ocultarNumero(usuario.getContraseña(), 1));
        usuarioDTO.setConexion(usuario.getUltima_conexion());
        usuarioDTO.setTipoUsuario( usuario.getTipoUsuario() );
        usuarioDTO.setFecha_creacion(usuario.getFecha_creacion());
        usuarioDTO.setEstado(usuario.isEstado_cuenta());
        return usuarioDTO;
    }
}