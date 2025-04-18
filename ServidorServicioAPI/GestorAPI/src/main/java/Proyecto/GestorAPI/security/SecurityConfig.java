package Proyecto.GestorAPI.security;

import Proyecto.GestorAPI.security.oauth2.CustomAuthenticationSuccessHandler;
import Proyecto.GestorAPI.security.oauth2.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    // Constantes para los roles de acceso
    public static final String ADMIN = RoleServer.ADMIN.name();
    public static final String USER = RoleServer.USER.name();

    private final CustomOAuth2UserService customOauth2UserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    // Constructor para inyección de dependencias
    public SecurityConfig(CustomOAuth2UserService customOauth2UserService,
                          CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
                          TokenProvider tokenProvider,
                          UserDetailsService userDetailsService) {
        this.customOauth2UserService = customOauth2UserService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Bean que proporciona el filtro para la autenticación por token.
     * Este filtro interceptará las solicitudes HTTP para validar los tokens JWT.
     *
     * @return TokenAuthenticationFilter, filtro para autenticar solicitudes con tokens.
     */
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(userDetailsService, tokenProvider);
    }

    /**
     * Bean que proporciona un AuthenticationManager para gestionar la autenticación.
     *
     * @param authenticationConfiguration Configuración de autenticación.
     * @return AuthenticationManager, utilizado para autenticar las solicitudes.
     * @throws Exception Si ocurre un error al configurar el manager de autenticación.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configuración principal de seguridad de la aplicación.
     * Se configuran las reglas de acceso, autenticación y manejo de sesiones.
     *
     * @param http Configuración de seguridad para las solicitudes HTTP.
     * @return SecurityFilterChain, cadena de filtros para asegurar las rutas de la aplicación.
     * @throws Exception Si ocurre un error al configurar la seguridad.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        // Acceso público a recursos estáticos y ciertas rutas
                        .requestMatchers("/static/**", "/resources/**", "/css/**", "/js/**", "/images/**").permitAll()
                        // Acceso restringido a los usuarios con roles ADMIN o USER
                        .requestMatchers(HttpMethod.GET, "/api/tickets", "/api/tickets/**").hasAnyAuthority(ADMIN, USER)
                        .requestMatchers(HttpMethod.GET, "/api/users/me").hasAnyAuthority(ADMIN, USER)
                        // Acceso solo para ADMIN a ciertas rutas de tickets y usuarios
                        .requestMatchers("/api/tickets", "/api/tickets/**").hasAuthority(ADMIN)
                        .requestMatchers("/api/users", "/api/users/**").hasAuthority(ADMIN)
                        // Rutas públicas de autenticación y recursos públicos
                        .requestMatchers("/public/**", "/auth/**", "/oauth2/**").permitAll()
                        .requestMatchers("/", "/error", "/csrf", "/swagger-ui/**", "/v3/api-docs/**","api/ocr/**").permitAll()
                        // Cualquier otra solicitud requiere autenticación
                        .anyRequest().authenticated())
                // Configuración de login OAuth2
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOauth2UserService))
                        .successHandler(customAuthenticationSuccessHandler))
                // Configuración de logout
                .logout(logout -> logout.logoutSuccessUrl("/").permitAll())
                // Añadir filtro de autenticación antes del filtro por defecto de nombre y contraseña
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // Configuración para sesiones sin estado (sin sesión en el servidor)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Manejo de errores de autenticación (401)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                // Deshabilitar CSRF (recomendado solo si la aplicación no tiene formularios de sesión)
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    /**
     * Bean para la configuración de un PasswordEncoder.
     * Se utiliza para encriptar las contraseñas de los usuarios en la base de datos.
     *
     * @return PasswordEncoder, encargado de la encriptación de contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
