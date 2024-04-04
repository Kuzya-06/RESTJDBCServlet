package ru.kuznetsov.entity;

import lombok.extern.slf4j.Slf4j;
import ru.kuznetsov.dao.EmployeeToProjectDAO;
import ru.kuznetsov.dao.inter.EmployeeToProjectRepository;

import java.util.List;

/**
 * Project entity
 * <p>
 * Relation:
 * Many To Many: Project <-> Employee
 */
@Slf4j
public class Project1 {
//    private static final EmployeeToProjectRepository empToProjRepo = EmployeeToProjectDAO.getInstance();
    private Integer id;
    private String name;
    private List<Employee1> employeeList;

    public Project1() {
    }

    public Project1(Integer id, String name, List<Employee1> employeeList) {
        this.id = id;
        this.name = name;
        this.employeeList = employeeList;
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

    public List<Employee1> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee1> employeeList) {
        this.employeeList = employeeList;
    }

    @Override
    public String toString() {
        return "Project1{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", employeeList=" + employeeList +
                '}';
    }
}
