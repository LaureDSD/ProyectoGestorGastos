package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.models.Ticket;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.security.CustomUserDetails;
import Proyecto.GestorAPI.services.OCRService;
import Proyecto.GestorAPI.services.TicketService;
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
    public ResponseEntity<?> processTicketImage(
            @RequestParam("archivo") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        // Validacion de archivo
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo no puede estar vacío");
        }
        //Procesar
        try {
            return ResponseEntity.ok(ocrService.processImageTicket(file,user));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error en el procesamiento OCR: " + e.getMessage());
        }
    }


    @PostMapping(value = "/ticketdigital", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Procesar ticket digital con OCR",
            description = "Recibe un archivo digital de ticket, lo procesa con OCR y devuelve los datos estructurados",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
    )
    public ResponseEntity<?> processTicketDigital(
            @RequestParam("archivo") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        //Validacion inicial
        if (file.isEmpty()) { return ResponseEntity.badRequest().body("El archivo no puede estar vacío");}
        //Procesar
        try {
            // Lógica para procesar ticket digital
            return ResponseEntity.ok(ocrService.proccessDigitalTicket(file,user));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error en el procesamiento: " + e.getMessage());
        }
    }
}