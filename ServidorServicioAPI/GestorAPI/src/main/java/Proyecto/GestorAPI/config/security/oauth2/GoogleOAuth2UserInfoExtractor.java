package Proyecto.GestorAPI.config.security.oauth2;

import Proyecto.GestorAPI.config.security.CustomUserDetails;
import Proyecto.GestorAPI.config.SecurityConfig;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Extractor personalizado para obtener la información del usuario de Google a través de OAuth2.
 * Implementa la interfaz {@link OAuth2UserInfoExtractor} para extraer y mapear la información
 * del usuario de Google al formato de {@link CustomUserDetails}.
 */
@Service
public class GoogleOAuth2UserInfoExtractor implements OAuth2UserInfoExtractor {

    /**
     * Extrae la información del usuario de Google y la mapea a un objeto {@link CustomUserDetails}.
     *
     * @param oAuth2User el usuario autenticado proveniente de Google.
     * @return un objeto {@link CustomUserDetails} con la información extraída.
     */
    @Override
    public CustomUserDetails extractUserInfo(OAuth2User oAuth2User) {
        CustomUserDetails customUserDetails = new CustomUserDetails();

        // Extrae el correo electrónico del usuario (como nombre de usuario también)
        customUserDetails.setUsername(retrieveAttr("email", oAuth2User));

        // Extrae el nombre completo del usuario
        customUserDetails.setName(retrieveAttr("name", oAuth2User));

        // Extrae el correo electrónico del usuario
        customUserDetails.setEmail(retrieveAttr("email", oAuth2User));

        // Extrae la URL de la foto de perfil del usuario
        customUserDetails.setAvatarUrl(retrieveAttr("picture", oAuth2User));

        // Establece el proveedor como Google
        customUserDetails.setProvider(OAuth2Provider.GOOGLE);

        // Establece los atributos del usuario (todo lo que devuelve Google)
        customUserDetails.setAttributes(oAuth2User.getAttributes());

        // Establece el rol de usuario como "USER" en la seguridad
        customUserDetails.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority(SecurityConfig.USER)));

        return customUserDetails;
    }

    /**
     * Determina si este extractor acepta la solicitud de un proveedor OAuth2 determinado (en este caso, Google).
     *
     * @param userRequest la solicitud de OAuth2 que contiene la información del proveedor.
     * @return true si el proveedor es Google, false en caso contrario.
     */
    @Override
    public boolean accepts(OAuth2UserRequest userRequest) {
        return OAuth2Provider.GOOGLE.name().equalsIgnoreCase(userRequest.getClientRegistration().getRegistrationId());
    }

    /**
     * Método auxiliar para obtener un atributo específico del objeto {@link OAuth2User}.
     * Si el atributo no existe, retorna una cadena vacía.
     *
     * @param attr el nombre del atributo a extraer.
     * @param oAuth2User el usuario de OAuth2 desde el que se extrae el atributo.
     * @return el valor del atributo como cadena, o una cadena vacía si no existe.
     */
    private String retrieveAttr(String attr, OAuth2User oAuth2User) {
        Object attribute = oAuth2User.getAttributes().get(attr);
        return attribute == null ? "" : attribute.toString();
    }
}
