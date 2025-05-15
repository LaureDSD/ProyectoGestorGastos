package Proyecto.GestorAPI.modelsDTO.ticket;

import lombok.Data;

import java.util.List;

@Data
public class ProductTicketResponse {
    private String nombre;
    private String cantidad;
    private double precio;
    private double subtotal;
    private List<String> categorias;
}
