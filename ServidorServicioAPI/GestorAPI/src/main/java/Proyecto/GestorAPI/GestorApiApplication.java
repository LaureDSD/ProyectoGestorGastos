package Proyecto.GestorAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * La clase principal de la aplicación Spring Boot.
 *
 * La clase `GestorApiApplication` es la clase principal que inicia la aplicación. Está decorada con la
 * anotación `@SpringBootApplication`, que es una combinación de tres anotaciones clave:
 * 1. `@Configuration` - Indica que esta clase puede ser utilizada para la configuración de Spring.
 * 2. `@EnableAutoConfiguration` - Habilita la configuración automática de Spring Boot, como la configuración de la base de datos, servidores embebidos, etc.
 * 3. `@ComponentScan` - Habilita la detección automática de beans dentro del paquete y subpaquetes.
 *
 * Esta clase se utiliza para arrancar el contexto de Spring Boot, inicializando todos los beans, configuraciones
 * y la aplicación en general.
 */
@SpringBootApplication
public class GestorApiApplication {

	/**
	 * Método principal para ejecutar la aplicación.
	 *
	 * Este es el punto de entrada para la aplicación Spring Boot. El método `run()` arranca la aplicación
	 * y configura todos los servicios, controladores y repositorios que haya en la aplicación.
	 *
	 * @param args Argumentos que pueden ser pasados desde la línea de comandos (si se desea).
	 */
	public static void main(String[] args) {
		// Llama al método `run` de SpringApplication para arrancar la aplicación.
		SpringApplication.run(GestorApiApplication.class, args);
	}

}
