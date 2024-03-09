package ru.kuznetsov.entity;

import ru.kuznetsov.dao.EmployeeToProjectDAO;
import ru.kuznetsov.dao.inter.EmployeeToProjectRepository;

import java.util.List;

/**
 * Employee entity
 * <p>
 * Relation:
 * Many To One: Employee -> Department
 * Many To Many: Employee <-> Project
 */
public class Employee {
    private static final EmployeeToProjectRepository employeeToProjectRepository
            = EmployeeToProjectDAO.getInstance();
    private Integer id;
    private String name;
    private Role role;
    private List<Project> projectList;

    public Employee() {
    }

    public Employee(Integer id, String name, Role role, List<Project> projectList) {
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Project> getProjectList() {
        if(projectList == null){
            projectList = employeeToProjectRepository.findProjectsByEmployeeId(this.id);
        }
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }


}
