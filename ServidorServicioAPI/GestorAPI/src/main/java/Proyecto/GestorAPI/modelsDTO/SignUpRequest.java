package Proyecto.GestorAPI.modelsDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(
        @Schema(example = "user1") @NotBlank String username,
        @Schema(example = "user1") @NotBlank String password,
        @Schema(example = "User1") @NotBlank String name,
        @Schema(example = "user1@gesthor.com") @Email String email) {
}
