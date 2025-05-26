package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;  // Используем UserService вместо AdminService

@RestController
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;  // Внедряем UserService

    public UserRestController(UserService userService) {
        this.userService = userService;  // Инициализируем UserService
    }

    // Перенаправление на страницу user/index.html
    @GetMapping("")
    public ResponseEntity<Void> redirectToUserPage() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/user/index.html");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);  // HTTP 302 редирект
    }

    // Получение данных текущего пользователя
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        User user = userService.getCurrentUser();  // Используем UserService для получения текущего пользователя
        if (user != null) {
            return ResponseEntity.ok(user);  // Возвращаем данные текущего пользователя
        } else {
            return ResponseEntity.status(401).build();  // Если пользователь не найден, возвращаем 401 (Unauthorized)
        }
    }
}
