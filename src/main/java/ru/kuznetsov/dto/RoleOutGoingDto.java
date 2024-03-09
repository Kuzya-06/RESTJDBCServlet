package ru.kuznetsov.dto;

public class RoleOutGoingDto {
    private Integer id;
    private String name;

    public RoleOutGoingDto() {
    }

    public RoleOutGoingDto(Integer id, String name) {
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
