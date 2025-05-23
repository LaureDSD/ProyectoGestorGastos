package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.models.Ticket;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.ticket.CreateTicketRequest;
import Proyecto.GestorAPI.modelsDTO.ticket.TicketDto;
import Proyecto.GestorAPI.modelsDTO.ticket.UpdateTicketRequest;
import Proyecto.GestorAPI.config.security.CustomUserDetails;
import Proyecto.GestorAPI.config.security.RoleServer;
import Proyecto.GestorAPI.services.CategoryExpenseService;
import Proyecto.GestorAPI.services.TicketService;
import Proyecto.GestorAPI.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

/**
 * Controlador REST para gestión de tickets.
 * <p>
 * Define los endpoints para CRUD de tickets con control de acceso basado en roles y propiedad.
 * <p>
 * Se utiliza seguridad mediante token Bearer (JWT) documentada con Swagger/OpenAPI.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Ticket Management (Verify control)", description = "Operaciones relacionadas con tickets")
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;
    private final CategoryExpenseService categoriaService;

    /**
     * Obtener todos los tickets.
     * <p>
     * Si el usuario es ADMIN puede filtrar tickets por clienteId, si no es admin solo obtiene sus propios tickets.
     *
     * @param clienteId   Opcional, filtro por id de cliente (solo admins).
     * @param currentUser Usuario autenticado actual.
     * @return Lista de TicketDto o 204 No Content si no hay tickets.
     */
    @GetMapping("")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Obtener todos los tickets filtrados por clienteId (opcional para admins)"
    )
    public ResponseEntity<List<TicketDto>> getTickets(
            @RequestParam(value = "clienteId", required = false) Long clienteId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        List<Ticket> tickets = new ArrayList<>();

        // Control de acceso: si no es admin, solo obtiene sus tickets
        if (user.getRole() != RoleServer.ADMIN) {
            tickets = ticketService.getTicketsByUserId(user.getId());
        } else {
            // Si es admin, filtra por clienteId si se proporciona, sino obtiene todos
            tickets = (clienteId != null)
                    ? ticketService.getTicketsByUserId(clienteId)
                    : ticketService.getAll();
        }

        // Si no hay tickets, retorna 204 No Content
        if (tickets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Retorna la lista mapeada a DTOs
        return ResponseEntity.ok(tickets.stream()
                .map(TicketDto::from)
                .collect(Collectors.toList()));
    }

    /**
     * Obtener un ticket por su ID.
     * <p>
     * Solo el propietario o un admin pueden acceder.
     *
     * @param ticketId    ID del ticket a obtener.
     * @param currentUser Usuario autenticado actual.
     * @return TicketDto o 404 si no existe o 403 si no tiene permiso.
     */
    @GetMapping("/{ticketId}")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Obtener un ticket por ID"
    )
    public ResponseEntity<TicketDto> getTicketById(
            @PathVariable Long ticketId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        Ticket ticket = ticketService.getByID(ticketId).orElse(null);

        // Retorna 404 si no existe
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }

        // Control de acceso: si no es admin y no es propietario, retorna 403 Forbidden
        if (user.getRole() != RoleServer.ADMIN && !ticket.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Retorna el ticket mapeado a DTO
        return ResponseEntity.ok(TicketDto.from(ticket));
    }

    /**
     * Crear un nuevo ticket.
     * <p>
     * Si el usuario es admin puede crear un ticket para otro cliente especificando clienteId.
     * Se valida que el campo productos no esté vacío.
     *
     * @param clienteId   Opcional, id del cliente para el que se crea el ticket (solo admins).
     * @param request     Datos para crear el ticket.
     * @param currentUser Usuario autenticado actual.
     * @return TicketDto creado con código 201 o error 400 si productos está vacío.
     */
    @PostMapping("")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Crear un nuevo ticket"
    )
    public ResponseEntity<?> createTicket(
            @RequestParam(value = "clienteId", required = false) Long clienteId,
            @Valid @RequestBody CreateTicketRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Ticket ticket;
        if (user.getRole() != RoleServer.ADMIN) {
            // Si no es admin crea ticket para sí mismo
            ticket = ticketService.mappingCreateTicket(request, user.getId());
        } else {
            // Si es admin crea ticket para clienteId si está, sino para sí mismo
            ticket = ticketService.mappingCreateTicket(request, (clienteId != null) ? clienteId : user.getId());
        }

        // Validación: productos no puede estar vacío
        if (ticket.getProductsJSON() == null || ticket.getProductsJSON().isEmpty()) {
            return ResponseEntity.badRequest().body("El campo de productos no puede estar vacío.");
        }

        // Persistir ticket creado
        Ticket createdTicket = ticketService.setItem(ticket);

        // Retornar ticket creado con código 201 Created
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TicketDto.from(createdTicket));
    }

    /**
     * Eliminar un ticket por ID.
     * <p>
     * Solo el propietario o admin pueden eliminar.
     *
     * @param ticketId    ID del ticket a eliminar.
     * @param currentUser Usuario autenticado actual.
     * @return 204 No Content si se elimina, 404 si no existe, 400 si no es propietario.
     */
    @DeleteMapping("/{ticketId}")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Eliminar un ticket por ID"
    )
    public ResponseEntity<Void> deleteTicket(
            @PathVariable Long ticketId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Ticket ticket = ticketService.getByID(ticketId).orElse(null);

        // Verificar existencia, 404 si no existe
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }

        // Verificar propiedad o rol admin, 400 si no tiene permiso
        if (user.getRole() != RoleServer.ADMIN && !ticket.getUser().getId().equals(user.getId())) {
            return ResponseEntity.badRequest().build();
        }

        // Eliminar ticket
        ticketService.deleteByID(ticketId);

        // Retornar 204 No Content
        return ResponseEntity.noContent().build();
    }

    /**
     * Actualizar un ticket por ID.
     * <p>
     * Solo propietario o admin pueden actualizar.
     * Además, el spentId del ticket debe coincidir con el del request.
     *
     * @param ticketId    ID del ticket a actualizar.
     * @param request     Datos para actualizar el ticket.
     * @param currentUser Usuario autenticado actual.
     * @return TicketDto actualizado o 404 si no existe, 403 si no tiene permiso.
     */
    @PutMapping("/{ticketId}")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Actualizar un ticket por ID"
    )
    public ResponseEntity<TicketDto> updateTicket(
            @PathVariable Long ticketId,
            @Valid @RequestBody UpdateTicketRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Ticket ticket = ticketService.getByID(ticketId).orElse(null);

        // Verificar existencia, 404 si no existe
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }

        // Verificar propiedad y que spentId coincida, 403 Forbidden si no cumple
        if (user.getRole() != RoleServer.ADMIN && !ticket.getUser().getId().equals(user.getId()) ||
                !ticket.getSpentId().equals(request.getSpentId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Mapear datos del request al ticket existente
        ticket = ticketService.mappingUpdateTicket(request, ticket);

        // Guardar ticket actualizado
        Ticket updatedTicket = ticketService.setItem(ticket);

        // Retornar ticket actualizado
        return ResponseEntity.ok(TicketDto.from(updatedTicket));
    }

}
