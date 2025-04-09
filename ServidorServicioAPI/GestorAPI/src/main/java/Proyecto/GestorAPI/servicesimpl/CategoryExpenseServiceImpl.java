package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.models.categoryExpense;
import Proyecto.GestorAPI.repositories.CategoryExpenseRepository;
import Proyecto.GestorAPI.services.CategoryExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryExpenseServiceImpl implements  CategoryExpenseService {

    @Autowired
    private CategoryExpenseRepository repository;

    @Override
    public List<categoryExpense> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<categoryExpense> getByID(Long id) {
        return repository.findById(id);
    }

    @Override
    public categoryExpense setItem(categoryExpense o) {
        return repository.save(o);
    }

    @Override
    public void deleteByID(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

}
