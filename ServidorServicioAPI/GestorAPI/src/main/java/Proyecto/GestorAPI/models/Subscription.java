package Proyecto.GestorAPI.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "subscripciones")
@Data
@Getter
@Setter
@NoArgsConstructor
public class Subscription extends Spent {

    //Inicio de la subscripcion
    @Column(nullable = false)
    private LocalDateTime start;

    //Final de la subscripcion, vacio indifinido
    private LocalDateTime end;

    //En base a fecha de inicio,intervalo y dia ,calcular el precio acumulado actual,
    private double accumulate;

    //Que dia se renueva
    private int restartDay;

    //Cada cuanto tiempo se reneuva
    private int intervalTime;

    //Indicar si esta activada o no
    private boolean activa;
}
