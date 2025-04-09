package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.categoryExpense;

import java.util.List;
import java.util.Optional;

public interface CategoryExpenseService {

    List<categoryExpense> getAll();

    Optional<categoryExpense> getByID(Long id);

    categoryExpense setItem(categoryExpense o);

    void deleteByID(Long id);

    boolean existsById(Long id);
}
