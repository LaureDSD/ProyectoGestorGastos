package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.models.Ticket;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.CreateTicketRequest;
import Proyecto.GestorAPI.modelsDTO.TicketDto;
import Proyecto.GestorAPI.security.CustomUserDetails;
import Proyecto.GestorAPI.security.RoleServer;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Ticket Management (Restricted access)", description = "Operation con tickets")
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;
    private final CategoryExpenseService categoriaService;

    @GetMapping("/")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Obtener todos los tickets filtrados por clienteId (opcional para admins)"
    )
    public ResponseEntity<List<TicketDto>> getTickets(
            @RequestParam(value = "clienteId", required = false) Long clienteId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        List<Ticket> tickets = new ArrayList<>();

        //Verificacion propiedad
        if(user.getRole()!= RoleServer.ADMIN){
            //si no es admin
            tickets = ticketService.getTicketsByUserId(user.getId());
        }else{
            //si es admin
            tickets = (clienteId != null)
                    ? ticketService.getTicketsByUserId(clienteId)
                    : ticketService.getAll();
        }

        //Verificacionde de existencia
        if (tickets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        //Devolucion
        return ResponseEntity.ok(tickets.stream()
                .map(TicketDto::from)
                .collect(Collectors.toList()));
    }

    @PostMapping("/")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Crear un nuevo ticket"
    )
    public ResponseEntity<TicketDto> createTicket(
            @RequestParam(value = "clienteId", required = false) Long clienteId,
            @Valid @RequestBody CreateTicketRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Ticket ticket;

        if(user.getRole()!= RoleServer.ADMIN){
            //si no es admin
            ticket = mappingTicket(request, user.getId());
        }else{
            //si es admin
            ticket = mappingTicket(request,(clienteId != null) ? clienteId : user.getId());
        }
        //Creacion
        Ticket createdTicket = ticketService.setItem(ticket);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TicketDto.from(createdTicket));
    }

    private Ticket mappingTicket(CreateTicketRequest request , Long clienteId) {
        // Crear el ticket con referencias por ID
        Ticket ticket = new Ticket();

        ticket.setUser(userService.getUserById(clienteId).orElse(new User()));
        ticket.setCategory(categoriaService.getByID(request.categoriaId()).orElse(null));
        ticket.setExpenseDate(request.fechaCompra());
        ticket.setTotal(request.total());
        ticket.setProductsJSON(request.productosJSON());
        ticket.setCreatedAt(LocalDateTime.now());
        return  ticket;
    }

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
        //Verificacionde existencia
        if ( ticket == null){
            return ResponseEntity.notFound().build();
        }
        //Verificacion de propiedad
        if(user.getRole() != RoleServer.ADMIN && !ticket.getUser().getId().equals(user.getId())){
            return ResponseEntity.badRequest().build();
        }
        //Eliminacion
        ticketService.deleteByID(ticketId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{ticketId}")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Actualizar un ticket por ID"
    )
    public ResponseEntity<TicketDto> updateTicket(
            @PathVariable Long ticketId,
            @Valid @RequestBody Ticket request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Ticket ticket;
        //verificacion de propiedad
        if(user.getRole() != RoleServer.ADMIN && !request.getUser().getId().equals(user.getId())){
            return ResponseEntity.badRequest().build();
        }
        //Verificacion complememntaria
        if (request.getSpent_id() == null || !request.getSpent_id().equals(ticketId)) {
            return ResponseEntity.badRequest().build();
        }else {
            ticket = ticketService.getByID(ticketId).orElse(null);
        }
        //Veriificacion existencia
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        //Actualizacion
        Ticket updatedTicket = ticketService.setItem(request);
        return ResponseEntity.ok(TicketDto.from(updatedTicket));
    }
}