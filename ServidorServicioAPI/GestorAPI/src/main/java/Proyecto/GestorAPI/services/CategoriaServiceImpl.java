package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.Categoria;
import Proyecto.GestorAPI.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaServiceImpl implements CategoriaService{

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public CharSequence getAllCategorias() {
        return (CharSequence) categoriaRepository.findAll();
    }

    @Override
    public void saveCategoria(Categoria categoria) {
        categoriaRepository.save(categoria);
    }

    @Override
    public Categoria getCategoriaById(Long catId) {
        return categoriaRepository.findById(catId).orElse(null);
    }
}
