package Proyecto.GestorAPI.modelsDTO;

import Proyecto.GestorAPI.models.Producto;

public record ProductoDto(String nombre, double precio, int cantidad) {

    public static ProductoDto from(Producto producto) {
        return new ProductoDto(
                producto.getNombre(),
                producto.getPrecio(),
                producto.getCantidad()
        );
    }
}