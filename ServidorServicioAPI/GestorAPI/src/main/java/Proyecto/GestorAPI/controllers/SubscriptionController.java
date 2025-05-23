package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.exceptions.ResourceNotFoundException;
import Proyecto.GestorAPI.models.Subscription;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.models.enums.ExpenseClass;
import Proyecto.GestorAPI.modelsDTO.subscription.CreateSubscriptionRequest;
import Proyecto.GestorAPI.modelsDTO.subscription.SubscriptionDto;
import Proyecto.GestorAPI.modelsDTO.subscription.UpdateSubscriptionRequest;
import Proyecto.GestorAPI.config.security.CustomUserDetails;
import Proyecto.GestorAPI.config.security.RoleServer;
import Proyecto.GestorAPI.services.CategoryExpenseService;
import Proyecto.GestorAPI.services.SubscriptionService;
import Proyecto.GestorAPI.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

/**
 * Controlador REST para la gestión de suscripciones.
 *
 * Provee endpoints para crear, obtener, actualizar y eliminar suscripciones.
 * La seguridad está basada en autenticación con token Bearer y control de acceso según rol.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/subscripciones")
@Tag(name = "Subscription Management (Verify control)", description = "Operaciones sobre suscripciones")
public class SubscriptionController {

    // Servicios necesarios inyectados para operaciones con suscripciones, usuarios y categorías
    @Autowired
    private final SubscriptionService subscriptionService;

    @Autowired
    private final UserService userService;

    @Autowired
    private CategoryExpenseService categoriaService;

    /**
     * Obtiene todas las suscripciones, filtradas opcionalmente por clienteId.
     *
     * - Si el usuario autenticado no es administrador, solo devuelve sus propias suscripciones.
     * - Si es administrador, puede consultar todas o filtrar por clienteId.
     * - Retorna 204 NO CONTENT si no hay resultados.
     *
     * @param clienteId (Opcional) ID del cliente para filtrar suscripciones (solo admins).
     * @param currentUser Usuario autenticado.
     * @return Lista de SubscriptionDto con suscripciones o 204 si no hay datos.
     */
    @GetMapping("")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Obtener todas las suscripciones filtradas por clienteId (opcional para admins)"
    )
    public ResponseEntity<List<SubscriptionDto>> getSubscriptions(
            @RequestParam(value = "clienteId", required = false) Long clienteId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        List<Subscription> subscriptions = new ArrayList<>();

        if(user.getRole() != RoleServer.ADMIN){
            // No admin: obtener solo sus propias suscripciones
            subscriptions = subscriptionService.getSubscriptionsByUserId(user.getId());
        }else{
            // Admin: puede filtrar por clienteId o obtener todas
            subscriptions = (clienteId != null)
                    ? subscriptionService.getSubscriptionsByUserId(clienteId)
                    : subscriptionService.getAll();
        }

        // Si no hay suscripciones, retornar 204 No Content
        if (subscriptions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Retornar lista convertida a DTO
        return ResponseEntity.ok(subscriptions.stream()
                .map(SubscriptionDto::from)
                .collect(Collectors.toList()));
    }

    /**
     * Obtiene una suscripción por su ID.
     *
     * - Solo el administrador o el propietario pueden acceder a la suscripción.
     * - Retorna 404 si la suscripción no existe.
     * - Retorna 403 Forbidden si el usuario no tiene permiso.
     *
     * @param subscriptionId ID de la suscripción.
     * @param currentUser Usuario autenticado.
     * @return SubscriptionDto con la suscripción solicitada o error HTTP correspondiente.
     */
    @GetMapping("/{subscriptionId}")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Obtener una suscripción por ID"
    )
    public ResponseEntity<SubscriptionDto> getSubscriptionById(
            @PathVariable Long subscriptionId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Subscription subscription = subscriptionService.getByID(subscriptionId).orElse(null);

        // Verificar existencia de la suscripción
        if (subscription == null) {
            return ResponseEntity.notFound().build();
        }

        // Verificar propiedad o rol admin para permitir acceso
        if (user.getRole() != RoleServer.ADMIN && !subscription.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Retornar DTO de la suscripción encontrada
        return ResponseEntity.ok(SubscriptionDto.from(subscription));
    }

    /**
     * Crea una nueva suscripción.
     *
     * - El usuario autenticado puede crear para sí mismo.
     * - El administrador puede crear para otro cliente usando clienteId.
     * - Calcula el campo 'accumulate' basado en total e IVA.
     *
     * @param clienteId (Opcional) ID del cliente para crear la suscripción (solo admins).
     * @param request Datos de la suscripción a crear.
     * @param currentUser Usuario autenticado.
     * @return SubscriptionDto con la suscripción creada y código 201 Created.
     */
    @PostMapping("")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Crear una nueva suscripción"
    )
    public ResponseEntity<SubscriptionDto> createSubscription(
            @RequestParam(value = "clienteId", required = false) Long clienteId,
            @Valid @RequestBody CreateSubscriptionRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Subscription subscription;

        if(user.getRole() != RoleServer.ADMIN){
            // No admin: crear para sí mismo
            subscription = mappingSubscription(request, user.getId());
        }else{
            // Admin: crear para clienteId o para sí mismo si clienteId es null
            subscription = mappingSubscription(request, (clienteId != null) ? clienteId : user.getId());
        }

        // Cálculo del acumulado considerando IVA y total
        subscription.setAccumulate(((request.total() * (request.iva()/100)) + request.total()));

        // Guardar suscripción en base de datos
        Subscription createdSubscription = subscriptionService.setItem(subscription);

        // Retornar respuesta con DTO y código 201
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SubscriptionDto.from(createdSubscription));
    }

    /**
     * Método auxiliar para mapear CreateSubscriptionRequest a entidad Subscription.
     *
     * - Asocia la suscripción con usuario y categoría mediante sus IDs.
     * - Establece el tipo de gasto como SUBSCRIPCIÓN.
     *
     * @param request Datos recibidos para crear la suscripción.
     * @param clienteId ID del usuario propietario.
     * @return Objeto Subscription mapeado.
     */
    private Subscription mappingSubscription(CreateSubscriptionRequest request, Long clienteId){
        Subscription subscription = new Subscription();
        subscription.setUser(userService.getUserById(clienteId).orElse(null));
        subscription.setName(request.name());
        subscription.setDescription(request.description());
        subscription.setIcon(request.icon());
        subscription.setExpenseDate(request.fechaCompra());
        subscription.setTotal(request.total());
        subscription.setIva(request.iva());
        subscription.setStart(request.start());
        subscription.setEnd(request.end());
        subscription.setAccumulate(request.accumulate());
        subscription.setRestartDay(request.restartDay());
        subscription.setIntervalTime(request.intervalTime());
        subscription.setActiva(request.activa());
        subscription.setCategory(categoriaService.getByID(request.categoriaId()).orElse(null));
        subscription.setTypeExpense(ExpenseClass.SUBSCRIPCION);
        return subscription;
    }

    /**
     * Elimina una suscripción por su ID.
     *
     * - Solo el propietario o administrador pueden eliminar.
     * - Retorna 404 si no existe la suscripción.
     * - Retorna 400 Bad Request si el usuario no tiene permiso.
     *
     * @param subscriptionId ID de la suscripción a eliminar.
     * @param currentUser Usuario autenticado.
     * @return Respuesta vacía con código 204 No Content o error.
     */
    @DeleteMapping("/{subscriptionId}")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Eliminar una suscripción por ID"
    )
    public ResponseEntity<Void> deleteSubscription(
            @PathVariable Long subscriptionId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Subscription subscription = subscriptionService.getByID(subscriptionId).orElse(null);

        // Verificación existencia
        if (subscription == null){
            return ResponseEntity.notFound().build();
        }

        // Verificación de propiedad o rol admin para eliminar
        if(user.getRole() != RoleServer.ADMIN && !subscription.getUser().getId().equals(user.getId())){
            return ResponseEntity.badRequest().build();
        }

        // Eliminar suscripción
        subscriptionService.deleteByID(subscriptionId);

        // Retornar sin contenido 204
        return ResponseEntity.noContent().build();
    }

    /**
     * Actualiza una suscripción existente.
     *
     * - Solo propietario o administrador pueden actualizar.
     * - Retorna 404 si la suscripción no existe.
     * - Retorna 400 si el usuario no tiene permiso.
     *
     * @param subscriptionId ID de la suscripción a actualizar.
     * @param request Datos para actualización.
     * @param currentUser Usuario autenticado.
     * @return SubscriptionDto con la suscripción actualizada o error HTTP.
     */
    @PutMapping("/{subscriptionId}")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Actualizar una suscripción existente"
    )
    public ResponseEntity<SubscriptionDto> updateSubscription(
            @PathVariable Long subscriptionId,
            @Valid @RequestBody UpdateSubscriptionRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Subscription subscription = subscriptionService.getByID(subscriptionId).orElse(null);

        // Verificación existencia
        if (subscription == null) {
            return ResponseEntity.notFound().build();
        }

        // Verificación propiedad o rol admin para modificar
        if(user.getRole() != RoleServer.ADMIN && !subscription.getUser().getId().equals(user.getId())){
            return ResponseEntity.badRequest().build();
        }

        // Actualización de campos permitidos
        subscription.setName(request.name());
        subscription.setDescription(request.description());
        subscription.setIcon(request.icon());
        subscription.setExpenseDate(request.fechaCompra());
        subscription.setTotal(request.total());
        subscription.setIva(request.iva());
        subscription.setStart(request.start());
        subscription.setEnd(request.end());
        subscription.setAccumulate(request.accumulate());
        subscription.setRestartDay(request.restartDay());
        subscription.setIntervalTime(request.intervalTime());
        subscription.setActiva(request.activa());
        subscription.setCategory(categoriaService.getByID(request.categoriaId()).orElse(null));
        subscription.setTypeExpense(ExpenseClass.SUBSCRIPCION);

        // Guardar cambios
        Subscription updatedSubscription = subscriptionService.setItem(subscription);

        // Retornar DTO actualizado
        return ResponseEntity.ok(SubscriptionDto.from(updatedSubscription));
    }

}
