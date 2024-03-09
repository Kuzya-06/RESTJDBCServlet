package ru.kuznetsov.service;

import ru.kuznetsov.dto.ProjectIncomingDto;
import ru.kuznetsov.dto.ProjectOutGoingDto;
import ru.kuznetsov.dto.ProjectUpdateDto;
import ru.kuznetsov.exception.NotFoundException;

import java.util.List;

public interface ProjectService {
    ProjectOutGoingDto save(ProjectIncomingDto project);

    void update(ProjectUpdateDto project) throws NotFoundException;

    ProjectOutGoingDto findById(Integer projectId) throws NotFoundException;

    List<ProjectOutGoingDto> findAll();

    void delete(Integer projectId) throws NotFoundException;

    void deleteEmployeeFromProject(Integer projectId, Integer emplId) throws NotFoundException;

    void addEmployeeToProject(Integer departmentId, Integer userId) throws NotFoundException;
}
