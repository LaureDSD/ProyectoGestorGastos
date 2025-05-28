package Proyecto.GestorAPI.models;

import Proyecto.GestorAPI.models.enums.ExpenseClass;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "gastos")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Spent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spentId;

    /**
     * Nombre o título del gasto.
     * No nulo ni vacío, máximo 150 caracteres.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
    @Column(nullable = false, length = 150)
    private String name;

    /**
     * Descripción adicional del gasto.
     * Opcional, máximo 500 caracteres.
     */
    @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
    @Column(length = 500)
    private String description;

    /**
     * Icono representativo (ej. nombre de clase CSS).
     * Opcional, máximo 50 caracteres.
     */
    @Size(max = 50, message = "El icono no puede superar 50 caracteres")
    @Column(length = 50)
    private String icon;

    /**
     * Fecha en la que se realizó el gasto.
     * Obligatoria.
     */
    @NotNull(message = "La fecha del gasto es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(nullable = false)
    private LocalDateTime expenseDate;

    /**
     * Importe total (sin IVA).
     * Obligatorio, mayor que 0.
     */
    @DecimalMin(value = "0.01", message = "El total debe ser mayor que 0")
    @Column(nullable = false)
    private double total;

    /**
     * Porcentaje de IVA aplicado.
     * Opcional, mínimo 0, máximo 100.
     */
    @DecimalMin(value = "0.0", message = "El IVA no puede ser negativo")
    @DecimalMax(value = "100.0", message = "El IVA no puede superar 100")
    @Column(nullable = false)
    private double iva;

    /**
     * Usuario dueño del gasto.
     * Obligatorio.
     */
    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Categoría opcional.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = true)
    private CategoryExpense category;

    /**
     * Fecha y hora de creación.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de última modificación.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Tipo de gasto.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    @NotNull(message = "El tipo de gasto es obligatorio")
    private ExpenseClass typeExpense = ExpenseClass.GASTO_GENERICO;


    public Spent(String name, String description, String icon,
                 LocalDateTime expenseDate, double total, double iva,
                 User user, CategoryExpense category,
                 LocalDateTime createdAt, LocalDateTime updatedAt,
                 ExpenseClass typeExpense) {

        this.name = name;
        this.description = description;
        this.icon = icon;
        this.expenseDate = expenseDate;
        this.total = total;
        this.iva = iva;
        this.user = user;
        this.category = category;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now();
        this.typeExpense = typeExpense != null ? typeExpense : ExpenseClass.GASTO_GENERICO;
    }

}
