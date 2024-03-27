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
public class Project {
    private static final EmployeeToProjectRepository empToProjRepo = EmployeeToProjectDAO.getInstance();
    private Integer id;
    private String name;
    private List<Employee> employeeList;

    public Project() {
    }

    public Project(Integer id, String name, List<Employee> employeeList) {
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

    public List<Employee> getEmployeeList() {
        log.info("begin getEmployeeList()");
        if (employeeList == null) {
            employeeList = empToProjRepo.findEmployeesByProjectId(this.id);
        }
        log.info("employeeList = "+employeeList.toString());
        log.info("end getEmployeeList()");
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", employeeList=" + employeeList +
                '}';
    }
}
