package Proyecto.GestorAPI.modelsDTO.user;

import Proyecto.GestorAPI.models.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserMeOutDto {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String imageUrl;
    private String server;
    private String role;
    private boolean active;
    private boolean fv2;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserMeOutDto from(User user) {
        return UserMeOutDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .imageUrl(user.getImageUrl())
                .server(user.getServer())
                .role(user.getRole().name())
                .active(user.isActive())
                .fv2(user.isFv2())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}