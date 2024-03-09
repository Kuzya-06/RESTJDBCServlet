package ru.kuznetsov.dto;

public class EmployeeSmallOutGoingDto {
    private Integer id;
    private String name;

    public EmployeeSmallOutGoingDto() {
    }

    public EmployeeSmallOutGoingDto(Integer id, String name) {
        this.id = id;
        this.name = name;

    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
