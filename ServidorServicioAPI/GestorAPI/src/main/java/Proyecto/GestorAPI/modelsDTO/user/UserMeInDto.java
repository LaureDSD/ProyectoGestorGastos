package Proyecto.GestorAPI.modelsDTO.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserMeInDto {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String username;

    @Email
    private String email;

    private String phone;
    private String address;
    private String imageUrl;
    private String server;
    private boolean fv2;
}
