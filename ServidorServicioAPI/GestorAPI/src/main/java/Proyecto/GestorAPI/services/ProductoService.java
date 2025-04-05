package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.Producto;

import java.util.List;

public interface ProductoService {

    List<Producto> getAllProductos();

    Producto getProductoById(Long id);

    Producto saveProducto(Producto producto);

    void deleteProducto(Long id);

    Producto updateProducto(Long id, Producto producto);
}
