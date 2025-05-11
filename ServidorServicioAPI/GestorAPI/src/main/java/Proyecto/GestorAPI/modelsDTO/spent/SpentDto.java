package Proyecto.GestorAPI.modelsDTO.spent;

import Proyecto.GestorAPI.models.Spent;
import Proyecto.GestorAPI.models.enums.ExpenseClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record SpentDto(
        Long spentId,
        Long userId,
        Long categoriaId,
        LocalDateTime fechaCompra,
        String name,
        String description,
        double total,
        double iva,
        ExpenseClass typeExpense,
        String icon) {

    public static SpentDto from(Spent spent) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return new SpentDto(
                spent.getSpentId(),
                spent.getUser().getId(),
                spent.getCategory().getId(),
                spent.getExpenseDate(),
                spent.getName(),
                spent.getDescription(),
                spent.getTotal(),
                spent.getIva(),
                spent.getTypeExpense(),
                spent.getIcon()
        );
    }
}
