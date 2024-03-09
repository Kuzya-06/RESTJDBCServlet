package ru.kuznetsov.dao;

import lombok.extern.slf4j.Slf4j;
import ru.kuznetsov.dao.inter.EmployeeRepository;
import ru.kuznetsov.dao.inter.EmployeeToProjectRepository;
import ru.kuznetsov.dao.inter.ProjectRepository;
import ru.kuznetsov.dao.inter.RoleRepository;
import ru.kuznetsov.dao.sql.EmployeeSQLQuery;
import ru.kuznetsov.db.ConnectionManager;
import ru.kuznetsov.db.ConnectionManagerImpl;
import ru.kuznetsov.entity.Employee;
import ru.kuznetsov.entity.EmployeeToProject;
import ru.kuznetsov.entity.Project;
import ru.kuznetsov.entity.Role;
import ru.kuznetsov.exception.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class EmployeeDAO implements EmployeeRepository {

    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
    private final RoleRepository roleDAO = RoleDAO.getInstance();
    private final ProjectRepository projectDAO = ProjectDAO.getInstance();
    private final EmployeeToProjectRepository employeeToProjectDAO = EmployeeToProjectDAO.getInstance();
    private static EmployeeRepository instance;

    private EmployeeDAO() {
    }

    public static synchronized EmployeeRepository getInstance() {
        if (instance == null) {
            instance = new EmployeeDAO();
        }
        return instance;
    }

    /**
     * Сохранить в базу сущность employee,
     * 1. сохраняем самого employee,
     * 2. сохраняем его роль
     * 3. сохраняем список проектов.
     */
    @Override
    public Employee save(Employee employee) {
        log.info("Begin save()");
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EmployeeSQLQuery.SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, employee.getName());
            if (employee.getRole() == null) {
                preparedStatement.setNull(2, Types.NULL);
            } else {
                preparedStatement.setInt(2, employee.getRole().getId());
            }
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                employee = new Employee(
                        resultSet.getInt("id"),
                        employee.getName(),
                        employee.getRole(),
                        null
                );
            }
            saveProjectList(employee);
            employee.getProjectList();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return employee;
    }

    /**
     * 1. Проверяем список на пустоту
     * 1.1 если пустой то удаляем все записи из базы которые == empId.
     * 1.2 получаем все записи которые уже есть в базе
     * 1.3 сверяем то что есть, добавляем, обновляем, или удаляем.
     */
    private void saveProjectList(Employee employee) {
        if (employee.getProjectList() != null && !employee.getProjectList().isEmpty()) {
            List<Integer> projectIdList = new ArrayList<>(
                    employee.getProjectList()
                            .stream()
                            .map(Project::getId)
                            .toList()
            );
            List<EmployeeToProject> existsProjectList = employeeToProjectDAO.findAllByProjectId(employee.getId());
            for (EmployeeToProject employeeToProject : existsProjectList) {
                if (!projectIdList.contains(employeeToProject.getProject_id())) {
                    employeeToProjectDAO.deleteById(employeeToProject.getId());
                }
                projectIdList.remove(employeeToProject.getProject_id());
            }
            for (Integer projectId : projectIdList) {
                if (projectDAO.exitsById(projectId)) {
                    EmployeeToProject employeeToProject = new EmployeeToProject(
                            null,
                            employee.getId(),
                            projectId
                    );
                    employeeToProjectDAO.save(employeeToProject);
                }
            }
        } else {
            employeeToProjectDAO.deleteByEmployeeId(employee.getId());
        }
    }

    @Override
    public void update(Employee employee) {
        log.info("update() = " + employee);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EmployeeSQLQuery.UPDATE_SQL);) {

            preparedStatement.setString(1, employee.getName());

            if (employee.getRole() == null) {
                preparedStatement.setNull(2, Types.NULL);
            } else {
                preparedStatement.setInt(2, employee.getRole().getId());
            }
            preparedStatement.setInt(3, employee.getId());
            preparedStatement.executeUpdate();
            log.info("update() preparedStatement = " + preparedStatement);
            saveProjectList(employee);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EmployeeSQLQuery.DELETE_SQL);) {

            employeeToProjectDAO.deleteByEmployeeId(id);

            preparedStatement.setInt(1, id);
            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return deleteResult;
    }

    @Override
    public Optional<Employee> findById(Integer id) {
        Employee emp = null;
        log.info("Begin findById()");
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EmployeeSQLQuery.FIND_BY_ID_SQL)) {

            preparedStatement.setInt(1, id);
            log.info("findById() preparedStatement = "+preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                emp = createEmployee(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        log.info("end findById() "+emp);
        return Optional.ofNullable(emp);
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> userList = new ArrayList<>();
        log.info("Begin findAll()");
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EmployeeSQLQuery.FIND_ALL_SQL)) {

            log.info("preparedStatement = "+preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userList.add(createEmployee(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        log.info("end findAll() "+userList);
        return userList;
    }

    private Employee createEmployee(ResultSet resultSet) throws SQLException {
        log.info("Begin createEmployee()"+resultSet);
        Integer empId = resultSet.getInt("id");
        Role role = roleDAO.findById(resultSet.getInt("role_id")).orElse(null);
        Employee name = new Employee(
                empId,
                resultSet.getString("name"),
                role,
                null
        );
        log.info("createEmployee() EmployeeName = "+name);
        return name;
    }

    @Override
    public boolean exitsById(Integer id) {
        boolean isExists = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EmployeeSQLQuery.EXIST_BY_ID_SQL)) {

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
}
