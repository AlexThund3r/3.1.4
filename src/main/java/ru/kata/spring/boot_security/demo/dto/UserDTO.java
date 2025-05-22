package ru.kata.spring.boot_security.demo.dto;

import java.util.List;

public class UserDTO {
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String password;
    private List<Long> rolesSelected;
    private Long id;

    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<Long> getRolesSelected() { return rolesSelected; }
    public void setRolesSelected(List<Long> rolesSelected) { this.rolesSelected = rolesSelected; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}