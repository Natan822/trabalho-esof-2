package user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import user_service.model.User;
import user_service.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public List<User> users() {
        return userService.getUsers();
    }

    @GetMapping("/{user_id}")
    public User getUser(@PathVariable(name = "user_id")Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.orElse(null);
    }

}
