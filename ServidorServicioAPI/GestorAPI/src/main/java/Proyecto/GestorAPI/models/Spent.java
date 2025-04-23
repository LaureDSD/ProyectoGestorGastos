package Proyecto.GestorAPI.models;

import Proyecto.GestorAPI.models.enums.ExpenseClass;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad que representa un gasto registrado por un usuario.
 *
 * Los gastos pueden pertenecer a diferentes tipos como tickets, facturas, suscripciones o transferencias.
 * Cada gasto está relacionado con un usuario y opcionalmente con una categoría.
 * Se permite la herencia para extender tipos de gasto especializados.
 */
@Entity
@Table(name = "gastos")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Spent {

    /**
     * Identificador único del gasto.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spent_id;

    /**
     * Nombre o título del gasto.
     */
    private String name;

    /**
     * Descripción adicional del gasto.
     */
    private String description;

    /**
     * Icono representativo del gasto (puede ser una clase CSS, nombre de imagen, etc.).
     */
    private String icon;

    /**
     * Fecha en la que se realizó el gasto.
     */
    private LocalDateTime expenseDate;

    /**
     * Importe total del gasto (sin IVA incluido).
     */
    private double total;

    /**
     * Porcentaje de IVA aplicado al gasto.
     * Si no se especifica, se puede tomar el valor por defecto o el de la categoría asignada.
     */
    private double iva;

    /**
     * Usuario al que pertenece este gasto.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Categoría opcional a la que pertenece el gasto.
     */
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoryExpense category;

    /**
     * Fecha y hora en que se creó el registro.
     * Se establece automáticamente al persistir.
     */
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de la última modificación del registro.
     * Se actualiza automáticamente antes de cada modificación.
     */
    private LocalDateTime updatedAt;

    /**
     * Tipo de gasto: puede ser un gasto genérico o una subclase especializada como Ticket, Factura, etc.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private ExpenseClass typeExpense = ExpenseClass.GASTO_GENERICO;

    /**
     * Callback de JPA que se ejecuta automáticamente antes de insertar un nuevo gasto.
     * Establece las fechas de creación y modificación.
     */
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Callback de JPA que se ejecuta automáticamente antes de actualizar un gasto existente.
     * Actualiza la fecha de modificación.
     */
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
