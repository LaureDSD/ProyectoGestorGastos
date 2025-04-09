package Proyecto.GestorAPI.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Representa una categoría de gasto dentro del sistema.
 *
 * Cada categoría contiene información básica como nombre, descripción y porcentaje de IVA aplicado.
 * Además, está relacionada con una lista de gastos individuales asociados a dicha categoría.
 *
 * Esta clase se encuentra mapeada a la tabla "categorias" en la base de datos.
 */
@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@Table(name = "categorias")
public class CategoryExpense implements Serializable {

    /**
     * Identificador único de la categoría.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre identificativo de la categoría.
     */
    private String name;

    /**
     * Descripción detallada de la categoría, útil para dar contexto o ejemplos de uso.
     */
    private String description;

    /**
     * Porcentaje de IVA que se aplica por defecto a los gastos de esta categoría.
     */
    private double iva;

    /**
     * Fecha y hora en la que se creó el registro por primera vez.
     * Se asigna automáticamente al persistir.
     */
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de la última modificación del registro.
     * Se actualiza automáticamente antes de cada cambio.
     */
    private LocalDateTime updatedAt;

    /**
     * Lista de gastos asociados a esta categoría.
     * Se ignora en la serialización JSON para evitar referencias cíclicas.
     */
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Spent> spentList;

    /**
     * Método de ciclo de vida que se ejecuta antes de insertar un nuevo registro en la base de datos.
     * Establece los campos de creación y actualización al momento actual.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Método de ciclo de vida que se ejecuta antes de actualizar un registro existente.
     * Actualiza el campo de modificación con la fecha y hora actual.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Constructor personalizado para inicializar una categoría con su nombre y descripción.
     *
     * @param nombre      Nombre de la categoría.
     * @param descripcion Descripción de la categoría.
     */
    public CategoryExpense(String nombre, String descripcion) {
        this.name = nombre;
        this.description = descripcion;
    }
}
