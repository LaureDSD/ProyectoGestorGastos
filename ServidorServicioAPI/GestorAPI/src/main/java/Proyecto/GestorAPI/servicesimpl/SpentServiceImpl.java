package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.models.Spent;
import Proyecto.GestorAPI.models.Subscription;
import Proyecto.GestorAPI.models.Ticket;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.models.enums.ExpenseClass;
import Proyecto.GestorAPI.modelsDTO.spent.CreateSpentRequest;
import Proyecto.GestorAPI.modelsDTO.spent.SpentFullDto;
import Proyecto.GestorAPI.modelsDTO.spent.UpdateSpentRequest;
import Proyecto.GestorAPI.repositories.SpentRepository;
import Proyecto.GestorAPI.services.CategoryExpenseService;
import Proyecto.GestorAPI.services.SpentService;
import Proyecto.GestorAPI.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SpentServiceImpl implements SpentService {

    // Inyección del repositorio de Spent para realizar operaciones en la base de datos.
    @Autowired
    private SpentRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryExpenseService categoryExpenseService;

    /**
     * Obtiene todos los gastos registrados.
     *
     * Este método consulta la base de datos para obtener una lista de todos los objetos `Spent`.
     *
     * @return Una lista con todos los gastos registrados en la base de datos.
     */
    @Override
    public List<Spent> getAll() {
        return repository.findAll();
    }

    /**
     * Obtiene un gasto por su ID.
     *
     * Este método busca un gasto específico a partir de su ID. Si el gasto existe,
     * se devuelve un `Optional` con el objeto `Spent`. Si no se encuentra, se devuelve un `Optional` vacío.
     *
     * @param id El ID del gasto a buscar.
     * @return Un `Optional` que contiene el gasto si se encuentra, o un `Optional.empty()` si no se encuentra.
     */
    @Override
    public Optional<Spent> getByID(Long id) {
        return repository.findById(id);
    }

    /**
     * Guarda o actualiza un gasto.
     *
     * Este método guarda un nuevo gasto o actualiza un gasto existente en la base de datos. Si el
     * objeto `Spent` ya existe, se actualizará con la nueva información. Si no, se creará un nuevo
     * registro en la base de datos.
     *
     * @param o El objeto `Spent` que se desea guardar o actualizar.
     * @return El objeto `Spent` guardado o actualizado.
     */
    @Override
    public Spent setItem(Spent o) {
        return repository.save(o);
    }

    /**
     * Elimina un gasto por su ID.
     *
     * Este método elimina un gasto de la base de datos utilizando su ID. Si el ID es válido,
     * el gasto será eliminado. Si no se encuentra el gasto, se lanzará una excepción.
     *
     * @param id El ID del gasto a eliminar.
     */
    @Override
    public void deleteByID(Long id) {
        repository.deleteById(id);
    }

    /**
     * Verifica si existe un gasto con el ID proporcionado.
     *
     * Este método verifica si existe un gasto en la base de datos con el ID proporcionado.
     *
     * @param id El ID del gasto a verificar.
     * @return `true` si existe un gasto con el ID dado, `false` si no existe.
     */
    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    /**
     * Obtiene la lista de gastos (Spents) asociados a un usuario específico.
     *
     * @param id Identificador único del usuario.
     * @return Lista de objetos Spent relacionados con el usuario dado.
     */
    @Override
    public List<Spent> getSpentsByUserId(Long id) {
        return repository.getByUserId(id);
    }

    /**
     * Cuenta la cantidad total de gastos asociados a un usuario específico.
     *
     * @param userId Identificador único del usuario.
     * @return Número total de gastos registrados para el usuario.
     */
    @Override
    public long countSpentsByUserId(Long userId) {
        return repository.countByUserId(userId);
    }

    /**
     * Obtiene el conteo total de gastos registrados en el sistema.
     *
     * @return Número total de gastos (Spents) almacenados en el repositorio.
     */
    @Override
    public int getCountSpents() {
        return repository.countGastos();
    }

    /**
     * Mapea los datos de la petición de actualización a entidad Spent.
     *
     * @param request Datos de actualización
     * @param clienteId Id del cliente propietario
     * @return Entidad Spent con datos actualizados
     */
    @Override
    public Spent mappingUpdateSpent(UpdateSpentRequest request, Long clienteId) {
        Spent spent = new Spent();
        spent.setSpentId(request.spentId());
        spent.setUser(userService.getUserById(clienteId).orElse(new User()));
        spent.setCategory(categoryExpenseService.getByID(request.categoriaId()).orElse(null));
        spent.setExpenseDate(request.fechaCompra());
        spent.setTypeExpense(ExpenseClass.valueOf(request.typeExpense()));
        spent.setTotal(request.total());
        spent.setIva(request.iva());
        spent.setName(request.name());
        spent.setDescription(request.description());
        spent.setIcon(request.icon());
        spent.setCreatedAt(LocalDateTime.now());
        return spent;
    }

    /**
     * Mapea los datos de la petición de creación a entidad Spent.
     *
     * @param request Datos para crear el gasto
     * @param clienteId Id del cliente propietario
     * @return Entidad Spent creada
     */
    @Override
    public Spent mappingSpent(CreateSpentRequest request, Long clienteId) {
        Spent spent = new Spent();
        spent.setUser(userService.getUserById(clienteId).orElse(new User()));
        spent.setCategory(categoryExpenseService.getByID(request.categoriaId()).orElse(null));
        spent.setExpenseDate(request.fechaCompra());
        spent.setTypeExpense(ExpenseClass.valueOf(request.typeExpense()));
        spent.setTotal(request.total());
        spent.setIva(request.iva());
        spent.setName(request.name());
        spent.setDescription(request.description());
        spent.setIcon(request.icon());
        spent.setCreatedAt(LocalDateTime.now());
        return spent;
    }

    /**
     * Método auxiliar para mapear una lista de entidades Spent a DTOs completos.
     * @param spents Lista de gastos
     * @return Lista de DTOs completos
     */
    @Override
    public List<SpentFullDto> mappingSpentFullDtosList(List<Spent> spents) {
        List<SpentFullDto> result = new ArrayList<>();
        for (Spent gasto : spents) {
            result.add(mappingSpentFullDto(gasto));
        }
        return result;
    }

    /**
     * Método auxiliar para mapear una entidad Spent a DTO completo.
     * Incluye detalles específicos según el tipo de gasto (Ticket o Subscription).
     *
     * @param gasto Entidad gasto
     * @return DTO completo con todos los campos relevantes
     */
    @Override
    public SpentFullDto mappingSpentFullDto(Spent gasto) {
        SpentFullDto dto = new SpentFullDto();
        dto.setSpentId(gasto.getSpentId());
        dto.setUserId(gasto.getUser().getId());
        dto.setCategoriaId(gasto.getCategory().getId());
        dto.setName(gasto.getName());
        dto.setDescription(gasto.getDescription());
        dto.setIcon(gasto.getIcon());
        dto.setFechaCompra(gasto.getExpenseDate());
        dto.setTotal(gasto.getTotal());
        dto.setIva(gasto.getIva());
        dto.setTypeExpense(gasto.getTypeExpense());

        // Si es Ticket, agregar campos específicos
        if (gasto instanceof Ticket ticket) {
            dto.setStore(ticket.getStore());
            dto.setProductsJSON(ticket.getProductsJSON());
        }

        // Si es Subscription, agregar campos específicos
        if (gasto instanceof Subscription sub) {
            dto.setStart(sub.getStart());
            dto.setEnd(sub.getEnd());
            dto.setAccumulate(sub.getAccumulate());
            dto.setRestartDay(sub.getRestartDay());
            dto.setIntervalTime(sub.getIntervalTime());
            dto.setActiva(sub.isActiva());
        }
        return dto;
    }

}
