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
    private LocalDateTime purchaseDate;
    private Category category;
    private Double total;
    private String store;
    private double confidence;
    @Column(columnDefinition = "JSON")
    private String productsJSON; //nombre,categoria,cantidad,precio

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Category categoria;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Ticket(User user, Category categoria, Double total, LocalDateTime fechaCompra, String productosJSON) {
        this.user = user;
        this.categoria = categoria;
        this.total = total;
        this.purchaseDate = fechaCompra;
        this.productsJSON = productosJSON;
    }
}