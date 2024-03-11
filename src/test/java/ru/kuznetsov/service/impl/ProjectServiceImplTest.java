package ru.kuznetsov.service.impl;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.kuznetsov.dao.EmployeeDAO;
import ru.kuznetsov.dao.EmployeeToProjectDAO;
import ru.kuznetsov.dao.ProjectDAO;
import ru.kuznetsov.dao.inter.EmployeeRepository;
import ru.kuznetsov.dao.inter.EmployeeToProjectRepository;
import ru.kuznetsov.dao.inter.ProjectRepository;
import ru.kuznetsov.dto.ProjectIncomingDto;
import ru.kuznetsov.dto.ProjectOutGoingDto;
import ru.kuznetsov.dto.ProjectUpdateDto;
import ru.kuznetsov.entity.EmployeeToProject;
import ru.kuznetsov.entity.Project;
import ru.kuznetsov.exception.NotFoundException;
import ru.kuznetsov.service.ProjectService;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

class ProjectServiceImplTest {
    private static ProjectService projectService;
    private static ProjectRepository mockProjectRepository;
    private static EmployeeRepository mockEmployeeRepository;
    private static EmployeeToProjectRepository mockEmployeeToProjectRepository;
    private static ProjectDAO oldProjectInstance;
    private static EmployeeDAO oldEmployeeInstance;
    private static EmployeeToProjectDAO oldLinkInstance;

    private static void setMock(ProjectRepository mock) {
        try {
            Field instance = ProjectDAO.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldProjectInstance = (ProjectDAO) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setMock(EmployeeRepository mock) {
        try {
            Field instance = EmployeeDAO.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldEmployeeInstance = (EmployeeDAO) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setMock(EmployeeToProjectRepository mock) {
        try {
            Field instance = EmployeeToProjectDAO.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldLinkInstance = (EmployeeToProjectDAO) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        mockProjectRepository = Mockito.mock(ProjectRepository.class);
        setMock(mockProjectRepository);
        mockEmployeeRepository = Mockito.mock(EmployeeRepository.class);
        setMock(mockEmployeeRepository);
        mockEmployeeToProjectRepository = Mockito.mock(EmployeeToProjectRepository.class);
        setMock(mockEmployeeToProjectRepository);

        projectService = ProjectServiceImpl.getInstance();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = ProjectDAO.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldProjectInstance);

        instance = EmployeeDAO.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldEmployeeInstance);

        instance = EmployeeToProjectDAO.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldLinkInstance);
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(mockProjectRepository);
    }

    @Test
    void save() {
        Integer expectedId = 1;

        ProjectIncomingDto dto = new ProjectIncomingDto("project #2");
        Project project = new Project(expectedId, "project #10", List.of());

        Mockito.doReturn(project).when(mockProjectRepository).save(Mockito.any(Project.class));

        ProjectOutGoingDto result = projectService.save(dto);

        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void update() throws NotFoundException {
        Integer expectedId = 1;

        ProjectUpdateDto dto = new ProjectUpdateDto(expectedId, "project update #1");

        Mockito.doReturn(true).when(mockProjectRepository).exitsById(Mockito.any());

        projectService.update(dto);

        ArgumentCaptor<Project> argumentCaptor = ArgumentCaptor.forClass(Project.class);
        Mockito.verify(mockProjectRepository).update(argumentCaptor.capture());

        Project result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void updateNotFound() {
        ProjectUpdateDto dto = new ProjectUpdateDto(1, "Project update #1");

        Mockito.doReturn(false).when(mockProjectRepository).exitsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> {
                    projectService.update(dto);
                }, "Not found."
        );
        Assertions.assertEquals("Project not found.", exception.getMessage());
    }

    @Test
    void findById() throws NotFoundException {
        Integer expectedId = 1;

        Optional<Project> department = Optional.of(new Project(expectedId, "Project found #1", List.of()));

        Mockito.doReturn(true).when(mockProjectRepository).exitsById(Mockito.any());
        Mockito.doReturn(department).when(mockProjectRepository).findById(Mockito.anyInt());

        ProjectOutGoingDto dto = projectService.findById(expectedId);

        Assertions.assertEquals(expectedId, dto.getId());
    }

    @Test
    void findByIdNotFound() {
     //   Optional<Project> project = Optional.empty();

        Mockito.doReturn(false).when(mockProjectRepository).exitsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> {
                    projectService.findById(1);
                }, "Not found."
        );
        Assertions.assertEquals("Project not found.", exception.getMessage());
    }

    @Test
    void findAll() {
        projectService.findAll();
        Mockito.verify(mockProjectRepository).findAll();
    }

    @Test
    void delete() throws NotFoundException {
        Integer expectedId = 100;

        Mockito.doReturn(true).when(mockProjectRepository).exitsById(Mockito.any());
        projectService.delete(expectedId);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(mockProjectRepository).deleteById(argumentCaptor.capture());

        Integer result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result);
    }

    @Test
    void deleteUserFromDepartment() throws NotFoundException {
        Integer expectedId = 100;
        Optional<EmployeeToProject> link = Optional.of(new EmployeeToProject(expectedId, 1, 2));

        Mockito.doReturn(true).when(mockEmployeeRepository).exitsById(Mockito.any());
        Mockito.doReturn(true).when(mockProjectRepository).exitsById(Mockito.any());
        Mockito.doReturn(link).when(mockEmployeeToProjectRepository).findByEmployeeIdAndProjectId(Mockito.anyInt(), Mockito.anyInt());

        projectService.deleteEmployeeFromProject(1, 1);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(mockEmployeeToProjectRepository).deleteById(argumentCaptor.capture());
        Integer result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result);
    }

    @Test
    void addUserToDepartment() throws NotFoundException {
        Integer expectedEmployeeId = 100;
        Integer expectedProjectId = 500;

        Mockito.doReturn(true).when(mockEmployeeRepository).exitsById(Mockito.any());
        Mockito.doReturn(true).when(mockProjectRepository).exitsById(Mockito.any());

        projectService.addEmployeeToProject(expectedProjectId, expectedEmployeeId);

        ArgumentCaptor<EmployeeToProject> argumentCaptor = ArgumentCaptor.forClass(EmployeeToProject.class);
        Mockito.verify(mockEmployeeToProjectRepository).save(argumentCaptor.capture());
        EmployeeToProject result = argumentCaptor.getValue();

        Assertions.assertEquals(expectedEmployeeId, result.getEmployee_id());
        Assertions.assertEquals(expectedProjectId, result.getProject_id());
    }
}