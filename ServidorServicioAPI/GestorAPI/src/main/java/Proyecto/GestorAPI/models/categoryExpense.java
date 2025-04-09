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

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@Table(name = "categorias")
public class categoryExpense implements Serializable {

    //Id de la categria
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Nombre de la ategoria
    private String name;

    //Descripcion de la categoria
    private String description;

    //Iva base de la categria
    private double iva;

    //Fecha de creacion
    private LocalDateTime createdAt;

    //Fecha de modificacion
    private LocalDateTime updatedAt;

    //Lista de gastos por categoria
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Spent> spentList;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public categoryExpense(String nombre, String descripcion) {
        this.description = descripcion;
        this.name = nombre;
    }
}