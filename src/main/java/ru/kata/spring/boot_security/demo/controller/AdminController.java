package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping({"", "/"})
    public String showAdminPanel(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        User admin = userService.findByEmail("admin@mail.ru");
        model.addAttribute("currentUser", admin);
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/admin";
    }

    @PostMapping("/new-user")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "rolesSelected", required = false) List<Long> rolesSelected) {
        if (rolesSelected != null && !rolesSelected.isEmpty()) {
            Set<Role> roles = new HashSet<>(roleService.getRolesByIds(rolesSelected));
            user.setRoles(roles);
        } else {
            user.setRoles(new HashSet<>());
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/admin";
        }
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/update-user";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "rolesSelected", required = false) List<Long> rolesSelected) {
        // Получаем текущего пользователя из БД
        User existingUser = userService.getUserById(user.getId());

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
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            userService.deleteUser(id);
        }
        return "redirect:/admin";
    }
}
