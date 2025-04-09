package Proyecto.GestorAPI.security.oauth2;

import Proyecto.GestorAPI.security.CustomUserDetails;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * Interfaz para la extracción de información del usuario a partir de proveedores OAuth2.
 */
public interface OAuth2UserInfoExtractor {

    /**
     * Extrae la información del usuario de la respuesta OAuth2.
     *
     * @param oAuth2User Objeto que contiene la información del usuario obtenida del proveedor OAuth2.
     * @return Un objeto CustomUserDetails con los detalles del usuario extraídos.
     */
    CustomUserDetails extractUserInfo(OAuth2User oAuth2User);

    /**
     * Determina si este extractor acepta la solicitud de un proveedor OAuth2 en particular.
     *
     * @param userRequest La solicitud del usuario OAuth2.
     * @return true si este extractor es compatible con la solicitud OAuth2.
     */
    boolean accepts(OAuth2UserRequest userRequest);
}
