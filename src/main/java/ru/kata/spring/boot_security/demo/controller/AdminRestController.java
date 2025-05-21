package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        if (roles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Если ролей нет
        }
        return new ResponseEntity<>(roles, HttpStatus.OK);  // Отправляем список ролей
    }

    // Получить всех пользователей
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        System.out.println("Users fetched: " + users);  // Логируем полученные пользователи
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    // Создать нового пользователя
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user, @RequestParam(value = "rolesSelected", required = false) List<Long> rolesSelected) {
        // Устанавливаем роли
        if (rolesSelected != null && !rolesSelected.isEmpty()) {
            Set<Role> roles = new HashSet<>(roleService.getRolesByIds(rolesSelected));
            user.setRoles(roles);
        } else {
            user.setRoles(new HashSet<>());
        }

        // Проверка на существующего пользователя с таким email
        if (userService.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // Если email уже существует
        }

        userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // Получить пользователя по ID для редактирования
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Обновить пользователя
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user, @RequestParam(value = "rolesSelected", required = false) List<Long> rolesSelected) {
        User existingUser = userService.getUserById(id);
        if (existingUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Обновляем только те поля, которые были изменены
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setAge(user.getAge());
        existingUser.setEmail(user.getEmail());

        // Обновляем пароль только если он был указан
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(user.getPassword());
        }

        // Обновляем роли только если они были указаны
        if (rolesSelected != null && !rolesSelected.isEmpty()) {
            Set<Role> roles = new HashSet<>(roleService.getRolesByIds(rolesSelected));
            existingUser.setRoles(roles);
        }

        userService.updateUser(existingUser);
        return new ResponseEntity<>(existingUser, HttpStatus.OK);
    }

    // Удалить пользователя
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
