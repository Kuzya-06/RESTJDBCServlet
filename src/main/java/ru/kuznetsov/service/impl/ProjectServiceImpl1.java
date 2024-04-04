package ru.kuznetsov.service.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kuznetsov.dao.*;
import ru.kuznetsov.dao.inter.*;
import ru.kuznetsov.dto.ProjectIncomingDto;
import ru.kuznetsov.dto.ProjectOutGoingDto;
import ru.kuznetsov.dto.ProjectOutIdNameEmployeeDto;
import ru.kuznetsov.dto.ProjectUpdateDto;
import ru.kuznetsov.entity.EmployeeToProject;
import ru.kuznetsov.entity.Project;
import ru.kuznetsov.entity.Project1;
import ru.kuznetsov.exception.NotFoundException;
import ru.kuznetsov.mapper.ProjectDtoMapper;
import ru.kuznetsov.mapper.impl.ProjectDtoMapperImpl;
import ru.kuznetsov.service.ProjectService;
import ru.kuznetsov.service.ProjectService1;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProjectServiceImpl1 implements ProjectService1{
    private final ProjectRepository1 pRepository = ProjectDAO1.getInstance();
    private static final ProjectDtoMapper projectDtoMapper = ProjectDtoMapperImpl.getInstance();
    private static ProjectService1 instance;

    private ProjectServiceImpl1() {
    }

    public static synchronized ProjectService1 getInstance() {
        if (instance == null) {
            instance = new ProjectServiceImpl1();
        }
        return instance;
    }

    @Override
    public ProjectOutIdNameEmployeeDto findById(Integer projectId) throws NotFoundException {
        log.info("begin findById id = "+projectId);

        Project1 project1 = pRepository.findById(projectId).orElseThrow(() ->
                new NotFoundException("Project not found."));

        ProjectOutIdNameEmployeeDto map = projectDtoMapper.map(project1);
        return map;
    }

    @Override
    public List<ProjectOutIdNameEmployeeDto> findAll() {
        log.info(" Begin findAll()");
        List<Project1> all = pRepository.findAll();
        log.info(" proList "+all);
        List<ProjectOutIdNameEmployeeDto> map = projectDtoMapper.map((ArrayList<Project1>) all);
        log.info(" List<ProjectOutGoingDto> map "+map);
        return map;
    }

}
