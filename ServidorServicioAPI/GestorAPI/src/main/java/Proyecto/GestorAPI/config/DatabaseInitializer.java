package Proyecto.GestorAPI.config;

import Proyecto.GestorAPI.models.Categoria;
import Proyecto.GestorAPI.models.Ticket;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.security.SecurityConfig;
import Proyecto.GestorAPI.security.oauth2.OAuth2Provider;
import Proyecto.GestorAPI.services.CategoriaServiceImpl;
import Proyecto.GestorAPI.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CategoriaServiceImpl categoriaService;

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


    private static final List<Categoria> CATEGORIAS = Arrays.asList(
            new Categoria("Alimentos", "Productos destinados a la nutrición y el consumo."),
            new Categoria("Bebidas", "Refrescos, jugos, agua, bebidas alcohólicas, etc."),
            new Categoria("Hogar", "Artículos para la casa y el bienestar del hogar."),
            new Categoria("Ropa", "Prendas de vestir y accesorios."),
            new Categoria("Salud y Belleza", "Productos para el cuidado personal, higiene y bienestar."),
            new Categoria("Electrónica", "Dispositivos electrónicos, gadgets y electrodomésticos."),
            new Categoria("Entretenimiento", "Artículos relacionados con el ocio, juegos y películas."),
            new Categoria("Deportes", "Equipos y accesorios deportivos."),
            new Categoria("Educación", "Material educativo, cursos y libros."),
            new Categoria("Transporte", "Gastos relacionados con el transporte personal o público."),
            new Categoria("Viajes", "Gastos relacionados con viajes y turismo."),
            new Categoria("Varios", "Productos que no encajan en otras categorías.")
    );

}
