package ru.kuznetsov.mapper;

import ru.kuznetsov.dto.ProjectIncomingDto;
import ru.kuznetsov.dto.ProjectOutGoingDto;
import ru.kuznetsov.dto.ProjectOutIdNameEmployeeDto;
import ru.kuznetsov.dto.ProjectUpdateDto;
import ru.kuznetsov.entity.Project;
import ru.kuznetsov.entity.Project1;

import java.util.ArrayList;
import java.util.List;

public interface ProjectDtoMapper {

    Project map(ProjectIncomingDto projectIncomingDto);

    ProjectOutGoingDto map(Project project);

    Project map(ProjectUpdateDto projectUpdateDto);

    List<ProjectOutGoingDto> map(List<Project> projectList);

    List<Project> mapUpdateList(List<ProjectUpdateDto> projectList);

    ProjectOutIdNameEmployeeDto map(Project1 project1);

    List<ProjectOutIdNameEmployeeDto> map(ArrayList<Project1> project1List);
}
