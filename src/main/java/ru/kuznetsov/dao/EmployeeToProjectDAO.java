package ru.kuznetsov.dao;

import ru.kuznetsov.dao.inter.EmployeeRepository;
import ru.kuznetsov.dao.inter.EmployeeToProjectRepository;
import ru.kuznetsov.dao.inter.ProjectRepository;
import ru.kuznetsov.dao.sql.EmpToProjectSQLQuery;
import ru.kuznetsov.db.ConnectionManager;
import ru.kuznetsov.db.ConnectionManagerImpl;
import ru.kuznetsov.entity.Employee;
import ru.kuznetsov.entity.EmployeeToProject;
import ru.kuznetsov.entity.Project;
import ru.kuznetsov.exception.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeToProjectDAO implements EmployeeToProjectRepository {

    private static final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
    private static final ProjectRepository projectRepo = ProjectDAO.getInstance();
    private static final EmployeeRepository employeeRepo = EmployeeDAO.getInstance();
    private static EmployeeToProjectRepository instance;

    private EmployeeToProjectDAO() {
    }
    public static synchronized EmployeeToProjectRepository getInstance() {
        if (instance == null) {
            instance = new EmployeeToProjectDAO();
        }
        return instance;
    }
    @Override
    public EmployeeToProject save(EmployeeToProject employeeToProject) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EmpToProjectSQLQuery.SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, employeeToProject.getEmployee_id());
            preparedStatement.setInt(2, employeeToProject.getProject_id());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                employeeToProject = new EmployeeToProject(
                        resultSet.getInt("id"),
                        employeeToProject.getEmployee_id(),
                        employeeToProject.getProject_id()
                );
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return employeeToProject;
    }

    @Override
    public void update(EmployeeToProject employeeToProject) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EmpToProjectSQLQuery.UPDATE_SQL);) {

            preparedStatement.setInt(1, employeeToProject.getEmployee_id());
            preparedStatement.setLong(2, employeeToProject.getProject_id());
            preparedStatement.setLong(3, employeeToProject.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EmpToProjectSQLQuery.DELETE_SQL);) {

            preparedStatement.setInt(1, id);

            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return deleteResult;
    }

    @Override
    public Optional<EmployeeToProject> findById(Integer id) {
        EmployeeToProject userToDepartment = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EmpToProjectSQLQuery.FIND_BY_ID_SQL)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userToDepartment = createEmpToProject(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return Optional.ofNullable(userToDepartment);
    }

    private EmployeeToProject createEmpToProject(ResultSet resultSet) throws SQLException {
        EmployeeToProject empToProject;
        empToProject = new EmployeeToProject(
                resultSet.getInt("id"),
                resultSet.getInt("employee_id"),
                resultSet.getInt("project_id")
        );
        return empToProject;
    }

    @Override
    public List<EmployeeToProject> findAll() {
        List<EmployeeToProject> empToProjectList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EmpToProjectSQLQuery.FIND_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                empToProjectList.add(createEmpToProject(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return empToProjectList;
    }

    @Override
    public boolean exitsById(Integer id) {
        boolean isExists = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EmpToProjectSQLQuery.EXIST_BY_ID_SQL)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isExists = resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return isExists;
    }

    @Override
    public boolean deleteByEmployeeId(Integer empId) {
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EmpToProjectSQLQuery.DELETE_BY_USERID_SQL);) {

            preparedStatement.setInt(1, empId);

            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return deleteResult;
    }

    @Override
    public boolean deleteByProjectId(Integer projId) {
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EmpToProjectSQLQuery.DELETE_BY_DEPARTMENT_ID_SQL);) {

            preparedStatement.setInt(1, projId);

            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return deleteResult;
    }

    @Override
    public List<EmployeeToProject> findAllByEmployeesId(Integer empId) {
        List<EmployeeToProject> userToDepartmentList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EmpToProjectSQLQuery.FIND_ALL_BY_USERID_SQL)) {

            preparedStatement.setLong(1, empId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userToDepartmentList.add(createEmpToProject(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return userToDepartmentList;
    }

    @Override
    public List<Project> findProjectsByEmployeeId(Integer empId) {
        List<Project> projectList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EmpToProjectSQLQuery.FIND_ALL_BY_USERID_SQL)) {

            preparedStatement.setLong(1, empId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer projectId = resultSet.getInt("project_id");
                Optional<Project> optionalProject = projectRepo.findById(projectId);
                if (optionalProject.isPresent()) {
                    projectList.add(optionalProject.get());
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return projectList;
    }

    @Override
    public List<EmployeeToProject> findAllByProjectId(Integer projId) {
        List<EmployeeToProject> empToProjectList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EmpToProjectSQLQuery.FIND_ALL_BY_DEPARTMENT_ID_SQL)) {

            preparedStatement.setInt(1, projId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                empToProjectList.add(createEmpToProject(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return empToProjectList;
    }

    @Override
    public List<Employee> findEmployeesByProjectId(Integer projId) {
        List<Employee> employeeList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EmpToProjectSQLQuery.FIND_ALL_BY_DEPARTMENT_ID_SQL)) {

            preparedStatement.setLong(1, projId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int empId = resultSet.getInt("employee_id");
                Optional<Employee> optionalEmp = employeeRepo.findById(empId);
                if (optionalEmp.isPresent()) {
                    employeeList.add(optionalEmp.get());
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return employeeList;
    }

    @Override
    public Optional<EmployeeToProject> findByEmployeeIdAndProjectId(Integer empId, Integer projId) {
        Optional<EmployeeToProject> employeeToProject = Optional.empty();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EmpToProjectSQLQuery.FIND_BY_USERID_AND_DEPARTMENT_ID_SQL)) {

            preparedStatement.setInt(1, empId);
            preparedStatement.setInt(2, projId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                employeeToProject = Optional.of(createEmpToProject(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return employeeToProject;
    }
}
