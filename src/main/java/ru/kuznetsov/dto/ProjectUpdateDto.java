package ru.kuznetsov.dto;

public class ProjectUpdateDto {
    private Integer id;
    private String name;

    public ProjectUpdateDto() {
    }

    public ProjectUpdateDto(Integer id, String name) {
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
