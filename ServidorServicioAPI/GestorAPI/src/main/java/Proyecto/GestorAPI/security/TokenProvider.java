package Proyecto.GestorAPI.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j // Añade la capacidad de registrar logs
@Component // Marca la clase como un componente gestionado por Spring
@RequiredArgsConstructor // Genera un constructor con los parámetros necesarios para la inyección de dependencias
public class TokenProvider {

    // Constantes relacionadas con el tipo de token, el emisor y el público
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "order-api";
    public static final String TOKEN_AUDIENCE = "order-app";

    private final UserDetailsService userDetailsService;  // Servicio para obtener los detalles del usuario

    @Value("${app.jwt.secret}")
    private String jwtSecret;  // Clave secreta utilizada para firmar el JWT

    @Value("${app.jwt.expiration.minutes}")
    private Long jwtExpirationMinutes;  // Tiempo de expiración del JWT en minutos

    /**
     * Genera un JWT (JSON Web Token) para un usuario autenticado.
     *
     * @param authentication El objeto de autenticación que contiene los detalles del usuario
     * @return El token JWT generado
     */
    public String generate(Authentication authentication) {
        // Obtenemos los detalles del usuario desde la autenticación
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        // Obtenemos los roles del usuario y los convertimos a una lista de strings
        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Convertimos la clave secreta a bytes
        byte[] signingKey = jwtSecret.getBytes();

        // Obtiene la hora actual
        Instant now = Instant.now();

        // Genera el token JWT
        return Jwts.builder()
                .header().add("typ", TOKEN_TYPE) // Especificamos el tipo de token
                .and()
                .signWith(Keys.hmacShaKeyFor(signingKey), Jwts.SIG.HS512) // Firmamos el token con la clave secreta
                .issuedAt(Date.from(now)) // Fecha de emisión del token
                .expiration(Date.from(now.plusSeconds(60 * jwtExpirationMinutes))) // Fecha de expiración del token
                .id(UUID.randomUUID().toString()) // ID único para el token
                .issuer(TOKEN_ISSUER) // El emisor del token
                .audience().add(TOKEN_AUDIENCE) // El público del token
                .and()
                .subject(user.getUsername()) // El nombre de usuario (sujeto)
                .claim("rol", roles) // Añadimos el rol del usuario como un claim
                .claim("name", user.getName()) // Añadimos el nombre del usuario como un claim
                .claim("preferred_username", user.getUsername()) // Nombre de usuario preferido
                .claim("email", user.getEmail()) // Correo electrónico del usuario
                .compact(); // Genera el token compacto
    }

    /**
     * Valida un token JWT y devuelve su contenido (claims) si es válido.
     *
     * @param token El token JWT que se desea validar
     * @return Un Optional que contiene el JWS (JSON Web Signature) si el token es válido, o un Optional vacío si no lo es
     */
    public Optional<Jws<Claims>> validateTokenAndGetJws(String token) {
        try {
            // Convertimos la clave secreta a bytes
            byte[] signingKey = jwtSecret.getBytes();

            // Intentamos analizar y validar el token
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(signingKey)) // Verificación de la firma con la clave secreta
                    .build()
                    .parseSignedClaims(token); // Parseo de los claims del token

            // Si todo es correcto, devolvemos el JWS (contenido del token)
            return Optional.of(jws);
        } catch (ExpiredJwtException exception) {
            // Si el token ha expirado, lo registramos en los logs
            log.error("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            // Si el token es de un tipo no soportado, lo registramos en los logs
            log.error("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
        } catch (MalformedJwtException exception) {
            // Si el token está mal formado, lo registramos en los logs
            log.error("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
        } catch (SignatureException exception) {
            // Si la firma del token es inválida, lo registramos en los logs
            log.error("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            // Si el token es vacío o nulo, lo registramos en los logs
            log.error("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
        }
        // Si ocurre cualquier error, devolvemos un Optional vacío
        return Optional.empty();
    }

}
