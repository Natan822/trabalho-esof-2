package auth_service.controller;

import auth_service.dto.LoginDTO;
import auth_service.dto.RegisterDTO;
import auth_service.model.UserCredentials;
import auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public Mono<ResponseEntity<?>> register(@ModelAttribute RegisterDTO registerDTO) {
        return authService.register(registerDTO);
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@ModelAttribute LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }
}
