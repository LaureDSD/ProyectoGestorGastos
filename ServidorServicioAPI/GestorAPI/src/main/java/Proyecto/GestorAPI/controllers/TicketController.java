package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.models.Ticket;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.CreateTicketRequest;
import Proyecto.GestorAPI.modelsDTO.TicketDto;
import Proyecto.GestorAPI.services.CategoryService;
import Proyecto.GestorAPI.services.TicketService;
import Proyecto.GestorAPI.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Ticket Management", description = "Operaciones sobre tickets")
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;
    private final CategoryService categoriaService;

    @GetMapping("/")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Obtener todos los tickets filtrados por clienteId (opcional)"
    )
    public ResponseEntity<List<TicketDto>> getTickets(
            @RequestParam(value = "clienteId", required = false) Long clienteId) {

        List<Ticket> tickets = (clienteId != null)
                ? ticketService.getTicketsByClienteId(clienteId)
                : ticketService.getTickets();

        if (tickets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

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
            @Valid @RequestBody CreateTicketRequest request) {

        // Crear el ticket con referencias por ID
        Ticket ticket = new Ticket();
        ticket.setUser(userService.getUserById(request.userId()).orElse(new User()));
        ticket.setCategoria(categoriaService.getCategoriaById(request.categoriaId()));
        ticket.setFechaCompra(request.fechaCompra());
        ticket.setTotal(request.total());
        ticket.setProductosJSON(request.productosJSON());
        ticket.setCreatedAt(LocalDateTime.now());

        Ticket createdTicket = ticketService.saveTicket(ticket);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TicketDto.from(createdTicket));
    }

    @DeleteMapping("/{ticketId}")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Eliminar un ticket por ID"
    )
    public ResponseEntity<Void> deleteTicket(@PathVariable Long ticketId) {
        if (!ticketService.existsById(ticketId)) {
            return ResponseEntity.notFound().build();
        }

        ticketService.deleteTicketById(ticketId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{ticketId}")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Actualizar un ticket por ID"
    )
    public ResponseEntity<TicketDto> updateTicket(
            @PathVariable Long ticketId,
            @Valid @RequestBody Ticket ticket) {

        if (ticket.getId() == null || !ticket.getId().equals(ticketId)) {
            return ResponseEntity.badRequest().build();
        }

        Ticket updatedTicket = ticketService.saveTicket(ticket);
        return ResponseEntity.ok(TicketDto.from(updatedTicket));
    }
}