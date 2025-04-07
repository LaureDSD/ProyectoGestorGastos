package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.Category;
import jakarta.validation.constraints.NotNull;

public interface CategoryService {
    CharSequence getAllCategorias();
    void saveCategoria(Category categoria);
    Category getCategoriaById(@NotNull Long aLong);
    void deleteCategoria(Category categoria);
}
