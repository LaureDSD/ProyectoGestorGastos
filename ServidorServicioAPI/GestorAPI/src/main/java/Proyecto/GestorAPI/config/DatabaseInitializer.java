package Proyecto.GestorAPI.config;

import Proyecto.GestorAPI.models.*;
import Proyecto.GestorAPI.models.enums.ExpenseClass;
import Proyecto.GestorAPI.config.security.RoleServer;
import Proyecto.GestorAPI.config.security.oauth2.OAuth2Provider;
import Proyecto.GestorAPI.services.*;
import Proyecto.GestorAPI.servicesimpl.CategoryExpenseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Clase encargada de inicializar la base de datos al arrancar la aplicación.
 * <p>
 * Implementa {@link CommandLineRunner} para ejecutar la inserción de datos por defecto.
 * <p>
 * Los datos insertados incluyen:
 * <ul>
 *     <li>Usuarios con roles y credenciales codificadas.</li>
 *     <li>Categorías de gastos predefinidas.</li>
 *     <li>Gastos generales con diferentes fechas y categorías.</li>
 *     <li>Tickets con detalles en JSON sobre productos y categorías.</li>
 *     <li>Suscripciones con información de fechas y estado activo.</li>
 *     <li>Intentos de login para auditoría.</li>
 * </ul>
 * <p>
 * La inicialización solo se ejecuta si no existen usuarios en la base de datos para evitar duplicados.
 * Las contraseñas de usuarios son codificadas usando {@link PasswordEncoder}.
 * <p>
 * Anotada como {@link Component} para ser detectada automáticamente por Spring.
 * Utiliza {@link lombok.RequiredArgsConstructor} para inyección de dependencias mediante constructor.
 * <p>
 * Utiliza logging con {@link lombok.extern.slf4j.Slf4j}.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseInitializer implements CommandLineRunner {

    /**
     * Servicio para gestión de usuarios.
     */
    private final UserService userService;

    /**
     * Servicio para gestión de gastos.
     */
    private final SpentService spentService;

    /**
     * Servicio para gestión de tickets.
     */
    private final TicketService ticketService;

    /**
     * Servicio para gestión de suscripciones.
     */
    private final SubscriptionService subscriptionService;

    /**
     * Codificador de contraseñas para seguridad.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Servicio para gestión de categorías de gastos.
     */
    private final CategoryExpenseServiceImpl categoriaService;

    /**
     * Servicio para registro de intentos de login.
     */
    private final LoginAttemptService loginAttemptService;

    /**
     * Método ejecutado al iniciar la aplicación.
     * Comprueba si ya existen usuarios y, en caso contrario, inserta datos por defecto.
     * Los datos incluyen usuarios, categorías, gastos, tickets, suscripciones y logs de intentos de login.
     *
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    @Override
    public void run(String... args) {
        // Evita inserción si la tabla de usuarios no está vacía.
        if (!userService.getUsers().isEmpty()) {
            return;
        }

        // Inserción de usuarios con contraseñas codificadas.
        USERS.forEach(user -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
        });

        // Inserción de categorías predefinidas.
        CATEGORIAS.forEach(categoriaService::setItem);

        // Inserción de gastos generales.
        SPENTS.forEach(spentService::setItem);

        // Inserción de tickets.
        TICKETS.forEach(ticketService::setItem);

        // Inserción de suscripciones.
        SUBSCRIPTIONS.forEach(subscriptionService::setItem);

        // Inserción de registros de intentos de login.
        LOGIN_ATTEMPTS.forEach(loginAttemptService::setItem);

        System.out.println("Database initialized");
    }

    /**
     * Lista estática de usuarios por defecto a insertar.
     * Incluye nombre de usuario, contraseña, rol, proveedor OAuth2, y datos adicionales.
     */
    public static final List<User> USERS = List.of(
            new User("admin", "admin", "Admin", "admin@gesthor.com", RoleServer.ADMIN,
                    "", OAuth2Provider.LOCAL, "local-1", "GesThor-Admin", "admin address", "123456789", false),
            new User("user", "user", "User", "user@gesthor.com", RoleServer.USER,
                    "", OAuth2Provider.LOCAL, "local-2", "GesThor-User", "user address", "987654321", true)
    );

    /**
     * Lista estática de categorías de gastos.
     * Cada categoría tiene un nombre y una descripción breve.
     */
    private static final List<CategoryExpense> CATEGORIAS = Arrays.asList(
            new CategoryExpense("Alimentos", "Productos destinados a la nutrición y el consumo.", 4),
            new CategoryExpense("Bebidas", "Refrescos, jugos, agua, bebidas alcohólicas, etc.", 10),
            new CategoryExpense("Hogar", "Artículos para la casa y el bienestar del hogar.", 21),
            new CategoryExpense("Ropa", "Prendas de vestir y accesorios.", 21),
            new CategoryExpense("Salud y Belleza", "Productos para el cuidado personal, higiene y bienestar.", 21),
            new CategoryExpense("Electrónica", "Dispositivos electrónicos, gadgets y electrodomésticos.", 21),
            new CategoryExpense("Entretenimiento", "Artículos relacionados con el ocio, juegos y películas.", 21),
            new CategoryExpense("Deportes", "Equipos y accesorios deportivos.", 21),
            new CategoryExpense("Educación", "Material educativo, cursos y libros.", 4),
            new CategoryExpense("Transporte", "Gastos relacionados con el transporte personal o público.", 10),
            new CategoryExpense("Viajes", "Gastos relacionados con viajes y turismo.", 21),
            new CategoryExpense("Varios", "Productos que no encajan en otras categorías.", 21),

// Categorías adicionales recomendadas:
            new CategoryExpense("Restaurantes y Bares", "Comidas y bebidas consumidas fuera de casa.", 10),
            new CategoryExpense("Servicios", "Servicios contratados como peluquería, fontanería, etc.", 21),
            new CategoryExpense("Mascotas", "Productos y servicios para animales domésticos.", 21),
            new CategoryExpense("Impuestos y Tasas", "Pagos de impuestos, tasas gubernamentales, etc.", 0),
            new CategoryExpense("Donaciones", "Aportaciones a ONGs u organizaciones sin fines de lucro.", 0),
            new CategoryExpense("Mantenimiento Vehículo", "Reparaciones, revisiones y repuestos de automóviles.", 21),
            new CategoryExpense("Alquiler", "Pagos de alquiler de vivienda o local.", 10),
            new CategoryExpense("Teléfono e Internet", "Servicios de telecomunicaciones.", 21),
            new CategoryExpense("Seguros", "Seguros de salud, vida, coche, hogar, etc.", 21),
            new CategoryExpense("Finanzas", "Comisiones bancarias, intereses, servicios financieros.", 21)
    );

    /**
     * Lista estática de gastos generales a insertar.
     * Cada gasto contiene título, descripción, fecha, importe, IVA, usuario asociado,
     * categoría, fechas de creación y modificación, y clase de gasto.
     */
    public static final List<Spent> SPENTS = List.of(
            new Spent("Compra de equipo", "Compra de equipo de oficina", "", LocalDateTime.now(), 1200.00, 21.0,
                    USERS.get(0), CATEGORIAS.get(5), LocalDateTime.now(), LocalDateTime.now(), ExpenseClass.GASTO_GENERICO),
            new Spent("Suscripción a software", "Pago mensual de suscripción a software", "", LocalDateTime.now().minusMonths(2), 30.00, 21.0,
                    USERS.get(0), CATEGORIAS.get(6), LocalDateTime.now().minusMonths(2), LocalDateTime.now(), ExpenseClass.GASTO_GENERICO),
            new Spent("Compra de alimentos", "Compra en supermercado", "", LocalDateTime.now().minusMonths(4), 50.00, 10.0,
                    USERS.get(1), CATEGORIAS.get(7), LocalDateTime.now().minusMonths(4), LocalDateTime.now(), ExpenseClass.GASTO_GENERICO),
            new Spent("Transporte", "Pago transporte público", "", LocalDateTime.now().minusMonths(21), 15.00, 10.0,
                    USERS.get(1), CATEGORIAS.get(8), LocalDateTime.now().minusMonths(20), LocalDateTime.now(), ExpenseClass.GASTO_GENERICO),

            new Spent("Compra de equipo 2", "Compra de equipo de oficina", "", LocalDateTime.now().minusMonths(1), 600.00, 21.0,
                    USERS.get(0), CATEGORIAS.get(5), LocalDateTime.now(), LocalDateTime.now(), ExpenseClass.GASTO_GENERICO),
            new Spent("Suscripción a software 2", "Pago mensual de suscripción a software", "", LocalDateTime.now().minusMonths(2), 30.00, 21.0,
                    USERS.get(0), CATEGORIAS.get(6), LocalDateTime.now().minusMonths(5), LocalDateTime.now(), ExpenseClass.GASTO_GENERICO),
            new Spent("Compra de alimentos 2", "Compra en supermercado", "", LocalDateTime.now().minusMonths(15), 50.00, 10.0,
                    USERS.get(1), CATEGORIAS.get(7), LocalDateTime.now().minusMonths(4), LocalDateTime.now(), ExpenseClass.GASTO_GENERICO),
            new Spent("Transporte 2", "Pago transporte público", "", LocalDateTime.now().minusMonths(3), 15.00, 10.0,
                    USERS.get(1), CATEGORIAS.get(8), LocalDateTime.now().minusMonths(3), LocalDateTime.now(), ExpenseClass.GASTO_GENERICO)
    );

    /**
     * Lista estática de tickets con detalle JSON de productos y categorías.
     * Contiene información similar a gastos pero con detalle adicional en formato JSON.
     */
    public static final List<Ticket> TICKETS = List.of(
            new Ticket("Compra de oficina", "Compra de equipo de oficina", "", LocalDateTime.now().minusMonths(6), 1200.00, 21.0,
                    USERS.get(0), CATEGORIAS.get(0), LocalDateTime.now().minusMonths(5), LocalDateTime.now(), ExpenseClass.TICKET,
                    "Electrodomesticos", "[{\"nombre\": \"Laptop\", \"categorias\": [\"Tecnología\", \"Computación\"], \"cantidad\": 1, \"precio\": 1000.00}, {\"nombre\": \"Mochila\", \"categorias\": [\"Accesorios\"], \"cantidad\": 1, \"precio\": 200.00}]"),
            new Ticket("Supermercado", "Compra en supermercado", "", LocalDateTime.now().minusMonths(12), 50.00, 10.0,
                    USERS.get(1), CATEGORIAS.get(1) , LocalDateTime.now().minusMonths(11), LocalDateTime.now(), ExpenseClass.TICKET,
                    "Supermercado", "[{\"nombre\": \"Leche\", \"categorias\": [\"Alimentos\", \"Lácteos\"], \"cantidad\": 2, \"precio\": 1.25}, {\"nombre\": \"Pan\", \"categorias\": [\"Alimentos\"], \"cantidad\": 1, \"precio\": 0.95}]"),
            new Ticket("Compra de oficina 2", "Compra de equipo de oficina", "", LocalDateTime.now().minusMonths(6), 1200.00, 21.0,
                    USERS.get(0), CATEGORIAS.get(0), LocalDateTime.now().minusMonths(4), LocalDateTime.now(), ExpenseClass.TICKET,
                    "Electrodomesticos", "[{\"nombre\": \"Laptop\", \"categorias\": [\"Tecnología\", \"Computación\"], \"cantidad\": 1, \"precio\": 1000.00}, {\"nombre\": \"Mochila\", \"categorias\": [\"Accesorios\"], \"cantidad\": 1, \"precio\": 200.00}]"),
            new Ticket("Supermercado 2", "Compra en supermercado", "", LocalDateTime.now().minusMonths(12), 50.00, 10.0,
                    USERS.get(1), CATEGORIAS.get(1) , LocalDateTime.now().minusMonths(6), LocalDateTime.now(), ExpenseClass.TICKET,
                    "Supermercado", "[{\"nombre\": \"Leche\", \"categorias\": [\"Alimentos\", \"Lácteos\"], \"cantidad\": 2, \"precio\": 1.25}, {\"nombre\": \"Pan\", \"categorias\": [\"Alimentos\"], \"cantidad\": 1, \"precio\": 0.95}]")
    );

    /**
     * Lista estática de suscripciones con fechas, estado y periodicidad.
     * Contiene datos de suscripción mensual o anual, importe, usuario, categoría y estado activo.
     */
    public static final List<Subscription> SUBSCRIPTIONS = List.of(
            new Subscription("Netflix", "Subscripción mensual de streaming", "",
                    LocalDateTime.now().minusMonths(23), 10.99, 1.21,
                    USERS.get(1), CATEGORIAS.get(3),
                    LocalDateTime.now().minusMonths(1), LocalDateTime.now(),
                    ExpenseClass.SUBSCRIPCION,
                    LocalDateTime.now().minusMonths(1), null,
                    1, 30, true),

            new Subscription("Spotify", "Subscripción mensual de música", "",
                    LocalDateTime.now().minusWeeks(12), 9.99, 1.10,
                    USERS.get(0), CATEGORIAS.get(4),
                    LocalDateTime.now().minusWeeks(12), LocalDateTime.now(),
                    ExpenseClass.SUBSCRIPCION,
                    LocalDateTime.now().minusWeeks(2), null,
                    5, 30, true),

            new Subscription("Amazon", "Subscripción mensual de streaming", "",
                    LocalDateTime.now().minusMonths(8), 10.99, 1.21,
                    USERS.get(1), CATEGORIAS.get(3),
                    LocalDateTime.now().minusMonths(8), LocalDateTime.now(),
                    ExpenseClass.SUBSCRIPCION,
                    LocalDateTime.now().minusMonths(1), null,
                    1, 30, false),

            new Subscription("XBoxPass", "Subscripción mensual de juegos", "",
                    LocalDateTime.now().minusWeeks(2), 9.99, 1.10,
                    USERS.get(0), CATEGORIAS.get(4),
                    LocalDateTime.now().minusWeeks(12), LocalDateTime.now(),
                    ExpenseClass.SUBSCRIPCION,
                    LocalDateTime.now().minusWeeks(2), null,
                    5, 30, true)
    );

    /**
     * Lista estática de intentos de login para auditoría.
     * Cada registro contiene el usuario, fecha y si el intento fue exitoso o no.
     */
    public static final List<LoginAttempt> LOGIN_ATTEMPTS = List.of(
            new LoginAttempt("admin", Instant.now(), true),
            new LoginAttempt("user", Instant.now(), false),
            new LoginAttempt("admin", Instant.now(), true),
            new LoginAttempt("user", Instant.now(), false)
    );

}
