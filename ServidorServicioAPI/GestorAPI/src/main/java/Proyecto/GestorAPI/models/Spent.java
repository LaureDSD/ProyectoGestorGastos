package Proyecto.GestorAPI.models;

import Proyecto.GestorAPI.models.enums.ExpenseClass;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "gastos")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Spent {

    //ID del gasto
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spent_id;

    // Nombre del gasto
    private String name;

    //Descripcion del gasto
    private String description;

    //Icono del gasto
    private String icon;

    //Fecha del gasto
    private LocalDateTime expenseDate;

    //Total base del gasto
    private double total;

    //Iva aplicado opcional, default 21 o categoria asignada.
    private double iva;

    //Usuario
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Categoria generica del gasto
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private categoryExpense category;

    //Fecha de creacion
    private LocalDateTime createdAt;

    //Fecha de modificacion
    private LocalDateTime updatedAt;

    //Tipo e gasto (Generico o Sub clase (Factura,Ticket,Subscripcion,Transferencia))
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private ExpenseClass typeExpense;

    //Inicializa fechas
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    //Acualiza fecha de modificacion
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
