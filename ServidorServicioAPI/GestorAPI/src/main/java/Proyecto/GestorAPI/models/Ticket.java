package Proyecto.GestorAPI.models;

import Proyecto.GestorAPI.models.enums.ExpenseClass;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "tickets")
public class Ticket extends Spent {

    /**
     * Nombre de la tienda donde se realizó la compra.
     * No puede ser nulo ni vacío, y longitud máxima 100 caracteres.
     */
    @NotBlank(message = "El nombre de la tienda no debe estar vacío")
    @Size(max = 100, message = "El nombre de la tienda no puede superar 100 caracteres")
    @Column(name = "store", nullable = true, length = 100)
    private String store;

    /**
     * Lista de productos en formato JSON.
     * Permitimos que sea nullable (por si no se tiene detalle de productos).
     * La longitud máxima depende de la base (en MySQL tipo JSON no aplica longitud,
     * pero en otros motores sí).
     *
     * Aquí no validamos el contenido JSON con anotaciones, lo ideal sería validarlo
     * a nivel de servicio o mediante un validador personalizado.
     */
    @Column(name = "products_json", columnDefinition = "JSON", nullable = false)
    private String productsJSON;

    public Ticket(
            @NotBlank(message = "El nombre no puede estar vacío") @Size(max = 100) String name,
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
            @NotBlank(message = "El nombre de la tienda no puede estar vacío") @Size(max = 100) String store,
            String productsJSON
    ) {
        super(name, description, icon, expenseDate, total, iva, user, category, createdAt, updatedAt, typeExpense);
        this.store = store;
        this.productsJSON = productsJSON;
    }
}
