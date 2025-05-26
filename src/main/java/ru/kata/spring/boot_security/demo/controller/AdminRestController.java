package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.AdminService;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")  // Путь для контроллера, используем /admin
public class AdminRestController {

    private final AdminService adminService;
    private final RoleService roleService;

    @Autowired
    public AdminRestController(AdminService adminService, RoleService roleService) {
        this.adminService = adminService;
        this.roleService = roleService;
    }

    // Обработчик для запроса на /admin
    @GetMapping("")
    public ResponseEntity<Void> redirectToAdminPage() {
        // Перенаправление на /admin/index.html
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/admin/index.html");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> showAdminPanel() {
        Map<String, Object> response = new HashMap<>();
        response.put("users", adminService.getAllUsers());
        response.put("currentUser", adminService.getCurrentUser());
        response.put("allRoles", roleService.getAllRoles());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserDTO dto) {
        try {
            User createdUser = adminService.createUser(dto);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка создания пользователя");
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
        if (!id.equals(dto.getId())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            // Проверяем, существует ли пользователь с таким ID
            User updatedUser = adminService.updateUser(dto);  // Обновляем пользователя
            return ResponseEntity.ok(updatedUser); // Возвращаем обновленного пользователя
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();  // Если пользователь не найден
        } catch (Exception e) {
            return ResponseEntity.status(500).build();  // Обработка других ошибок
        }
    }


    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = adminService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(adminService.getAllRoles());
    }
    @GetMapping("/currentUser")
    public ResponseEntity<User> getCurrentUser() {
        User currentUser = adminService.getCurrentUser(); // Получаем текущего пользователя из сервиса
        if (currentUser != null) {
            return ResponseEntity.ok(currentUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Если нет авторизованного пользователя
        }
    }

}
