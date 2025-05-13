package Proyecto.GestorAPI.servicesimpl;

import Proyecto.GestorAPI.models.Ticket;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.models.enums.ExpenseClass;
import Proyecto.GestorAPI.modelsDTO.ticket.CreateTicketRequest;
import Proyecto.GestorAPI.modelsDTO.ticket.TicketResponse;
import Proyecto.GestorAPI.services.OCRService;
import Proyecto.GestorAPI.services.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Service
public class OCRServiceImpl implements OCRService {

    @Value("${python.server.url}")
    private String pythonServerUrl;

    @Value("${python.server.apiKey}")
    private String pythonServerApiKey;

    @Autowired
    private TicketService ticketService;

    private final RestTemplate restTemplate;

    public OCRServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public CreateTicketRequest processImageTicket(MultipartFile file, User user) throws IOException {
        // 1. Guardar archivo temporalmente
        Path tempFilePath = Files.createTempFile("ticket_", "_" + file.getOriginalFilename());
        Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

        // 2. Procesar el archivo con OCR
        String ocrResult = sendFileForOCR(tempFilePath.toFile());
        System.out.println("OCR Result 1: " + ocrResult);
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("OK");
        TicketResponse ticketOCR = mapper.readValue(ocrResult, TicketResponse.class);
        System.out.println("OCR Result 2: " + ticketOCR);

        // 3. Crear y guardar el ticket
        CreateTicketRequest ticket = new CreateTicketRequest();
        ticket.setUserId(user.getId());
        ticket.setIcon("");
        ticket.setCategoriaId(0L);
        ticket.setStore(ticketOCR.getEstablecimiento());
        ticket.setProductsJSON(ticketOCR.getArticulos());
        ticket.setTotal(ticketOCR.getTotal());
        ticket.setName("");
        ticket.setDescription("");
        ticket.setFechaCompra(LocalDateTime.now());

        // Guardar el ticket en la base de datos y devolverlo
        return ticket;
    }

    @Override
    public CreateTicketRequest proccessDigitalTicket(MultipartFile file, User user) throws IOException {
        // Guardar archivo temporalmente
        Path tempFilePath = Files.createTempFile("TicketDigital" + "_", "_" + file.getOriginalFilename());
        Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

        // Procesar con OCR
        String ocrResult = sendFileForOCR(tempFilePath.toFile());

        // Crear y guardar ticket
        CreateTicketRequest ticket = new CreateTicketRequest();
        ticket.setProductsJSON(ocrResult);
        return ticket;
    }

    @Override
    public String sendFileForOCR(File file) throws IOException {
        // Crear multipart
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(file));

        // Crear headers con API Key
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Bearer " + pythonServerApiKey); // Usar API Key como Bearer token

        // Armar entidad HTTP
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Enviar solicitud al servidor Python
        String url = pythonServerUrl + "/ocr";
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            System.out.println("Status: " + response.getStatusCode() + " Body: " + response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.out.println("Error Status: " + ex.getStatusCode() + " Error Body: " + ex.getResponseBodyAsString());
            throw new IOException("Error al procesar OCR. Código: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
        }
    }
}
