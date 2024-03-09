package ru.kuznetsov.dto;

import java.util.List;

public class EmployeeOutGoingDto {
    private Integer id;
    private String name;
    private RoleOutGoingDto role;
    private List<ProjectOutGoingDto> projectList;

    public EmployeeOutGoingDto() {
    }

    public EmployeeOutGoingDto(Integer id, String name, RoleOutGoingDto role, List<ProjectOutGoingDto> projectList) {
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

   public RoleOutGoingDto getRole() {
        return role;
    }

    public List<ProjectOutGoingDto> getProjectList() {
        return projectList;
    }

}
