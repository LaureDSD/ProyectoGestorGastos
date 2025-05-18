package Proyecto.GestorAPI.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j // Registra los logs de la clase
@RequiredArgsConstructor // Genera el constructor con los parámetros necesarios para la inyección de dependencias
@Component // Marca la clase como un componente de Spring para ser inyectado en otras clases
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Definición de constantes para las cabeceras de autenticación
    public static final String TOKEN_HEADER = "Authorization";  // Nombre del encabezado para el token
    public static final String TOKEN_PREFIX = "Bearer ";  // Prefijo que se espera antes del token

    private final UserDetailsService userDetailsService;  // Servicio para obtener los detalles del usuario
    private final TokenProvider tokenProvider;  // Proveedor de tokens para validar y extraer información

    /**
     * Método que se ejecuta para procesar cada solicitud HTTP y autenticar al usuario si se incluye un token válido.
     *
     * @param request La solicitud HTTP
     * @param response La respuesta HTTP
     * @param chain La cadena de filtros a ejecutar después de este filtro
     * @throws ServletException En caso de error con el servlet
     * @throws IOException En caso de error con el procesamiento de la solicitud o respuesta
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            // Intentamos extraer el JWT del encabezado de la solicitud
            getJwtFromRequest(request)
                    // Validamos y extraemos el JWS (JSON Web Signature) usando el tokenProvider
                    .flatMap(tokenProvider::validateTokenAndGetJws)
                    .ifPresent(jws -> {
                        // Si el token es válido, extraemos el nombre de usuario del payload del JWS
                        String username = jws.getPayload().getSubject();
                        // Obtenemos los detalles del usuario con el nombre de usuario
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        // Creamos un token de autenticación con los detalles del usuario
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        // Añadimos los detalles de la autenticación (como la IP o el agente de usuario)
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // Establecemos el objeto de autenticación en el contexto de seguridad de Spring
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });
        } catch (Exception e) {
            // Si ocurre un error en el proceso, lo registramos
            log.error("Cannot set user authentication", e);
        }
        // Continuamos con la cadena de filtros
        chain.doFilter(request, response);
    }

    /**
     * Extrae el JWT del encabezado de la solicitud HTTP.
     *
     * @param request La solicitud HTTP
     * @return Un Optional que contiene el token si se encuentra, o un Optional vacío si no se encuentra.
     */
    private Optional<String> getJwtFromRequest(HttpServletRequest request) {
        String tokenHeader = request.getHeader(TOKEN_HEADER);
        // Verificamos si el encabezado contiene el token con el prefijo "Bearer "
        if (StringUtils.hasText(tokenHeader) && tokenHeader.startsWith(TOKEN_PREFIX)) {
            // Si es así, eliminamos el prefijo y devolvemos el token
            return Optional.of(tokenHeader.replace(TOKEN_PREFIX, ""));
        }
        // Si no hay token válido, devolvemos un Optional vacío
        return Optional.empty();
    }

}
