package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.exceptions.ErrorPharseJsonException;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.models.enums.ExpenseClass;
import Proyecto.GestorAPI.modelsDTO.ticket.CreateTicketRequest;
import Proyecto.GestorAPI.modelsDTO.ticket.TicketResponse;
import Proyecto.GestorAPI.modelsDTO.ticket.UpdateTicketRequest;
import Proyecto.GestorAPI.repositories.TicketRepository;
import Proyecto.GestorAPI.services.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Proyecto.GestorAPI.models.Ticket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.time.LocalTime.now;

@RequiredArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {

    // Inyección del repositorio de Ticket para realizar operaciones en la base de datos.
    @Autowired
    private TicketRepository repository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CategoryExpenseServiceImpl categoriaService;

    /**
     * Obtiene todos los tickets registrados.
     *
     * Este método consulta la base de datos para obtener una lista de todos los tickets.
     *
     * @return Una lista con todos los tickets registrados en la base de datos.
     */
    @Override
    public List<Ticket> getAll() {
        return repository.findAll();
    }

    /**
     * Obtiene un ticket por su ID.
     *
     * Este método busca un ticket específico a partir de su ID. Si el ticket existe, 
     * se devuelve un `Optional` con el objeto `Ticket`. Si no se encuentra, se devuelve un `Optional` vacío.
     *
     * @param id El ID del ticket a buscar.
     * @return Un `Optional` que contiene el ticket si se encuentra, o un `Optional.empty()` si no se encuentra.
     */
    @Override
    public Optional<Ticket> getByID(Long id) {
        return repository.findById(id);
    }

    /**
     * Guarda o actualiza un ticket.
     *
     * Este método guarda un nuevo ticket o actualiza uno existente en la base de datos. Si el objeto 
     * `Ticket` ya existe, se actualizará con la nueva información. Si no, se creará un nuevo 
     * registro en la base de datos.
     *
     * @param o El objeto `Ticket` que se desea guardar o actualizar.
     * @return El objeto `Ticket` guardado o actualizado.
     */
    @Override
    public Ticket setItem(Ticket o) {
        return repository.save(o);
    }

    /**
     * Elimina un ticket por su ID.
     *
     * Este método elimina un ticket de la base de datos utilizando su ID. Si el ID es válido, 
     * el ticket será eliminado. Si no se encuentra el ticket, se lanzará una excepción.
     *
     * @param id El ID del ticket a eliminar.
     */
    @Override
    public void deleteByID(Long id) {
        repository.deleteById(id);
    }

    /**
     * Verifica si existe un ticket con el ID proporcionado.
     *
     * Este método verifica si existe un ticket en la base de datos con el ID proporcionado.
     *
     * @param id El ID del ticket a verificar.
     * @return `true` si existe un ticket con el ID dado, `false` si no existe.
     */
    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    /**
     * Obtiene los tickets de un cliente específico.
     *
     * Este método busca todos los tickets asociados a un cliente a partir de su ID. Utiliza el 
     * método `findByUserId()` del repositorio para obtener los tickets correspondientes al cliente.
     *
     * @param id El ID del cliente a buscar los tickets.
     * @return Una lista de tickets asociados al cliente especificado.
     */
    @Override
    public List<Ticket> getTicketsByUserId(Long id) {
        return repository.findByUserId(id);
    }

    @Override
    public Ticket mappingCreateTicket(CreateTicketRequest request, Long clienteId) {
        // Crear el ticket con referencias por ID
        Ticket ticket = new Ticket();

        ticket.setUser(userService.getUserById(clienteId).orElse(new User()));
        ticket.setCategory(categoriaService.getByID(request.getCategoriaId()).orElse(null));
        ticket.setExpenseDate(request.getFechaCompra());
        ticket.setTotal(request.getTotal());
        ticket.setIcon(request.getIcon());
        ticket.setDescription(request.getDescription());
        ticket.setName(request.getName());
        ticket.setStore(request.getStore());
        ticket.setProductsJSON(request.getProductsJSON());
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setTypeExpense(ExpenseClass.TICKET);
        return  ticket;
    }

    @Override
    public Ticket mappingUpdateTicket(UpdateTicketRequest request, Ticket ticket) {
        // Actualización
        ticket.setCategory(categoriaService.getByID(request.getCategoriaId()).orElse(null));
        ticket.setStore(request.getStore());
        ticket.setExpenseDate(request.getFechaCompra());
        ticket.setName(request.getName());
        ticket.setDescription(request.getDescription());
        ticket.setTotal(request.getTotal());
        ticket.setIva(request.getIva());
        ticket.setIcon(request.getIcon());
        ticket.setIcon(request.getIcon());
        ticket.setProductsJSON(request.getProductsJSON());
        return ticket;
    }

    @Override
    public Ticket mappingCreateTicketbyOCR(String ocrResult, User user) throws ErrorPharseJsonException {
        ObjectMapper mapper = new ObjectMapper();

        TicketResponse ticketOCR = new TicketResponse();
        String articulosJson = "[]";

        System.out.println("Mappear");

        try {
            ticketOCR = mapper.readValue(ocrResult, TicketResponse.class);
            try {
                articulosJson = mapper.writeValueAsString(ticketOCR.getArticulos());
            } catch (Exception e) {
                System.out.println("Error al convertir artículos a JSON: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new ErrorPharseJsonException("Error al cargar ocr desde Python: " + e);
        }

        System.out.println("Mappear2");

        Ticket ticket = new Ticket();
        ticket.setUser(user);

        // Establecimiento
        try {
            ticket.setStore(ticketOCR.getEstablecimiento() != null ? ticketOCR.getEstablecimiento() : "Desconocido");
        } catch (Exception e) {
            ticket.setStore("Desconocido");
        }

        ticket.setProductsJSON(articulosJson);

        // Total
        try {
            ticket.setTotal(ticketOCR.getTotal() != null ? ticketOCR.getTotal() : 0.0);
        } catch (Exception e) {
            ticket.setTotal(0.0);
        }

        // IVA
        try {
            ticket.setIva(ticketOCR.getIva() != null ? ticketOCR.getIva() : 0.0);
        } catch (Exception e) {
            ticket.setIva(0.0);
        }

        // Nombre
        try {
            ticket.setName(ticketOCR.getEstablecimiento() != null ? ticketOCR.getEstablecimiento() : "Ticket");
        } catch (Exception e) {
            ticket.setName("Ticket");
        }

        ticket.setDescription("Sin descripción..");

        // Categoría (por defecto ID = 1)
        try {
            ticket.setCategory(categoriaService.getByID(1L).orElse(null));
        } catch (Exception e) {
            ticket.setCategory(null);
        }

        // Fecha y hora
        LocalDate fecha = LocalDate.now();
        LocalTime hora = LocalTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        try {
            if (ticketOCR.getFecha() != null && !ticketOCR.getFecha().isEmpty()) {
                fecha = LocalDate.parse(ticketOCR.getFecha(), dateFormatter);
            }
        } catch (Exception e) {
            System.out.println("Fecha inválida, usando actual: " + e.getMessage());
        }

        try {
            if (ticketOCR.getHora() != null && !ticketOCR.getHora().isEmpty()) {
                hora = LocalTime.parse(ticketOCR.getHora(), timeFormatter);
            }
        } catch (Exception e) {
            System.out.println("Hora inválida, usando actual: " + e.getMessage());
        }

        ticket.setExpenseDate(LocalDateTime.of(fecha, hora));
        ticket.setTypeExpense(ExpenseClass.TICKET);

        System.out.println("Mappear3");
        return ticket;
    }

}
