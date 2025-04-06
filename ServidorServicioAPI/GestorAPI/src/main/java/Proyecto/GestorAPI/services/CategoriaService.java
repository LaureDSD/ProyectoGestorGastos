package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.Categoria;

public interface CategoriaService {
    CharSequence getAllCategorias();
    void saveCategoria(Categoria categoria);
}
