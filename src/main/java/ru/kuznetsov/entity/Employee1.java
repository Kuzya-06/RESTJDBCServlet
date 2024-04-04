package ru.kuznetsov.entity;

import ru.kuznetsov.dao.EmployeeToProjectDAO;
import ru.kuznetsov.dao.inter.EmployeeToProjectRepository;
import ru.kuznetsov.dto.RoleIncomingDto;

import java.util.List;

/**
 * Employee entity
 * <p>
 * Relation:
 * Many To One: Employee -> Department
 * Many To Many: Employee <-> Project
 */
public class Employee1 {
//    private static final EmployeeToProjectRepository employeeToProjectRepository
//            = EmployeeToProjectDAO.getInstance();
    private Integer id;
    private String name;
    private RoleIncomingDto role;
    private List<Project1> projectList;

    public Employee1() {
    }

    public Employee1(Integer id, String name, RoleIncomingDto role, List<Project1> projectList) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.projectList = projectList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoleIncomingDto getRole() {
        return role;
    }

    public void setRole(RoleIncomingDto role) {
        this.role = role;
    }

    public List<Project1> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project1> projectList) {
        this.projectList = projectList;
    }
}
