package Proyecto.GestorAPI.config;

import Proyecto.GestorAPI.models.categoryExpense;
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

@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CategoryExpenseServiceImpl categoriaService;

    @Override
    public void run(String... args) {
        if (!userService.getUsers().isEmpty()) {
            return;
        }
        USERS.forEach(user -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
        });

        CATEGORIAS.forEach(categoriaService::setItem);

        log.info("Database initialized");
    }

    private static final List<User> USERS = Arrays.asList(
            new User("admin", "admin", "Admin", "admin@gesthor.com", RoleServer.ADMIN, null, OAuth2Provider.LOCAL, "1"),
            new User("user", "user", "User", "user@gesthor.com", RoleServer.USER, null, OAuth2Provider.LOCAL, "2")
    );


    private static final List<categoryExpense> CATEGORIAS = Arrays.asList(
            new categoryExpense("Alimentos", "Productos destinados a la nutrición y el consumo."),
            new categoryExpense("Bebidas", "Refrescos, jugos, agua, bebidas alcohólicas, etc."),
            new categoryExpense("Hogar", "Artículos para la casa y el bienestar del hogar."),
            new categoryExpense("Ropa", "Prendas de vestir y accesorios."),
            new categoryExpense("Salud y Belleza", "Productos para el cuidado personal, higiene y bienestar."),
            new categoryExpense("Electrónica", "Dispositivos electrónicos, gadgets y electrodomésticos."),
            new categoryExpense("Entretenimiento", "Artículos relacionados con el ocio, juegos y películas."),
            new categoryExpense("Deportes", "Equipos y accesorios deportivos."),
            new categoryExpense("Educación", "Material educativo, cursos y libros."),
            new categoryExpense("Transporte", "Gastos relacionados con el transporte personal o público."),
            new categoryExpense("Viajes", "Gastos relacionados con viajes y turismo."),
            new categoryExpense("Varios", "Productos que no encajan en otras categorías.")
    );

}
