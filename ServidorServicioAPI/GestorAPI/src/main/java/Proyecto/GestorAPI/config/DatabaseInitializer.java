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

@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserService userService;
    private final SpentService spentService;
    private final TicketService ticketService;
    private final SubscriptionService subscriptionService;
    private final PasswordEncoder passwordEncoder;
    private final CategoryExpenseServiceImpl categoriaService;
    private final LoginAttemptService loginAttemptService;

    @Override
    public void run(String... args) {
        if (!userService.getUsers().isEmpty()) {
            return;
        }

        // Insertar usuarios por defecto
        USERS.forEach(user -> {
            user.setPassword(passwordEncoder.encode(user.getPassword())); // Cifrado de contraseña
            userService.saveUser(user);
        });

        // Insertar categorías
        CATEGORIAS.forEach(categoriaService::setItem);

        // Insertar gastos
        SPENTS.forEach(spentService::setItem);

        // Insertar tickets
        TICKETS.forEach(ticketService::setItem);

        // Insertar suscripciones
        SUBSCRIPTIONS.forEach(subscriptionService::setItem);

        //Insertar Logs
        LOGIN_ATTEMPTS.forEach(loginAttemptService::setItem);


        System.out.println("Database initialized");
    }

    public static final List<User> USERS = List.of(
            new User("admin", "admin", "Admin", "admin@gesthor.com", RoleServer.ADMIN,
                    "", OAuth2Provider.LOCAL, "local-1", "GesThor-Admin", "admin address", "123456789", false),
            new User("user", "user", "User", "user@gesthor.com", RoleServer.USER,
                    "", OAuth2Provider.LOCAL, "local-2", "GesThor-User", "user address", "987654321", true)
    );

    private static final List<CategoryExpense> CATEGORIAS = Arrays.asList(
            new CategoryExpense("Alimentos", "Productos destinados a la nutrición y el consumo."),
            new CategoryExpense("Bebidas", "Refrescos, jugos, agua, bebidas alcohólicas, etc."),
            new CategoryExpense("Hogar", "Artículos para la casa y el bienestar del hogar."),
            new CategoryExpense("Ropa", "Prendas de vestir y accesorios."),
            new CategoryExpense("Salud y Belleza", "Productos para el cuidado personal, higiene y bienestar."),
            new CategoryExpense("Electrónica", "Dispositivos electrónicos, gadgets y electrodomésticos."),
            new CategoryExpense("Entretenimiento", "Artículos relacionados con el ocio, juegos y películas."),
            new CategoryExpense("Deportes", "Equipos y accesorios deportivos."),
            new CategoryExpense("Educación", "Material educativo, cursos y libros."),
            new CategoryExpense("Transporte", "Gastos relacionados con el transporte personal o público."),
            new CategoryExpense("Viajes", "Gastos relacionados con viajes y turismo."),
            new CategoryExpense("Varios", "Productos que no encajan en otras categorías.")
    );

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
                    USERS.get(0), CATEGORIAS.get(9),
                    LocalDateTime.now().minusWeeks(9), LocalDateTime.now(),
                    ExpenseClass.SUBSCRIPCION,
                    LocalDateTime.now().minusWeeks(2), null,
                    5, 30, true)
    );



    public static final List<LoginAttempt> LOGIN_ATTEMPTS = List.of(
            new LoginAttempt("admin", Instant.now(), true),
            new LoginAttempt("user", Instant.now(), false),
            new LoginAttempt("admin", Instant.now(), true),
            new LoginAttempt("user", Instant.now(), false)
    );
}
