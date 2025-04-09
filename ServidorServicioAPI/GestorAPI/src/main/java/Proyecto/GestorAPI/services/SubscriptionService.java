package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.models.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {
    List<Subscription> getAll();

    Optional<Subscription> getByID(Long id);

    Subscription setItem(Subscription o);

    void deleteByID(Long id);

    boolean existsById(Long id);
}
