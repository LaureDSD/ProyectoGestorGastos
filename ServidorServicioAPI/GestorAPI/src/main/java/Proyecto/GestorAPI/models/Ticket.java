package Proyecto.GestorAPI.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tickets")
@Data
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

    private Instant fechaCompra;

    private Double total;

    private LocalDateTime createdAt;

    // V2
    @Column(columnDefinition = "JSON")
    private String productosJSON; //nombre,categoria,cantidad,precio

    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Ticket(User user, Categoria categoria, Double total, Instant fechaCompra, String productosJSON) {
        this.user = user;
        this.categoria = categoria;
        this.total = total;
        this.fechaCompra = fechaCompra;
        this.productosJSON = productosJSON;
    }
}