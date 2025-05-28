package Proyecto.GestorAPI.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "categorias")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryExpense implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la categoría.
     * Obligatorio, no vacío, máximo 100 caracteres.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    @Column(nullable = false, length = 100, unique = true)
    private String name;

    /**
     * Descripción de la categoría.
     * Opcional, máximo 500 caracteres.
     */
    @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
    @Column(length = 500)
    private String description;

    /**
     * Porcentaje de IVA aplicado a esta categoría.
     * Valor entre 0 y 100.
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "El IVA mínimo es 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "El IVA máximo es 100")
    @Column(nullable = true)
    private double iva;

    /**
     * Fecha y hora de creación.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de última actualización.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Lista de gastos asociados.
     * Ignorado en JSON para evitar ciclos infinitos.
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Spent> spentList;


    /**
     * Constructor parcial para facilitar creación.
     */
    public CategoryExpense(String name, String description, double iva) {
        this.name = name;
        this.description = description;
        this.iva = iva;
    }
}
