package Proyecto.GestorAPI.config;

import Proyecto.GestorAPI.models.Category;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.security.SecurityConfig;
import Proyecto.GestorAPI.security.oauth2.OAuth2Provider;
import Proyecto.GestorAPI.servicesimpl.CategoryServiceImpl;
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
    private final CategoryServiceImpl categoriaService;

    @Override
    public void run(String... args) {
        if (!userService.getUsers().isEmpty()) {
            return;
        }
        USERS.forEach(user -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
        });

        CATEGORIAS.forEach(categoriaService::saveCategoria);

        log.info("Database initialized");
    }

    private static final List<User> USERS = Arrays.asList(
            new User("admin", "admin", "Admin", "admin@gesthor.com", SecurityConfig.ADMIN, null, OAuth2Provider.LOCAL, "1"),
            new User("user", "user", "User", "user@gesthor.com", SecurityConfig.USER, null, OAuth2Provider.LOCAL, "2")
    );


    private static final List<Category> CATEGORIAS = Arrays.asList(
            new Category("Alimentos", "Productos destinados a la nutrición y el consumo."),
            new Category("Bebidas", "Refrescos, jugos, agua, bebidas alcohólicas, etc."),
            new Category("Hogar", "Artículos para la casa y el bienestar del hogar."),
            new Category("Ropa", "Prendas de vestir y accesorios."),
            new Category("Salud y Belleza", "Productos para el cuidado personal, higiene y bienestar."),
            new Category("Electrónica", "Dispositivos electrónicos, gadgets y electrodomésticos."),
            new Category("Entretenimiento", "Artículos relacionados con el ocio, juegos y películas."),
            new Category("Deportes", "Equipos y accesorios deportivos."),
            new Category("Educación", "Material educativo, cursos y libros."),
            new Category("Transporte", "Gastos relacionados con el transporte personal o público."),
            new Category("Viajes", "Gastos relacionados con viajes y turismo."),
            new Category("Varios", "Productos que no encajan en otras categorías.")
    );

}
