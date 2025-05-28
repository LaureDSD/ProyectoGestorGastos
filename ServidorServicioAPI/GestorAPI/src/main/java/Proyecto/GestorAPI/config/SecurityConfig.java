package Proyecto.GestorAPI.config;

import Proyecto.GestorAPI.config.security.JwtAuthenticationFilter;
import Proyecto.GestorAPI.config.security.RoleServer;
import Proyecto.GestorAPI.config.security.TokenProvider;
import Proyecto.GestorAPI.config.security.oauth2.CustomAuthenticationSuccessHandler;
import Proyecto.GestorAPI.config.security.oauth2.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
    public JwtAuthenticationFilter tokenAuthenticationFilter() {
        return new JwtAuthenticationFilter(userDetailsService, tokenProvider);
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
                        .requestMatchers(
                                //"/admin/dashboard",
                                "/uploads/**",
                                "/auth/**",
                                "/static/**",
                                "/resources/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/public/**",
                                "/favicon.ico",
                                "/oauth2/**",
                                "/api/**",
                                "/error",
                                "/csrf",
                                "/swagger-ui/**",
                                "/admin/dashboard",
                                "/v3/api-docs/**")

                        .permitAll()

                        // Acceso restringido a los usuarios con roles ADMIN o USER
                        .requestMatchers(
                                "/api/**",
                                "/uploads/perfiles/**",
                                "/uploads/gastos/**")
                        .hasAnyAuthority(ADMIN,USER)

                        //Solo admins
                        .requestMatchers(
                                "/api/contacto",
                                "/api/contacto/**",
                                "/api/users",
                                "/api/users/**",
                                "/admin/**")
                        .hasAnyAuthority(ADMIN)


                        // Cualquier otra solicitud requiere autenticación
                        .anyRequest().authenticated())


                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/logout")
                        .addLogoutHandler(new HeaderWriterLogoutHandler(
                                new ClearSiteDataHeaderWriter(
                                        ClearSiteDataHeaderWriter.Directive.COOKIES,
                                        ClearSiteDataHeaderWriter.Directive.STORAGE,
                                        ClearSiteDataHeaderWriter.Directive.CACHE
                                )
                        ))
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession()
                        .maximumSessions(1)
                        .expiredUrl("/login?expired")
                )



                // Configuración de login OAuth2
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOauth2UserService))
                        .successHandler(customAuthenticationSuccessHandler))
                // Configuración de logout
                .logout(logout -> logout.logoutSuccessUrl("/").permitAll())
                // Añadir filtro de autenticación antes del filtro por defecto de nombre y contraseña
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // Configuración para sesiones sin estado (sin sesión en el servidor)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                // Manejo de errores de autenticación (401)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Permitir Angular
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // Permitir credenciales (JWT en cookies)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
