package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;

public interface AdminService {

    List<User> getAllUsers();
    User getUserById(Long id);
    void deleteUser(Long id);
    User getCurrentUser();
    List<Role> getAllRoles();

    // --- REST ---
    User createUser(UserDTO dto);
    User updateUser(UserDTO dto);
}