package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.models.Ticket;
import Proyecto.GestorAPI.services.PythonService;
import Proyecto.GestorAPI.services.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RestController
@RequestMapping("/api/ocr")
public class OCRController {

    private final TicketService ticketService;
    private final PythonService pythonService;

    public OCRController(TicketService ticketService, PythonService pythonService) {
        this.ticketService = ticketService;
        this.pythonService = pythonService;
    }

    @PostMapping("/ticket")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Subir archivo para procesar ticket con OCR",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
    )
    public ResponseEntity<?> processTicket(@RequestParam("archivo") MultipartFile file) { // Cambié "file" a "archivo"
        try {
            // 1. Guarda el archivo de manera temporal en el servidor
            File tempFile = saveFile(file);

            // 2. Enviar el archivo al servidor Python para realizar el OCR
            String ocrResultJson = pythonService.sendFileForOCR(tempFile);

            // 3. Crear un ticket temporal con el resultado del OCR
            Ticket ticket = new Ticket();
            ticket.setProductsJSON(ocrResultJson); // Asumimos que el JSON recibido es el resultado del OCR
            ticket.setCreatedAt(LocalDateTime.now()); // Fecha de creación

            // 4. Guardar el ticket en la base de datos (o mantenerlo temporal)
            Ticket savedTicket = ticketService.saveTicket(ticket);

            // 5. Eliminar archivo temporal
            tempFile.delete();

            // 6. Retorna el ticket con el JSON procesado
            return ResponseEntity.ok(savedTicket);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al procesar el archivo: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al procesar el OCR: " + e.getMessage());
        }
    }


    private File saveFile(MultipartFile file) throws IOException {
        // Guarda el archivo temporalmente en el servidor
        File tempFile = File.createTempFile("ticket-", ".jpg"); // Usa el tipo de archivo correcto según el archivo recibido
        file.transferTo(tempFile);
        return tempFile;
    }
}
