package Proyecto.GestorAPI.models;

import Proyecto.GestorAPI.models.enums.ExpenseClass;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "subscripciones")
@Data
@NoArgsConstructor
public class Subscription extends Spent {

    /**
     * Fecha de inicio de la subscripción. Obligatoria.
     */
    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime start;

    /**
     * Fecha de finalización. Nullable (indefinida si null).
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime end;

    /**
     * Monto acumulado. No negativo.
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "El acumulado no puede ser negativo")
    @Column(nullable = false)
    private double accumulate = 0;

    /**
     * Día de renovación, entre 1 y 31.
     */
    @Min(value = 1, message = "El día de reinicio debe ser mínimo 1")
    @Max(value = 31, message = "El día de reinicio debe ser máximo 31")
    @Column(nullable = false)
    private int restartDay;

    /**
     * Intervalo de tiempo (en días, semanas o meses, según lógica externa).
     * Debe ser positivo.
     */
    @Min(value = 1, message = "El intervalo de tiempo debe ser al menos 1")
    @Column(nullable = false)
    private int intervalTime;

    /**
     * Estado de la subscripción (activa o no).
     */
    @Column(nullable = false)
    private boolean activa;

    public Subscription(
            @NotBlank(message = "El nombre no puede estar vacío") String name,
            String description,
            String icon,
            @NotNull(message = "La fecha del gasto es obligatoria") LocalDateTime expenseDate,
            @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor que cero") double total,
            @DecimalMin(value = "0.0", message = "El IVA no puede ser negativo") double iva,
            @NotNull(message = "El usuario es obligatorio") User user,
            @NotNull(message = "La categoría es obligatoria") CategoryExpense category,
            @NotNull(message = "La fecha de creación es obligatoria") LocalDateTime createdAt,
            LocalDateTime updatedAt,
            @NotNull(message = "El tipo de gasto es obligatorio") ExpenseClass typeExpense,
            @NotNull(message = "La fecha de inicio es obligatoria") LocalDateTime startDate,
            LocalDateTime endDate,
            @Min(1) @Max(31) int restartDay,
            @Min(1) int intervalTime,
            boolean active
    ) {
        super(name, description, icon, expenseDate, total, iva, user, category, createdAt, updatedAt, typeExpense);
        this.start = startDate;
        this.end = endDate;
        this.accumulate = 0;  // inicializamos en 0 al crear, no desde parámetro
        this.restartDay = restartDay;
        this.intervalTime = intervalTime;
        this.activa = active;
    }
}
