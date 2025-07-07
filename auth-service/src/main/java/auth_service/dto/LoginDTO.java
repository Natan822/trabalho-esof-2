package auth_service.dto;

public record LoginDTO(
        String email,
        String password
) {}
