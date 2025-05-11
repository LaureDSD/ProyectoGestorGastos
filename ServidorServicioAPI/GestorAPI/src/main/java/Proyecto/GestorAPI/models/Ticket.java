package Proyecto.GestorAPI.models;

import Proyecto.GestorAPI.models.enums.ExpenseClass;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad que representa un gasto de tipo ticket de compra.
 *
 * Hereda de {@link Spent} y añade información específica como la tienda donde se realizó
 * la compra y el detalle de los productos adquiridos, almacenados en formato JSON.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tickets")
public class Ticket extends Spent {

    /**
     * Nombre de la tienda donde se realizó la compra.
     */
    private String store;

    /**
     * Lista de productos asociados al ticket, representada en formato JSON.
     *
     * Se espera un formato estructurado, por ejemplo:
     * <pre>
     * [
     *   {
     *     "nombre": "Leche",
     *     "categorias": ["Alimentos", "Lácteos"],
     *     "cantidad": 2,
     *     "precio": 1.25
     *   },
     *   {
     *     "nombre": "Pan",
     *     "categorias": ["Alimentos"],
     *     "cantidad": 1,
     *     "precio": 0.95
     *   }
     * ]
     * </pre>
     * Este campo puede ser utilizado para análisis, visualización o conversión
     * a estructuras de objetos mediante un parser (como Jackson).
     */
    @Column(columnDefinition = "JSON")
    private String productsJSON;

    public Ticket(String name, String description, String icon, LocalDateTime expenseDate, double total, double iva,
                  User user, CategoryExpense category, LocalDateTime createdAt, LocalDateTime updatedAt,
                  ExpenseClass typeExpense, String store, String productsJSON) {
        super(name, description, icon, expenseDate, total, iva, user, category, createdAt, updatedAt, typeExpense);
        this.store = store;
        this.productsJSON = productsJSON;
    }


}
