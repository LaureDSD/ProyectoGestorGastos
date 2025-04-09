package Proyecto.GestorAPI.models.enums;

/**
 * Enum que representa las diferentes clases de gastos que pueden existir en la aplicación.
 * Este enum se utiliza para clasificar los gastos de acuerdo a su tipo.
 */
public enum ExpenseClass {
    /**
     * Representa un gasto asociado a un ticket (por ejemplo, un recibo de compra).
     */
    TICKET,

    /**
     * Representa un gasto asociado a una factura (por ejemplo, una factura de un servicio).
     */
    FACTURA,

    /**
     * Representa un gasto asociado a una suscripción (por ejemplo, una suscripción mensual a un servicio).
     */
    SUBSCRIPCION,

    /**
     * Representa un gasto genérico que no se clasifica en ninguna de las otras categorías.
     */
    GASTO_GENERICO,

    /**
     * Representa un gasto asociado a una transferencia de dinero (por ejemplo, una transferencia bancaria).
     */
    TRANSFERENCIA
}
