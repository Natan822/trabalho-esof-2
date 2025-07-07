package auth_service.dto;

public record RegisterDTO(
        String email,
        String firstName,
        String lastName,
        String password
) {}
