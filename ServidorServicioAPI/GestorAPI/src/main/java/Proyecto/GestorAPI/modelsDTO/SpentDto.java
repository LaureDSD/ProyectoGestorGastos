package Proyecto.GestorAPI.modelsDTO;

import Proyecto.GestorAPI.models.Spent;
import java.time.format.DateTimeFormatter;

public record SpentDto(Long clienteId, String fechaCompra, String nombre, String descripcion, double total, double iva, String icono) {

    public static SpentDto from(Spent spent) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return new SpentDto(
                spent.getUser().getId(),
                formatter.format(spent.getExpenseDate()),
                spent.getName(),
                spent.getDescription(),
                spent.getTotal(),
                spent.getIva(),
                spent.getIcon()
        );
    }
}
