package Proyecto.GestorAPI.modelsDTO.spent;

import Proyecto.GestorAPI.models.enums.ExpenseClass;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SpentFullDto {
    // Comunes a todos los gastos
    private Long spentId;
    private Long userId;
    private Long categoriaId;
    private String name;
    private String description;
    private String icon;
    private LocalDateTime fechaCompra;
    private Double total;
    private Double iva;
    private ExpenseClass typeExpense;

    // Solo si es TICKET
    private String store;
    private String productsJSON;

    // Solo si es SUBSCRIPTION
    private LocalDateTime start;
    private LocalDateTime end;
    private Double accumulate;
    private Integer restartDay;
    private Integer intervalTime;
    private Boolean activa;
}
