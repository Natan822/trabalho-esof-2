package user_service.controller;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user_service.dto.UserDTO;
import user_service.model.User;
import user_service.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public List<User> users() {
        return userService.getUsers();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@ModelAttribute UserDTO userDTO) {
        User user = userService.addUser(userDTO);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "E-mail já cadastrado."));
        }

        return ResponseEntity.ok(Map.of(
                "success", "Usuário cadastrado.",
                "user", user));
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUserById(@PathVariable(name = "user_id")Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuário não encontrado."));
        }
        return ResponseEntity.ok(
                Map.of("success", "Usuário encontrado.",
                        "user", user));
    }

    @GetMapping("/search")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuário não encontrado."));
        }
        return ResponseEntity.ok(
                Map.of("success", "Usuário encontrado.",
                        "user", user));
    }

}
