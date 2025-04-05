package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.models.Ticket;
import Proyecto.GestorAPI.modelsDTO.TicketDto;
import Proyecto.GestorAPI.services.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping
    public List<Object> getTickets(@RequestParam(value = "clienteId", required = false) Long clienteId) {
        List<Ticket> tickets = null; //(clienteId == null) ? ticketService.getTickets() : ticketService.getTicketsByClienteId(clienteId);
        return tickets.stream()
                .map(TicketDto::from)
                .collect(Collectors.toList());
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TicketDto createTicket(@Valid @RequestBody Ticket ticket) {
        return TicketDto.from(ticketService.saveTicket(ticket));
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @DeleteMapping("/{ticketId}")
    public TicketDto deleteTicket(@PathVariable Long ticketId) {
        Ticket ticket = ticketService.validateAndGetTicket(ticketId);
        ticketService.deleteTicket(ticket);
        return TicketDto.from(ticket);
    }
}
