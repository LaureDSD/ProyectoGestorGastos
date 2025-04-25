package Proyecto.GestorAPI.modelsDTO;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TicketResponse {
    private String establecimiento;
    private String fecha;
    private String hora;
    private Double total;
    private List<Map<String, Object>> articulos;
    private Double confianza;
}
