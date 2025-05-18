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
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Ticket Management (Verify control)", description = "Operation con tickets")
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;
    private final CategoryExpenseService categoriaService;

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

    @GetMapping("/{ticketId}")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Obtener un ticket por ID"
    )
    public ResponseEntity<TicketDto> getTicketById(
            @PathVariable Long ticketId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        //System.out.println("Buscando ticket con ID: " + ticketId);
        Ticket ticket = ticketService.getByID(ticketId).orElse(null);
        System.out.println("Resultado: " + TicketDto.from(ticket));
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }

        if (user.getRole() != RoleServer.ADMIN && !ticket.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(TicketDto.from(ticket));
    }

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
        if(user.getRole()!= RoleServer.ADMIN){
            //si no es admin
            ticket = ticketService.mappingCreateTicket(request, user.getId());
        }else{
            //si es admin
            ticket = ticketService.mappingCreateTicket(request,(clienteId != null) ? clienteId : user.getId());
        }

        if (ticket.getProductsJSON() == null || ticket.getProductsJSON().isEmpty()) {
            return ResponseEntity.badRequest().body("El campo de productos no puede estar vacío.");
        }

        //Creacion
        Ticket createdTicket = ticketService.setItem(ticket);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TicketDto.from(createdTicket));
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
            @Valid @RequestBody UpdateTicketRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Ticket ticket = ticketService.getByID(ticketId).orElse(null);

        // Verificación de existencia
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }

        // Verificación de propiedad
        if (user.getRole() != RoleServer.ADMIN && !ticket.getUser().getId().equals(user.getId()) ||
            !ticket.getSpentId().equals(request.getSpentId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        //Mapping
         ticket = ticketService.mappingUpdateTicket(request, ticket);

        //Save
        Ticket updatedTicket = ticketService.setItem(ticket);

        //Return
        return ResponseEntity.ok(TicketDto.from(updatedTicket));
    }



}