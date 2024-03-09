package ru.kuznetsov.service.impl;

import lombok.extern.slf4j.Slf4j;
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
import ru.kuznetsov.mapper.ProjectDtoMapper;
import ru.kuznetsov.mapper.impl.ProjectDtoMapperImpl;
import ru.kuznetsov.service.ProjectService;

import java.util.List;

@Slf4j
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository pRepository = ProjectDAO.getInstance();
    private final EmployeeRepository empRepository = EmployeeDAO.getInstance();
    private final EmployeeToProjectRepository empToProjectRepository = EmployeeToProjectDAO.getInstance();
    private static final ProjectDtoMapper projectDtoMapper = ProjectDtoMapperImpl.getInstance();
    private static ProjectService instance;


    private ProjectServiceImpl() {
    }

    public static synchronized ProjectService getInstance() {
        if (instance == null) {
            instance = new ProjectServiceImpl();
        }
        return instance;
    }

    private void checkExistProject(Integer projectId) throws NotFoundException {
        if (!pRepository.exitsById(projectId)) {
            throw new NotFoundException("Project not found.");
        }
    }

    @Override
    public ProjectOutGoingDto save(ProjectIncomingDto projDto) {
        Project project = projectDtoMapper.map(projDto);
        project = pRepository.save(project);
        return projectDtoMapper.map(project);
    }

    @Override
    public void update(ProjectUpdateDto projectUpdateDto) throws NotFoundException {
        checkExistProject(projectUpdateDto.getId());
        Project proj = projectDtoMapper.map(projectUpdateDto);
        pRepository.update(proj);
    }

    @Override
    public ProjectOutGoingDto findById(Integer projectId) throws NotFoundException {
        Project proj = pRepository.findById(projectId).orElseThrow(() ->
                new NotFoundException("Project not found."));
        return projectDtoMapper.map(proj);
    }

    @Override
    public List<ProjectOutGoingDto> findAll() {
        log.info(" Begin findAll()");
        List<Project> proList = pRepository.findAll();
        log.info(" proList "+proList);
        List<ProjectOutGoingDto> map = projectDtoMapper.map(proList);
        log.info(" List<ProjectOutGoingDto> map "+map);
        return map;
    }

    @Override
    public void delete(Integer proId) throws NotFoundException {
        checkExistProject(proId);
        pRepository.deleteById(proId);
    }

    @Override
    public void deleteEmployeeFromProject(Integer proId, Integer empId) throws NotFoundException {
        checkExistProject(proId);
        if (empRepository.exitsById(empId)) {
            EmployeeToProject linkEmpProject = empToProjectRepository.findByEmployeeIdAndProjectId(empId, proId)
                    .orElseThrow(() -> new NotFoundException("Link many to many Not found."));

            empToProjectRepository.deleteById(linkEmpProject.getId());
        } else {
            throw new NotFoundException("Employee not found.");
        }

    }

    @Override
    public void addEmployeeToProject(Integer proId, Integer empId) throws NotFoundException {
        checkExistProject(proId);
        if (empRepository.exitsById(empId)) {
            EmployeeToProject linkEmpProject = new EmployeeToProject(
                    null,
                    empId,
                    proId
            );
            empToProjectRepository.save(linkEmpProject);
        } else {
            throw new NotFoundException("Employee not found.");
        }

    }

}
