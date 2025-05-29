package Proyecto.GestorAPI.config.security.oauth2;

import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.config.security.CustomUserDetails;
import Proyecto.GestorAPI.config.security.RoleServer;
import Proyecto.GestorAPI.services.UserService;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio personalizado para cargar el usuario autenticado a través de OAuth2.
 * Extiende {@link DefaultOAuth2UserService} para personalizar el comportamiento
 * de la carga del usuario desde el proveedor OAuth2 y mapear los datos extraídos.
 */
@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    // Servicio que maneja las operaciones relacionadas con los usuarios.
    private final UserService userService;

    // Lista de extractores de información del usuario desde el proveedor OAuth2.
    private final List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors;

    /**
     * Constructor que inicializa los servicios necesarios para la autenticación.
     *
     * @param userService el servicio que maneja la lógica de usuarios.
     * @param oAuth2UserInfoExtractors lista de extractores de información del usuario.
     */
    public CustomOAuth2UserService(UserService userService, List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors) {
        this.userService = userService;
        this.oAuth2UserInfoExtractors = oAuth2UserInfoExtractors;
    }

    /**
     * Sobrescribe el método {@link DefaultOAuth2UserService#loadUser(OAuth2UserRequest)}
     * para personalizar el proceso de carga del usuario desde el proveedor OAuth2.
     * Extrae los detalles del usuario utilizando los extractores configurados y
     * crea o actualiza el usuario en la base de datos.
     *
     * @param userRequest la solicitud de autenticación OAuth2.
     * @return el usuario autenticado como un {@link OAuth2User} personalizado.
     * @throws InternalAuthenticationServiceException si el proveedor OAuth2 no es compatible.
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // Carga el usuario desde el proveedor OAuth2
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Busca el extractor adecuado para el proveedor OAuth2
        Optional<OAuth2UserInfoExtractor> oAuth2UserInfoExtractorOptional = oAuth2UserInfoExtractors.stream()
                .filter(oAuth2UserInfoExtractor -> oAuth2UserInfoExtractor.accepts(userRequest))
                .findFirst();

        // Si no se encuentra un extractor adecuado, lanza una excepción
        if (oAuth2UserInfoExtractorOptional.isEmpty()) {
            throw new InternalAuthenticationServiceException("The OAuth2 provider is not supported yet");
        }

        // Extrae la información del usuario usando el extractor adecuado
        CustomUserDetails customUserDetails = oAuth2UserInfoExtractorOptional.get().extractUserInfo(oAuth2User);

        // Actualiza o inserta el usuario en la base de datos
        User user = upsertUser(customUserDetails);

        // Asocia el ID del usuario a los detalles del usuario personalizado
        customUserDetails.setId(user.getId());

        // Devuelve el usuario autenticado
        return customUserDetails;
    }

    /**
     * Crea o actualiza un usuario en la base de datos con la información extraída del proveedor OAuth2.
     *
     * @param customUserDetails los detalles del usuario extraídos del proveedor OAuth2.
     * @return el usuario creado o actualizado.
     */
    private User upsertUser(CustomUserDetails customUserDetails) {
        // Busca el usuario por su nombre de usuario
        Optional<User> userOptional = userService.getUserByUsername(customUserDetails.getUsername());
        User user;

        // Si el usuario no existe, crea uno nuevo
        if (userOptional.isEmpty()) {
            user = new User();
            user.setUsername(customUserDetails.getUsername());
            user.setName(customUserDetails.getName());
            user.setEmail(customUserDetails.getEmail());
            user.setImageUrl(customUserDetails.getAvatarUrl());
            user.setProvider(customUserDetails.getProvider());
            user.setCreatedAt(LocalDateTime.now());
            user.setFv2(true);
            user.setActive(true);

            user.setRole(RoleServer.USER); // Asigna el rol de usuario por defecto
        } else {
            // Si el usuario ya existe, actualiza su información
            user = userOptional.get();
            user.setEmail(customUserDetails.getEmail());
            //user.setImageUrl(customUserDetails.getAvatarUrl());
            user.setUpdatedAt(LocalDateTime.now());
        }

        // Guarda el usuario en la base de datos y devuelve el objeto persistido
        return userService.saveUser(user);
    }
}
