package ru.kuznetsov.dao.inter;

import ru.kuznetsov.entity.Employee;
import ru.kuznetsov.entity.EmployeeToProject;
import ru.kuznetsov.entity.Project;

import java.util.List;
import java.util.Optional;

public interface EmployeeToProjectRepository extends Repository<EmployeeToProject, Integer>{
    boolean deleteByEmployeeId(Integer empId);

    boolean deleteByProjectId(Integer projId);

    List<EmployeeToProject> findAllByEmployeesId(Integer empId);

    List<Project> findProjectsByEmployeeId(Integer empId);

    List<EmployeeToProject> findAllByProjectId(Integer projId);

    List<Employee> findEmployeesByProjectId(Integer projId);

    Optional<EmployeeToProject> findByEmployeeIdAndProjectId(Integer empId, Integer projId);
}
