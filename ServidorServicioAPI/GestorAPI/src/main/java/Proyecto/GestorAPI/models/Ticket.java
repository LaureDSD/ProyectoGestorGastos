package Proyecto.GestorAPI.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Category categoria;

    private LocalDateTime fechaCompra;

    private Double total;

    private LocalDateTime createdAt;

    // V2
    @Column(columnDefinition = "JSON")
    private String productosJSON; //nombre,categoria,cantidad,precio

    public Ticket(User user, Category categoria, Double total, LocalDateTime fechaCompra, String productosJSON) {
        this.user = user;
        this.categoria = categoria;
        this.total = total;
        this.fechaCompra = fechaCompra;
        this.productosJSON = productosJSON;
    }
}