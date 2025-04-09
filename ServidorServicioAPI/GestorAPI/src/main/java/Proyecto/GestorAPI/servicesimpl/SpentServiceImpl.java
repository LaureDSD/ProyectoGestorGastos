package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.models.Spent;
import Proyecto.GestorAPI.repositories.SpentRepository;
import Proyecto.GestorAPI.services.SpentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpentServiceImpl implements SpentService{

    @Autowired
    private SpentRepository repository;

    @Override
    public List<Spent> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Spent> getByID(Long id) {
        return repository.findById(id);
    }

    @Override
    public Spent setItem(Spent o) {
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