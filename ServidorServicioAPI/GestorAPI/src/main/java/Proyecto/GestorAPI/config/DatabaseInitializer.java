package Proyecto.GestorAPI.config;

import Proyecto.GestorAPI.models.CategoryExpense;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.security.RoleServer;
import Proyecto.GestorAPI.security.oauth2.OAuth2Provider;
import Proyecto.GestorAPI.servicesimpl.CategoryExpenseServiceImpl;
import Proyecto.GestorAPI.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

/**
 * Clase encargada de inicializar la base de datos con datos predeterminados
 * al iniciar la aplicación, como usuarios de prueba y categorías de gastos.
 *
 * Esta clase se ejecuta automáticamente al arrancar la aplicación Spring Boot.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CategoryExpenseServiceImpl categoriaService;

    /**
     * Método principal que se ejecuta al iniciar la aplicación.
     * Inserta usuarios y categorías por defecto si no existen datos previos.
     *
     * @param args argumentos de línea de comandos
     */
    @Override
    public void run(String... args) {
        // Verificar si ya existen usuarios registrados
        if (!userService.getUsers().isEmpty()) {
            return;
        }

        // Insertar usuarios por defecto
        USERS.forEach(user -> {
            user.setPassword(passwordEncoder.encode(user.getPassword())); // Cifrado de contraseña
            userService.saveUser(user);
        });

        // Insertar categorías de gasto predeterminadas
        CATEGORIAS.forEach(categoriaService::setItem);

        log.info("Database initialized");
    }

    /** Lista de usuarios por defecto (admin y usuario normal). */
    private static final List<User> USERS = Arrays.asList(
            new User("admin", "admin", "Admin", "admin@gesthor.com", RoleServer.ADMIN, null, OAuth2Provider.LOCAL, "1"),
            new User("user", "user", "User", "user@gesthor.com", RoleServer.USER, null, OAuth2Provider.LOCAL, "2")
    );

    /** Lista de categorías de gastos predeterminadas. */
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

}
