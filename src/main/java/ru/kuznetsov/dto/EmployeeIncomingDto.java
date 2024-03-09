package ru.kuznetsov.dto;

import ru.kuznetsov.entity.Role;

public class EmployeeIncomingDto {
    private String name;
    private Role role;

    public EmployeeIncomingDto() {
    }

    public EmployeeIncomingDto(String name,  Role role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

}

