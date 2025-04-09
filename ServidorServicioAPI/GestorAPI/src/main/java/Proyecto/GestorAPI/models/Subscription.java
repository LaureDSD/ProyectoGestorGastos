package Proyecto.GestorAPI.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Representa un gasto del tipo subscripción.
 *
 * Extiende la clase base {@link Spent} e incluye atributos específicos
 * como fecha de inicio, renovación, estado de actividad y cálculo de acumulado.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "subscripciones")
@Data
@Getter
@Setter
@NoArgsConstructor
public class Subscription extends Spent {

    /**
     * Fecha de inicio de la subscripción. Campo obligatorio.
     */
    @Column(nullable = false)
    private LocalDateTime start;

    /**
     * Fecha de finalización de la subscripción.
     * Si es null, se considera una subscripción indefinida.
     */
    private LocalDateTime end;

    /**
     * Monto acumulado calculado desde el inicio según el intervalo y frecuencia de renovación.
     */
    private double accumulate;

    /**
     * Día del mes en el que se renueva la subscripción.
     * Por ejemplo: 1 = día 1 de cada mes.
     */
    private int restartDay;

    /**
     * Intervalo de tiempo (en días, semanas o meses según lógica externa)
     * que define la frecuencia de renovación.
     */
    private int intervalTime;

    /**
     * Indica si la subscripción está actualmente activa.
     */
    private boolean activa;
}
