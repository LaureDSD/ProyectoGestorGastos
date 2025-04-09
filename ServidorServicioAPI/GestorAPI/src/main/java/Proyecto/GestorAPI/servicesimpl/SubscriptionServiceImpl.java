package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.models.Subscription;
import Proyecto.GestorAPI.repositories.SubscriptionRepository;
import Proyecto.GestorAPI.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository repository;

    @Override
    public List<Subscription> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Subscription> getByID(Long id) {
        return repository.findById(id);
    }

    @Override
    public Subscription setItem(Subscription o) {
        return repository.save(o);
    }

    @Override
    public void deleteByID(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
