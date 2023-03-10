package space.nixus.pubtrans.controller;

import space.nixus.pubtrans.model.User;
import space.nixus.pubtrans.model.UserParams;
import space.nixus.pubtrans.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * User admin controller.
 * 
 * See /swagger
 */
@RestController
@SecurityRequirement(name = "bearer")
public final class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users/roles/list")
    List<String> listRoles() {
        return userService.getRoles();
    }

    @GetMapping("/users/list")
    List<User> listUsers() {
        return userService.getUsers();
    }

    @PostMapping("/users/create")
    User creatUser(@RequestBody UserParams params) {
        return userService.create(params);
    }

    @PutMapping("/users/update/{id}")
    User updateUser(@PathVariable("id") long id, @RequestBody UserParams params) {
        return userService.update(id, params);
    }

    @DeleteMapping("/users/delete/{id}")
    String deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
        return "OK";
    }
}
