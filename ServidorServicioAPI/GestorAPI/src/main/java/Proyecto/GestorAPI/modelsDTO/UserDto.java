package Proyecto.GestorAPI.modelsDTO;


import Proyecto.GestorAPI.models.User;

public record UserDto(Long id, String username, String name, String email, String role) {

    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}