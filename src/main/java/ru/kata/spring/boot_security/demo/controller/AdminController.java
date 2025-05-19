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

    // Показать всех пользователей + форму для создания нового
    @GetMapping({"", "/"})
    public String showAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/admin";
    }


    // Создать нового пользователя
    @PostMapping("/new-user")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "rolesSelected", required = false) List<Long> rolesSelected) {
        if (rolesSelected != null && !rolesSelected.isEmpty()) {
            Set<Role> roles = new HashSet<>(roleService.getRolesByIds(rolesSelected));
            user.setRoles(roles);
        } else {
            user.setRoles(new HashSet<>()); // если роли не выбраны - пустой набор
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    // Форма редактирования пользователя
    @GetMapping("/update/{id}")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/admin"; // если пользователь не найден - редирект на список
        }
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin/update-user";
    }

    // Обновить пользователя
    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "rolesSelected", required = false) List<Long> rolesSelected) {
        if (rolesSelected != null && !rolesSelected.isEmpty()) {
            Set<Role> roles = new HashSet<>(roleService.getRolesByIds(rolesSelected));
            user.setRoles(roles);
        } else {
            user.setRoles(new HashSet<>());
        }
        userService.updateUser(user);
        return "redirect:/admin";
    }

    // Удалить пользователя
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
