package Proyecto.GestorAPI.services;

/**
 * Interfaz para el servicio de chat con inteligencia artificial (IA).
 * Define el contrato para enviar mensajes y obtener respuestas generadas por IA.
 */
public interface AIChatService {

    /**
     * Envía un mensaje al sistema de IA y obtiene una respuesta.
     *
     * @param mensaje Texto del mensaje que se enviará a la IA.
     * @return Respuesta generada por la IA en formato String.
     */
    String enviarMensaje(String mensaje);
}
