package ru.kuznetsov.dto;

import ru.kuznetsov.entity.Role;

public class EmployeeOutIdNameRoleDto {
    private Integer id;
    private String name;
    private RoleIncomingDto role;


    public EmployeeOutIdNameRoleDto() {
    }

    public EmployeeOutIdNameRoleDto(Integer id, String name, RoleIncomingDto role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

   public RoleIncomingDto getRole() {
        return role;
    }


}
