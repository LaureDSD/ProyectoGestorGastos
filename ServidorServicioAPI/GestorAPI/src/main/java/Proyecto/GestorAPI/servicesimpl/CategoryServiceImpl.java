package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.models.Category;
import Proyecto.GestorAPI.repositories.CategoryRepository;
import Proyecto.GestorAPI.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoriaRepository;

    @Override
    public CharSequence getAllCategorias() {
        return (CharSequence) categoriaRepository.findAll();
    }

    @Override
    public void saveCategoria(Category categoria) {
        categoriaRepository.save(categoria);
    }

    @Override
    public Category getCategoriaById(Long catId) {
        return categoriaRepository.findById(catId).orElse(null);
    }

    @Override
    public void deleteCategoria(Category categoria) {
         categoriaRepository.delete(categoria);
    }


}
