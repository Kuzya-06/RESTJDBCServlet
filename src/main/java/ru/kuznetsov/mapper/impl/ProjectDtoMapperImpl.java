package ru.kuznetsov.mapper.impl;


import lombok.extern.slf4j.Slf4j;
import ru.kuznetsov.dto.*;
import ru.kuznetsov.entity.Project;
import ru.kuznetsov.entity.Project1;
import ru.kuznetsov.mapper.ProjectDtoMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProjectDtoMapperImpl implements ProjectDtoMapper {
    private static ProjectDtoMapper instance;

    private ProjectDtoMapperImpl() {
    }

    public static synchronized ProjectDtoMapper getInstance() {
        if (instance == null) {
            instance = new ProjectDtoMapperImpl();
        }
        return instance;
    }

    @Override
    public Project map(ProjectIncomingDto dto) {
        return new Project(
                null,
                dto.getName(),
                null
        );
    }

    @Override
    public ProjectOutGoingDto map(Project project) {
        log.info("ProjectOutGoingDto map(Project project) = "+project);
        List<EmployeeSmallOutGoingDto> empList = project.getEmployeeList()
                .stream().map(user -> new EmployeeSmallOutGoingDto(
                        user.getId(),
                        user.getName()
                )).toList();
log.info("List<EmployeeSmallOutGoingDto> empList = "+empList.toString());
        return new ProjectOutGoingDto(
                project.getId(),
                project.getName(),
                empList
        );
    }

    @Override
    public Project map(ProjectUpdateDto updateDto) {
        return new Project(
                updateDto.getId(),
                updateDto.getName(),
                null
        );
    }

    @Override
    public List<ProjectOutGoingDto> map(List<Project> projectList) {
        return projectList.stream().map(this::map).toList();
    }

    @Override
    public List<Project> mapUpdateList(List<ProjectUpdateDto> projectList) {
        return projectList.stream().map(this::map).toList();
    }


    public ProjectOutIdNameEmployeeDto map(Project1 project1) {
        log.info("ProjectOutIdNameEmployeeDto map(Project1 project) = "+project1);
        List<EmployeeOutIdNameRoleDto> empList = project1.getEmployeeList()
                .stream().map(user -> new EmployeeOutIdNameRoleDto(
                        user.getId(),
                        user.getName(),
                        user.getRole()
                )).toList();
        log.info("List<EmployeeOutIdNameRoleDto> empList = "+empList.toString());
        return new ProjectOutIdNameEmployeeDto(
                project1.getId(),
                project1.getName(),
                empList
        );
    }

    @Override
    public List<ProjectOutIdNameEmployeeDto> map(ArrayList<Project1> project1List) {

      return   project1List.stream().map(this::map).toList();

    }

}
