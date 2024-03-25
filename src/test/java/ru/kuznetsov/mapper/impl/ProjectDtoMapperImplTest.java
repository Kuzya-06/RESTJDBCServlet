package ru.kuznetsov.mapper.impl;

import org.junit.jupiter.api.*;
import ru.kuznetsov.dto.ProjectIncomingDto;
import ru.kuznetsov.dto.ProjectOutGoingDto;
import ru.kuznetsov.dto.ProjectUpdateDto;
import ru.kuznetsov.entity.Employee;
import ru.kuznetsov.entity.Project;
import ru.kuznetsov.mapper.ProjectDtoMapper;
import java.util.List;
@Tag("test")
class ProjectDtoMapperImplTest {
    private ProjectDtoMapper projectDtoMapper;

    @BeforeEach
    void setUp() {
        projectDtoMapper = ProjectDtoMapperImpl.getInstance();
    }

    @DisplayName("Project map(ProjectIncomingDto)")
    @Test
    void mapIncoming() {
        ProjectIncomingDto dto = new ProjectIncomingDto("New Project");
        Project result = projectDtoMapper.map(dto);

        Assertions.assertNull(result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
    }

    @DisplayName("ProjectOutGoingDto map(Project)")
    @Test
    void testMapOutgoing() {
        Project project = new Project(100, "Project #100", List.of(new Employee(), new Employee()));

        ProjectOutGoingDto result = projectDtoMapper.map(project);

        Assertions.assertEquals(project.getId(), result.getId());
        Assertions.assertEquals(project.getName(), result.getName());
        Assertions.assertEquals(project.getEmployeeList().size(), result.getEmployeeList().size());
    }

    @DisplayName("Department map(DepartmentUpdateDto)")
    @Test
    void testMapUpdate() {
        ProjectUpdateDto dto = new ProjectUpdateDto(10, "Update name.");

        Project result = projectDtoMapper.map(dto);
        Assertions.assertEquals(dto.getId(), result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
    }

    @DisplayName("List<ProjectOutGoingDto> map(List<Project>)")
    @Test
    void testMap2() {
        List<Project> departmentList = List.of(
                new Project(1, "Project 1", List.of()),
                new Project(2, "Project 2", List.of()),
                new Project(3, "Project 3", List.of())
        );

        List<ProjectOutGoingDto> result = projectDtoMapper.map(departmentList);

        Assertions.assertEquals(3, result.size());
    }

    @DisplayName("List<Project> mapUpdateList(List<ProjectUpdateDto>)")
    @Test
    void mapUpdateList() {
        List<ProjectUpdateDto> projectUpdateDtos = List.of(
                new ProjectUpdateDto(),
                new ProjectUpdateDto(),
                new ProjectUpdateDto()
        );

        List<Project> result = projectDtoMapper.mapUpdateList(projectUpdateDtos);

        Assertions.assertEquals(3, result.size());
    }
}