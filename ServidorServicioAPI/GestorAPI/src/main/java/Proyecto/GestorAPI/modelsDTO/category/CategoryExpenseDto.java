package Proyecto.GestorAPI.modelsDTO.category;

import Proyecto.GestorAPI.models.CategoryExpense;

public record CategoryExpenseDto(
        Long id,
        String name,
        String description,
        double iva
) {
    public static CategoryExpenseDto from(CategoryExpense category) {
        return new CategoryExpenseDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getIva()
        );
    }
}
