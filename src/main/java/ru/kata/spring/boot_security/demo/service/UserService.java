package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends org.springframework.security.core.userdetails.UserDetailsService {
    List<User> getAllUsers();
    void saveUser(User user);
    User getUserById(Long id);
    void updateUser(User user);
    void deleteUser(Long id);
    User findByEmail(String email);
    boolean existsByEmail(String email);  // Новый метод для проверки существования email
    User getCurrentUser();
}
