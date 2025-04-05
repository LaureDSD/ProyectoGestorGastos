package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.services.TicketService;
import Proyecto.GestorAPI.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/public")
public class PublicController {

    private final UserService userService;
    private final TicketService ticketService;

    @GetMapping("/numberOfUsers")
    public Integer getNumberOfUsers() {
        return userService.getUsers().size();
    }

    @GetMapping("/numberOfTickets")
    public Integer getNumberOfTickets() {
        return ticketService.getTickets().size();
    }

}
