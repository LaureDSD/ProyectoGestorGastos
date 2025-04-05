package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.Producto;

import Proyecto.GestorAPI.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto getProductoById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id " + id));
    }

    @Override
    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void deleteProducto(Long id) {
        Producto producto = getProductoById(id);
        productoRepository.delete(producto);
    }

    @Override
    public Producto updateProducto(Long id, Producto producto) {
        Producto existingProducto = getProductoById(id);
        existingProducto.setNombre(producto.getNombre());
        existingProducto.setPrecio(producto.getPrecio());
        existingProducto.setCantidad(producto.getCantidad());
        return productoRepository.save(existingProducto);
    }
}
