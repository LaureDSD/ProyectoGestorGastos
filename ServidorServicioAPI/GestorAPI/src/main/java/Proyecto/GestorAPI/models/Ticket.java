package Proyecto.GestorAPI.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
@Getter
@Setter
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaCompra;

    private Double total;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    // V2
    @Column(columnDefinition = "JSON")
    private String productosJSON; //nombre,categoria,cantidad,precio

    public Ticket(User user, Categoria categoria, Double total, LocalDateTime fechaCompra, String productosJSON) {
        this.user = user;
        this.categoria = categoria;
        this.total = total;
        this.fechaCompra = fechaCompra;
        this.productosJSON = productosJSON;
    }
}