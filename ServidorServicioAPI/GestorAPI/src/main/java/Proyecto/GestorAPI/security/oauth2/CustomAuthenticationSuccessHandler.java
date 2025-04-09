package Proyecto.GestorAPI.security.oauth2;

import Proyecto.GestorAPI.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * Manejador de éxito de autenticación personalizada para manejar el flujo de redirección después de la autenticación exitosa.
 * Este componente extiende la clase {@link SimpleUrlAuthenticationSuccessHandler} para personalizar el comportamiento
 * después de que un usuario se haya autenticado correctamente a través de OAuth2.
 */
@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // Proveedor de tokens para generar un JWT
    private final TokenProvider tokenProvider;

    // URI de redirección configurada en la aplicación
    @Value("${app.oauth2.redirectUri}")
    private String redirectUri;

    /**
     * Método sobrecargado que se ejecuta después de una autenticación exitosa.
     * Maneja la redirección al URL de destino y agrega un token JWT como parámetro en la URL.
     *
     * @param request la solicitud HTTP.
     * @param response la respuesta HTTP.
     * @param authentication el objeto de autenticación que contiene los detalles del usuario autenticado.
     * @throws IOException si ocurre un error durante la escritura de la respuesta.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // Llama al método handle() para realizar el manejo de la redirección.
        handle(request, response, authentication);
        // Limpia los atributos de autenticación de la sesión después de la autenticación exitosa.
        super.clearAuthenticationAttributes(request);
    }

    /**
     * Maneja la lógica de redirección después de una autenticación exitosa, agregando el token JWT a la URL de destino.
     * Si no se proporciona un URI de redirección, se utiliza la URL de destino por defecto.
     *
     * @param request la solicitud HTTP.
     * @param response la respuesta HTTP.
     * @param authentication el objeto de autenticación que contiene los detalles del usuario autenticado.
     * @throws IOException si ocurre un error durante la redirección.
     */
    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // Determina el URL de destino, si el redirigido está vacío se utiliza la URL por defecto.
        String targetUrl = redirectUri.isEmpty() ?
                determineTargetUrl(request, response, authentication) : redirectUri;

        // Genera el token JWT a partir de la autenticación del usuario.
        String token = tokenProvider.generate(authentication);

        // Añade el token como un parámetro en la URL de destino.
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token) // Agrega el token como parámetro de consulta
                .build().toUriString();

        // Redirige al usuario a la URL de destino con el token.
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
