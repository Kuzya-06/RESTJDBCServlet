package ru.kuznetsov.mapper;

import ru.kuznetsov.dto.ProjectIncomingDto;
import ru.kuznetsov.dto.ProjectOutGoingDto;
import ru.kuznetsov.dto.ProjectUpdateDto;
import ru.kuznetsov.entity.Project;

import java.util.List;

public interface ProjectDtoMapper {

    Project map(ProjectIncomingDto projectIncomingDto);

    ProjectOutGoingDto map(Project project);

    Project map(ProjectUpdateDto projectUpdateDto);

    List<ProjectOutGoingDto> map(List<Project> projectList);

    List<Project> mapUpdateList(List<ProjectUpdateDto> projectList);
}
