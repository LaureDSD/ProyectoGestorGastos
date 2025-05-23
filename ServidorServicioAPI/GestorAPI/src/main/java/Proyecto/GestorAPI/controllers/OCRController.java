package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.exceptions.ErrorPharseJsonException;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.modelsDTO.ticket.TicketDto;
import Proyecto.GestorAPI.config.security.CustomUserDetails;
import Proyecto.GestorAPI.services.OCRService;
import Proyecto.GestorAPI.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RestController
@RequestMapping("/api/ocr")
@Tag(name = "OCR Service", description = "Procesamiento de tickets mediante OCR")
public class OCRController {

    @Autowired
    private OCRService ocrService;

    @Autowired
    private UserService userService;

    /**
     * Endpoint para procesar una imagen de ticket mediante OCR.
     * Recibe un archivo de imagen en formato multipart/form-data,
     * valida que el archivo no esté vacío, procesa la imagen para extraer
     * los datos del ticket y devuelve la información estructurada.
     *
     * Requiere autenticación con token Bearer.
     *
     * @param file Archivo de imagen del ticket a procesar.
     * @param currentUser Usuario autenticado que realiza la solicitud.
     * @return Datos estructurados del ticket o mensaje de error.
     */
    @PostMapping(value = "/ticket", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Procesar imagen de ticket con OCR",
            description = "Recibe una imagen de ticket, la procesa con OCR y devuelve los datos estructurados.",
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
    )
    public ResponseEntity<?> processTicketImage(
            @RequestParam("archivo") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo no puede estar vacío");
        }

        try {
            return ResponseEntity.ok(TicketDto.from(ocrService.processImageTicket(file, user)));
        } catch (Exception | ErrorPharseJsonException e) {
            return ResponseEntity.internalServerError().body("Error en el procesamiento OCR: " + e.getMessage());
        }
    }

    /**
     * Endpoint para procesar un ticket digital (archivo) mediante OCR.
     * Recibe un archivo digital (PDF, imagen, etc.) en multipart/form-data,
     * valida que el archivo no esté vacío, procesa el archivo para extraer
     * los datos del ticket y devuelve la información estructurada.
     *
     * Requiere autenticación con token Bearer.
     *
     * @param file Archivo digital del ticket a procesar.
     * @param currentUser Usuario autenticado que realiza la solicitud.
     * @return Datos estructurados del ticket o mensaje de error.
     */
    @PostMapping(value = "/ticketdigital", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Procesar ticket digital con OCR",
            description = "Recibe un archivo digital de ticket, lo procesa con OCR y devuelve los datos estructurados.",
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
    )
    public ResponseEntity<?> processTicketDigital(
            @RequestParam("archivo") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo no puede estar vacío");
        }

        try {
            return ResponseEntity.ok(TicketDto.from(ocrService.proccessDigitalTicket(file, user)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error en el procesamiento OCR: " + e.getMessage());
        }
    }
}
