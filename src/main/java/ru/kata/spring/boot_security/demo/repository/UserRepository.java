package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // Найти пользователя по email
    User findByEmail(String email);

    // Проверить, существует ли пользователь с таким email
    boolean existsByEmail(String email);
}
