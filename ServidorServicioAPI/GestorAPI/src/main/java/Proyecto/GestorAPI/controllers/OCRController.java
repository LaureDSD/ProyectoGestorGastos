package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.models.Ticket;
import Proyecto.GestorAPI.services.OCRService;
import Proyecto.GestorAPI.services.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Objects;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RestController
@RequestMapping("/api/ocr")
@Tag(name = "OCR Service", description = "Procesamiento de tickets mediante OCR")
public class OCRController {

    private final TicketService ticketService;
    private final OCRService ocrService;

    public OCRController(TicketService ticketService, OCRService ocrService) {
        this.ticketService = ticketService;
        this.ocrService = ocrService;
    }

    @PostMapping(value = "/ticket", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Procesar imagen de ticket con OCR",
            description = "Recibe una imagen de ticket, la procesa con OCR y devuelve los datos estructurados",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
    )
    public ResponseEntity<?> processTicket(
            @RequestParam("archivo") MultipartFile file) {

        // Validaciones iniciales
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo no puede estar vacío");
        }

        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            return ResponseEntity.badRequest().body("Solo se permiten archivos de imagen");
        }

        Path tempFilePath = null;
        try {
            // 1. Guardar archivo temporalmente
            tempFilePath = Files.createTempFile("ticket_", "_" + file.getOriginalFilename());
            Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

            // 2. Procesar con OCR
            String ocrResult = ocrService.sendFileForOCR(tempFilePath.toFile());

            // 3. Crear y guardar ticket
            Ticket ticket = new Ticket();
            ticket.setProductsJSON(ocrResult);
            ticket.setCreatedAt(LocalDateTime.now());

            Ticket savedTicket = ticketService.setItem(ticket);

            return ResponseEntity.ok(savedTicket);

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error al procesar el archivo: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error en el procesamiento OCR: " + e.getMessage());
        } finally {
            // Limpieza del archivo temporal
            if (tempFilePath != null) {
                try {
                    Files.deleteIfExists(tempFilePath);
                } catch (IOException e) {
                    System.err.println("Error al eliminar archivo temporal: " + e.getMessage());
                }
            }
        }
    }
}