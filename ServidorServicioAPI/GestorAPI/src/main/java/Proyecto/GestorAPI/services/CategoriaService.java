package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.Categoria;
import jakarta.validation.constraints.NotNull;

public interface CategoriaService {
    CharSequence getAllCategorias();
    void saveCategoria(Categoria categoria);

    Categoria getCategoriaById(@NotNull Long aLong);
}
