package ru.kuznetsov.dto;

import java.util.List;

public class ProjectOutGoingDto {
    private Integer id;
    private String name;
    private List<EmployeeSmallOutGoingDto> employeeList;

    public ProjectOutGoingDto() {
    }

    public ProjectOutGoingDto(Integer id, String name, List<EmployeeSmallOutGoingDto> employeeList) {
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

    public List<EmployeeSmallOutGoingDto> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<EmployeeSmallOutGoingDto> employeeList) {
        this.employeeList = employeeList;
    }
}
