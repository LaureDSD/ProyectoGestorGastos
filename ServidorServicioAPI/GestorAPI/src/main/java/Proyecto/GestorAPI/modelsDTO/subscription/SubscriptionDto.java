package Proyecto.GestorAPI.modelsDTO.subscription;

import Proyecto.GestorAPI.models.Subscription;
import Proyecto.GestorAPI.models.enums.ExpenseClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record SubscriptionDto(Long clienteId,
                              LocalDateTime fechaCompra,
                              String name,
                              String description,
                              double total,
                              double iva,
                              String icon,
                              LocalDateTime start,
                              LocalDateTime end,
                              double accumulate,
                              int restartDay,
                              int intervalTime,
                              ExpenseClass typeExpense,
                              boolean activa) {

    public static SubscriptionDto from(Subscription subscription) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return new SubscriptionDto(
                //Id
                subscription.getUser().getId(),
                subscription.getExpenseDate(),
                //Base
                subscription.getName(),
                subscription.getDescription(),
                subscription.getTotal(),
                subscription.getIva(),
                subscription.getIcon(),
                //Extra
                subscription.getStart(),
                subscription.getEnd(),
                subscription.getAccumulate(),
                subscription.getRestartDay(),
                subscription.getIntervalTime(),
                subscription.getTypeExpense(),
                subscription.isActiva()
        );
    }
}
