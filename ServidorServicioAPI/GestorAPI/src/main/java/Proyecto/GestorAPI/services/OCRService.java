package Proyecto.GestorAPI.services;

import Proyecto.GestorAPI.exceptions.ErrorPharseJsonException;
import Proyecto.GestorAPI.models.Ticket;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.ticket.CreateTicketRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Interfaz para el servicio OCR (Reconocimiento Óptico de Caracteres).
 * Define métodos para enviar archivos a un servicio externo de OCR,
 * procesar imágenes y tickets digitales para extraer información relevante.
 */
public interface OCRService {

    /**
     * Envía un archivo al servicio externo (Python) para realizar OCR y obtener texto.
     *
     * Este método envía un archivo (por ejemplo, imagen o documento escaneado) a un servicio de Python
     * que ejecuta OCR para extraer el texto contenido en el archivo.
     *
     * @param file Archivo que se enviará para el procesamiento OCR.
     * @param imagen Indica si el archivo enviado es una imagen.
     * @return Texto extraído tras aplicar OCR.
     * @throws IOException Si ocurre un error leyendo el archivo o en la comunicación con el servicio externo.
     */
    String sendFileForOCR(File file, boolean imagen) throws IOException;

    /**
     * Procesa una imagen que contiene un ticket, extrayendo la información relevante y generando un objeto Ticket.
     *
     * @param file Archivo MultipartFile que contiene la imagen del ticket.
     * @param user Usuario asociado al ticket.
     * @return Objeto Ticket con la información extraída.
     * @throws IOException Si ocurre un error en la lectura del archivo.
     * @throws ErrorPharseJsonException Si hay un error al parsear el JSON resultante del OCR.
     */
    Ticket processImageTicket(MultipartFile file, User user) throws IOException, ErrorPharseJsonException;

    /**
     * Procesa un ticket digital (no imagen) a partir de un archivo MultipartFile,
     * extrayendo la información relevante y generando un objeto Ticket.
     *
     * @param file Archivo MultipartFile que contiene el ticket digital.
     * @param user Usuario asociado al ticket.
     * @return Objeto Ticket con la información extraída.
     * @throws IOException Si ocurre un error en la lectura del archivo.
     */
    Ticket proccessDigitalTicket(MultipartFile file, User user) throws IOException;

}
