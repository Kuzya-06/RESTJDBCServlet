package ru.kuznetsov.dto;

public class ProjectIncomingDto {
    private String name;

    public ProjectIncomingDto() {
    }

    public ProjectIncomingDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
