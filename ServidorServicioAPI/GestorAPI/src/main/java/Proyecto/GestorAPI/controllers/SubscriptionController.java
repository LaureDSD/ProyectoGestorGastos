package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.models.Subscription;
import Proyecto.GestorAPI.modelsDTO.CreateSubscriptionRequest;
import Proyecto.GestorAPI.modelsDTO.SubscriptionDto;
import Proyecto.GestorAPI.services.SubscriptionService;
import Proyecto.GestorAPI.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/subscriptions")
@Tag(name = "Subscription Management", description = "Operaciones sobre suscripciones")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserService userService;

    @GetMapping("/")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Obtener todas las suscripciones filtradas por clienteId (opcional)"
    )
    public ResponseEntity<List<SubscriptionDto>> getSubscriptions(
            @RequestParam(value = "clienteId", required = false) Long clienteId) {

        List<Subscription> subscriptions = (clienteId != null)
                ? subscriptionService.getSubscriptionsByUserId(clienteId)
                : subscriptionService.getAll();

        if (subscriptions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(subscriptions.stream()
                .map(SubscriptionDto::from)
                .collect(Collectors.toList()));
    }


    @PostMapping("/")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Crear una nueva suscripción"
    )
    public ResponseEntity<SubscriptionDto> createSubscription(
            @Valid @RequestBody CreateSubscriptionRequest request) {

        // Crear la suscripción con referencias por ID
        Subscription subscription = new Subscription();
        subscription.setUser(userService.getUserById(request.userId()).orElse(null));
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

        Subscription createdSubscription = subscriptionService.setItem(subscription);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SubscriptionDto.from(createdSubscription));
    }

    @DeleteMapping("/{subscriptionId}")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Eliminar una suscripción por ID"
    )
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long subscriptionId) {
        if (!subscriptionService.existsById(subscriptionId)) {
            return ResponseEntity.notFound().build();
        }
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
            @Valid @RequestBody Subscription subscription) {

        if (subscription.getSpent_id() == null || !subscription.getSpent_id().equals(subscriptionId)) {
            return ResponseEntity.badRequest().build();
        }

        Subscription updatedSubscription = subscriptionService.setItem(subscription);
        return ResponseEntity.ok(SubscriptionDto.from(updatedSubscription));
    }
}
