package Proyecto.GestorAPI.modelsDTO.subscription;

import Proyecto.GestorAPI.models.Subscription;
import java.time.format.DateTimeFormatter;

public record SubscriptionDto(Long clienteId, String fechaCompra, String nombre, String descripcion, double total, double iva, String icono, String start, String end, double accumulate, int restartDay, int intervalTime, boolean activa) {

    public static SubscriptionDto from(Subscription subscription) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return new SubscriptionDto(
                //Id
                subscription.getUser().getId(),
                formatter.format(subscription.getExpenseDate()),
                //Base
                subscription.getName(),
                subscription.getDescription(),
                subscription.getTotal(),
                subscription.getIva(),
                subscription.getIcon(),
                //Extra
                subscription.getStart() != null ? formatter.format(subscription.getStart()) : null,
                subscription.getEnd() != null ? formatter.format(subscription.getEnd()) : null,
                subscription.getAccumulate(),
                subscription.getRestartDay(),
                subscription.getIntervalTime(),
                subscription.isActiva()
        );
    }
}
