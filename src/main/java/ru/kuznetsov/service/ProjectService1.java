package ru.kuznetsov.service;

import ru.kuznetsov.dto.ProjectIncomingDto;
import ru.kuznetsov.dto.ProjectOutGoingDto;
import ru.kuznetsov.dto.ProjectOutIdNameEmployeeDto;
import ru.kuznetsov.dto.ProjectUpdateDto;
import ru.kuznetsov.entity.Project1;
import ru.kuznetsov.exception.NotFoundException;

import java.util.List;

public interface ProjectService1 {


    ProjectOutIdNameEmployeeDto findById(Integer projectId) throws NotFoundException;

    List<ProjectOutIdNameEmployeeDto> findAll();
}
