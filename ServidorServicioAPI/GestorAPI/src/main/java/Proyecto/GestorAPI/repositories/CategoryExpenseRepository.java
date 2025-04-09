package Proyecto.GestorAPI.repositories;

import Proyecto.GestorAPI.models.categoryExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryExpenseRepository extends JpaRepository<categoryExpense, Long> {
}
