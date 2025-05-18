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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/subscripciones")
@Tag(name = "Subscription Management (Verify control)", description = "Operaciones sobre suscripciones")
public class SubscriptionController {

    @Autowired
    private final SubscriptionService subscriptionService;

    @Autowired
    private final UserService userService;

    @Autowired
    private CategoryExpenseService categoriaService;

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
            //si no es admin
            subscriptions = subscriptionService.getSubscriptionsByUserId(user.getId());
        }else{
            //si es admin
            subscriptions = (clienteId != null)
                    ? subscriptionService.getSubscriptionsByUserId(clienteId)
                    : subscriptionService.getAll();
        }
        //Verificacion de existencia
        if (subscriptions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        System.out.println(subscriptions.get(1).toString());
        //Devolucion
        return ResponseEntity.ok(subscriptions.stream()
                .map(SubscriptionDto::from)
                .collect(Collectors.toList()));
    }

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

        // Verificación de existencia
        if (subscription == null) {
            return ResponseEntity.notFound().build();
        }

        // Verificación de propiedad (solo admin o dueño puede ver)
        if (user.getRole() != RoleServer.ADMIN && !subscription.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        System.out.println("Pide: "+subscription);
        System.out.println("Fecha de compra real: " + subscription.getExpenseDate());

        System.out.println( "Envia: "+SubscriptionDto.from(subscription));


        return ResponseEntity.ok(SubscriptionDto.from(subscription));
    }


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

        if(user.getRole()!= RoleServer.ADMIN){
            //si no es admin
            subscription = mappingSubscription(request, user.getId());
        }else{
            //si es admin
            subscription = mappingSubscription(request,(clienteId != null) ? clienteId : user.getId());
        }
        //Trampita
        subscription.setAccumulate(((request.total() * (request.iva()/100)) + request.total()));
        //Creacion
        Subscription createdSubscription = subscriptionService.setItem(subscription);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SubscriptionDto.from(createdSubscription));
    }

    private Subscription mappingSubscription(CreateSubscriptionRequest request , Long clienteId){
        // Crear la suscripción con referencias por ID
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
        //Verificacionde existencia
        if ( subscription == null){
            return ResponseEntity.notFound().build();
        }
        //Verificacion de propiedad
        if(user.getRole() != RoleServer.ADMIN && !subscription.getUser().getId().equals(user.getId())){
            return ResponseEntity.badRequest().build();
        }
        //Eliminacion
        subscriptionService.deleteByID(subscriptionId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{subscriptionId}")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Actualizar una suscripción por ID"
    )
    public ResponseEntity<SubscriptionDto> updateSubscription(
            @PathVariable Long subscriptionId,
            @Valid @RequestBody UpdateSubscriptionRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) throws ResourceNotFoundException {

        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        Subscription subscription = subscriptionService.getByID(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

        // Verificación de propiedad
        if(user.getRole() != RoleServer.ADMIN && !subscription.getUser().getId().equals(user.getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Validación del id en el request vs path param
        if (request.spentId() == null || !request.spentId().equals(subscriptionId)) {
            return ResponseEntity.badRequest().build();
        }

        System.out.println("Entra:  "+request);
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
        // No cambiar user ni typeExpense
        //System.out.println("Guarda: "+subscription);

        //System.out.println("ANTES DE GUARDAR (Controller):");
        //System.out.println("Activa: " + subscription.isActiva());
        //System.out.println("Start: " + subscription.getStart());
        //System.out.println("ExpenseDate: " + subscription.getExpenseDate());

        Subscription updatedSubscription = subscriptionService.setItem(subscription);

        //System.out.println("Retorna: "+updatedSubscription);
        return ResponseEntity.ok(SubscriptionDto.from(updatedSubscription));
    }
}