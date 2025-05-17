package Proyecto.GestorAPI.controllers;

import Proyecto.GestorAPI.models.Spent;
import Proyecto.GestorAPI.models.User;
import Proyecto.GestorAPI.models.enums.ExpenseClass;
import Proyecto.GestorAPI.modelsDTO.spent.CreateSpentRequest;
import Proyecto.GestorAPI.modelsDTO.spent.SpentDto;
import Proyecto.GestorAPI.modelsDTO.spent.UpdateSpentRequest;
import Proyecto.GestorAPI.security.CustomUserDetails;
import Proyecto.GestorAPI.security.RoleServer;
import Proyecto.GestorAPI.services.CategoryExpenseService;
import Proyecto.GestorAPI.services.SpentService;
import Proyecto.GestorAPI.services.UserService;
import Proyecto.GestorAPI.servicesimpl.StorageServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/gastos")
@Tag(name = "Spent Management (Verify control) ", description = "Operaciones sobre los gastos registrados")
public class SpentController {

    @Autowired
    private  SpentService spentService;

    @Autowired
    private  UserService userService;

    @Autowired
    private  CategoryExpenseService categoriaService;

    @Autowired
    private StorageServiceImpl storageService;
    private static final String STORAGE_BASE_PATH = "gastos/";

    @GetMapping("")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Obtener todos los gastos filtrados por clienteId (opcional para admins)"
    )
    public ResponseEntity<List<SpentDto>> getSpents(
            @RequestParam(value = "clienteId", required = false) Long clienteId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        List<Spent> spents = new ArrayList<>();

        if(user.getRole() != RoleServer.ADMIN){
            //si no es admin
           spents = spentService.getSpentsByUserId(user.getId());
        }else{
            //si es admin
            spents = (clienteId != null)
                    ? spentService.getSpentsByUserId(clienteId)
                    : spentService.getAll();
        }
        //Verificacion de existencia
        if (spents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        //Devolucion
        return ResponseEntity.ok(spents.stream()
                .map(SpentDto::from)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{spentId}")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Obtener un gasto por ID"
    )
    public ResponseEntity<SpentDto> getSpentById(
            @PathVariable Long spentId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Spent spent = spentService.getByID(spentId).orElse(null);

        // Verificación de existencia
        if (spent == null) {
            return ResponseEntity.notFound().build();
        }

        // Verificación de propiedad (solo admin o dueño puede ver)
        if (user.getRole() != RoleServer.ADMIN && !spent.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(SpentDto.from(spent));
    }


    @PostMapping("")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = "Crear un nuevo gasto"
    )
    public ResponseEntity<SpentDto> createSpent(
            @RequestParam(value = "clienteId", required = false) Long clienteId,
            @Valid @RequestBody CreateSpentRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Spent spent;

        if(user.getRole() != RoleServer.ADMIN){
            //si no es admin
            spent = mappingSpent(request, user.getId());
        }else{
            //si es admin
            spent = mappingSpent(request,(clienteId != null) ? clienteId : user.getId());
        }
        //Creacion
        Spent createdSpent = spentService.setItem(spent);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SpentDto.from(createdSpent));
    }

    @DeleteMapping("/{spentId}")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Eliminar un gasto por ID"
    )
    public ResponseEntity<Void> deleteSpent(
            @PathVariable Long spentId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Spent spent = spentService.getByID(spentId).orElse(null);
        //Veriificacion
        if (spent == null) {
            return ResponseEntity.notFound().build();
        }
        //Verificacion de propiedad
        if(user.getRole() != RoleServer.ADMIN && !spent.getUser().getId().equals(user.getId())){
            return ResponseEntity.badRequest().build();
        }
        //Eliminacion
        spentService.deleteByID(spentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{spentId}")
    @Operation(
            security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)},
            summary = "Actualizar un gasto por ID"
    )
    public ResponseEntity<SpentDto> updateSpent(
            @PathVariable Long spentId,
            @Valid @RequestBody UpdateSpentRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Spent spent = spentService.getByID(spentId).orElse(null);;

        System.out.println("dhvkdsfkdsbfkdfdsahdsh " + request.spentId());
        //Veriificacion existencia
        if (spent == null) {
            return ResponseEntity.notFound().build();
        }

        //verificacion de propiedad
        if(user.getRole() != RoleServer.ADMIN && !request.userId().equals(user.getId())){
            return ResponseEntity.badRequest().build();
        }

        //Actualizacion
        Spent updatedSpent = spentService.setItem(mappingUpdateSpent(request, spent.getUser().getId()));
        return ResponseEntity.ok(SpentDto.from(updatedSpent));
    }

    private Spent mappingUpdateSpent (UpdateSpentRequest request , Long clienteId){
        // Crear el gasto con referencias por ID
        Spent spent = new Spent();
        spent.setSpentId(request.spentId());
        spent.setUser(userService.getUserById(clienteId).orElse(new User()));
        spent.setCategory(categoriaService.getByID(request.categoriaId()).orElse(null));
        spent.setExpenseDate(request.fechaCompra());
        spent.setTypeExpense(ExpenseClass.valueOf(request.typeExpense()));
        spent.setTotal(request.total());
        spent.setIva(request.iva());
        spent.setName(request.name());
        spent.setDescription(request.description());
        spent.setIcon(request.icon());
        spent.setCreatedAt(LocalDateTime.now());
        return  spent;
    }

    private Spent mappingSpent (CreateSpentRequest request , Long clienteId){
        // Crear el gasto con referencias por ID
        Spent spent = new Spent();
        spent.setUser(userService.getUserById(clienteId).orElse(new User()));
        spent.setCategory(categoriaService.getByID(request.categoriaId()).orElse(null));
        spent.setExpenseDate(request.fechaCompra());
        spent.setTypeExpense(ExpenseClass.valueOf(request.typeExpense()));
        spent.setTotal(request.total());
        spent.setIva(request.iva());
        spent.setName(request.name());
        spent.setDescription(request.description());
        spent.setIcon(request.icon());
        spent.setCreatedAt(LocalDateTime.now());
        return  spent;
    }

    @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
    @PutMapping(value = "/me/uploadSpenseImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadSpenseImageWithData(
            @RequestPart("image") MultipartFile file,
            @RequestParam("spentId") Long spentId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("No se envió ninguna imagen.");
        }

        try {
            // Aquí podrías validar o procesar el ticket
            System.out.println("Ticket recibido: ");

            // Procesar el usuario
            User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
            Spent spent = spentService.getByID(Long.valueOf(spentId)).orElse(null);
            String oldUrl = spent.getIcon();
            String newUrl = storageService.saveImageData(STORAGE_BASE_PATH, file);

            if (oldUrl != null && !oldUrl.isEmpty()) {
                storageService.deleteImageData(oldUrl);
            }

            spent.setIcon( newUrl );

            spentService.setItem(spent);



            return ResponseEntity.ok(Map.of("url", newUrl));

        } catch (IOException e) {return ResponseEntity.internalServerError().body("Error al guardar la imagen.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error inesperado: " + e.getMessage());
        }
    }

}