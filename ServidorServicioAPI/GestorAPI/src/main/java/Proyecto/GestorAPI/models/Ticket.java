package Proyecto.GestorAPI.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tickets")
public class Ticket extends Spent {

    //Tienda del ticket
    private String store;

    //Lista de productos en formato JSON
    @Column(columnDefinition = "JSON")
    private String productsJSON; //nombre,lista de categorias,cantidad,precio

}