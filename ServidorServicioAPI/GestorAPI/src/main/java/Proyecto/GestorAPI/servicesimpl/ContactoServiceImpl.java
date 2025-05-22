package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.models.FormContacto;
import Proyecto.GestorAPI.repositories.ContactoRepository;
import Proyecto.GestorAPI.services.ContactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactoServiceImpl implements ContactoService {

    @Autowired
    private ContactoRepository contactoRepository;


    @Override
    public List<FormContacto> getAll() {
        return contactoRepository.findAll();
    }

    @Override
    public Optional<FormContacto> getByID(Long id) {
        return contactoRepository.findById(id);
    }

    @Override
    public FormContacto setItem(FormContacto contacto) {
        return contactoRepository.save(contacto);
    }

    @Override
    public void deleteByID(Long id) {
        contactoRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return contactoRepository.existsById(id);
    }
}
