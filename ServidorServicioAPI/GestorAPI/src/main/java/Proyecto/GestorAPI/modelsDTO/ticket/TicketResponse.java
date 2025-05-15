package Proyecto.GestorAPI.modelsDTO.ticket;

import lombok.Data;

import java.util.List;

@Data
public class TicketResponse {
    private String establecimiento;
    private String fecha;
    private String hora;
    private Double total;
    private Long categoria;
    private List<ProductTicketResponse> articulos;
    private Double confianza;
    private Double iva;
}
