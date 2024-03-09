package ru.kuznetsov.dto;

import java.util.List;

public class EmployeeUpdateDto {
    private Integer id;
    private String name;
    private RoleUpdateDto role;
    private List<ProjectUpdateDto> projectList;

    public EmployeeUpdateDto() {
    }

    public EmployeeUpdateDto(Integer id, String name, RoleUpdateDto role, List<ProjectUpdateDto> projectList) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.projectList = projectList;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RoleUpdateDto getRole() {
        return role;
    }

    public List<ProjectUpdateDto> getProjectList() {
        return projectList;
    }

  }

