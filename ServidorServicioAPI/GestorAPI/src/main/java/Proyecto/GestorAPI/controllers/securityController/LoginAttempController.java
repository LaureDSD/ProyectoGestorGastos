package Proyecto.GestorAPI.controllers.securityController;

import Proyecto.GestorAPI.models.LoginAttempt;
import Proyecto.GestorAPI.servicesimpl.LoginAttemptServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static Proyecto.GestorAPI.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/loginAttemps")
@Tag(name = "Login Log (Admin only)", description = "Lista de intentos de acceso")
public class LoginAttempController {

    @Autowired
    private LoginAttemptServiceImpl loginAttemptService;

    @GetMapping("/")
    @Operation(
            security = @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME),
            summary = ""
    )
    public ResponseEntity<List<LoginAttempt>> getLogins(
            @RequestParam(value = "loginId", required = false) Long logId) {
        List<LoginAttempt> loginAttempts ;
        //if(logId != null){
            //loginAttempts = loginAttemptService.getByUserId(logId);
        //}else{
            loginAttempts = loginAttemptService.getAll();
        //}

        if(loginAttempts.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(loginAttempts);
    }

}
