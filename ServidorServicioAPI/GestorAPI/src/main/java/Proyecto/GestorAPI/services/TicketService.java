package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.exceptions.ErrorPharseJsonException;
import Proyecto.GestorAPI.models.Ticket;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.ticket.CreateTicketRequest;
import Proyecto.GestorAPI.modelsDTO.ticket.TicketResponse;
import Proyecto.GestorAPI.modelsDTO.ticket.UpdateTicketRequest;

import java.util.List;
import java.util.Optional;

public interface TicketService {

    /**
     * Obtiene una lista de todos los tickets.
     *
     * Este método devuelve todos los objetos de tipo `Ticket` almacenados en el sistema.
     * Los registros de tickets pueden ser utilizados para mostrar un listado o resumen
     * de todos los tickets generados en la plataforma.
     *
     * @return Una lista de objetos `Ticket` que representan los tickets registrados.
     */
    List<Ticket> getAll();

    /**
     * Obtiene un ticket específico por su ID.
     *
     * Este método permite recuperar un ticket utilizando su identificador único (ID).
     * Devuelve un `Optional` para manejar casos en los que el ticket no exista.
     *
     * @param id El ID del ticket que se desea obtener.
     * @return Un `Optional` que contiene el ticket si se encuentra, o está vacío si no se encuentra.
     */
    Optional<Ticket> getByID(Long id);

    /**
     * Crea o actualiza un ticket en el sistema.
     *
     * Este método permite guardar un objeto `Ticket` en la base de datos, ya sea creando un nuevo ticket
     * o actualizando uno existente si ya tiene un ID.
     *
     * @param o El objeto `Ticket` que representa el ticket a guardar o actualizar.
     * @return El objeto `Ticket` guardado o actualizado.
     */
    Ticket setItem(Ticket o);

    /**
     * Elimina un ticket del sistema por su ID.
     *
     * Este método permite eliminar un ticket existente de la base de datos utilizando su ID.
     *
     * @param id El ID del ticket que se desea eliminar.
     */
    void deleteByID(Long id);

    /**
     * Verifica si un ticket con el ID especificado existe en el sistema.
     *
     * Este método comprueba si existe un registro de ticket con el ID proporcionado.
     *
     * @param id El ID del ticket que se desea comprobar.
     * @return `true` si el ticket existe, `false` si no existe.
     */
    boolean existsById(Long id);

    /**
     * Obtiene una lista de tickets asociada a un cliente específico.
     *
     * Este método permite recuperar todos los tickets asociados a un cliente, identificado por su `clienteId`.
     *
     * @param clienteId El ID del cliente cuyos tickets se desean obtener.
     * @return Una lista de objetos `Ticket` asociados al cliente indicado.
     */
    List<Ticket> getTicketsByUserId(Long clienteId);

    /**
     * Mapea y crea un objeto `Ticket` a partir de una solicitud de creación de ticket.
     *
     * @param request   El objeto `CreateTicketRequest` que contiene los datos para crear el ticket.
     * @param clienteId El ID del usuario al que se asignará el ticket.
     * @return El objeto `Ticket` creado a partir de la solicitud y el ID de usuario.
     */
    Ticket mappingCreateTicket(CreateTicketRequest request, Long clienteId);

    /**
     * Mapea y actualiza un objeto `Ticket` existente con los datos de la solicitud de actualización.
     *
     * @param request El objeto `UpdateTicketRequest` que contiene los nuevos datos para actualizar el ticket.
     * @param ticket  El objeto `Ticket` existente que será actualizado.
     * @return El objeto `Ticket` actualizado con los nuevos datos.
     */
    Ticket mappingUpdateTicket(UpdateTicketRequest request, Ticket ticket);

    /**
     * Mapea y crea un objeto `Ticket` a partir de un resultado OCR (Reconocimiento Óptico de Caracteres).
     *
     * Este método procesa el resultado OCR obtenido de una imagen o documento, y crea un ticket
     * asociado a un usuario específico.
     *
     * @param ocrResult El resultado en texto extraído mediante OCR.
     * @param user      El usuario al que se asignará el ticket creado.
     * @return El objeto `Ticket` creado a partir del resultado OCR y el usuario.
     * @throws ErrorPharseJsonException Si ocurre un error al parsear el resultado OCR.
     */
    Ticket mappingCreateTicketbyOCR(String ocrResult, User user) throws ErrorPharseJsonException;

}
