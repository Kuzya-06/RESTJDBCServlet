package ru.kuznetsov.dao;

import lombok.extern.slf4j.Slf4j;
import ru.kuznetsov.dao.inter.ProjectRepository1;
import ru.kuznetsov.db.ConnectionManager;
import ru.kuznetsov.db.ConnectionManagerImpl;
import ru.kuznetsov.dto.RoleIncomingDto;
import ru.kuznetsov.entity.Employee1;
import ru.kuznetsov.entity.Project1;
import ru.kuznetsov.exception.RepositoryException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ProjectDAO1 implements ProjectRepository1 {

    private String SQL_GET_BY_ID = """
            SELECT  p.id, p.name, e.id, e.name, r.name
                           FROM task3.employee e
                                    JOIN task3.role r on r.id = e.role_id
                                    JOIN task3.employeeproject e2 on e.id = e2.employee_id
                                    JOIN task3.project p on p.id = e2.project_id
                           WHERE e2.project_id = ?;
              """;

    private String SQL_GET_ALL = """
            SELECT  p.id, p.name, e.id, e.name, r.name
            FROM task3.project p
                     JOIN task3.employeeproject ep on p.id = ep.project_id
                     JOIN task3.employee e on e.id = ep.employee_id
            JOIN task3.role r on r.id = e.role_id
            ORDER BY p.id;
                                 """;
    private static ProjectRepository1 instance;
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();

    private ProjectDAO1() {
    }

    public static synchronized ProjectRepository1 getInstance() {
        if (instance == null) {
            instance = new ProjectDAO1();
        }
        return instance;
    }


    @Override
    public Optional<Project1> findById(Integer id) {
        log.info("begin findByID = " + id);
        Project1 project1 = new Project1();
        Employee1 employee1;
        RoleIncomingDto role = new RoleIncomingDto();

        try (Connection connection = connectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_BY_ID)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            int anInt = 0;
            String nameProject = "";
            List<Employee1> list = new ArrayList<>();

            while (resultSet.next()) {
                employee1 = new Employee1();
                ResultSetMetaData metaData = resultSet.getMetaData();

                anInt = resultSet.getInt(1);
                nameProject = resultSet.getString(2);
                int idEmp = resultSet.getInt(3);
                String nameEmp = resultSet.getString(4);
                String nameRole = resultSet.getString(5);

                role.setName(nameRole);

                employee1.setId(idEmp);
                employee1.setName(nameEmp);
                employee1.setRole(role);
                list.add(0, employee1);

            }
            project1.setId(anInt);
            project1.setName(nameProject);
            project1.setEmployeeList(list);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        log.info("The end findByID = " + id);
        return Optional.ofNullable(project1);
    }


    @Override
    public ArrayList<Project1> findAll() {
        log.info(" Begin findAll()");
        Set<Project1> projectList = new HashSet<>();
        Employee1 employee1;
        RoleIncomingDto role;

        try (Connection connection = connectionManager.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            int projectId = 0;
            String projectName = "";
            List<Employee1> employee1List = null;
            Project1 projectNew = null;

            int tempId = -1;

            while (resultSet.next()) {

                employee1 = new Employee1();
                role = new RoleIncomingDto();

                projectId = resultSet.getInt(1);
                projectName = resultSet.getString(2);
                int employeeId = resultSet.getInt(3);
                String employeeName = resultSet.getString(4);
                String roleName = resultSet.getString(5);

                role.setName(roleName);
                employee1.setId(employeeId);
                employee1.setName(employeeName);
                employee1.setRole(role);

                if (projectId != tempId) {
                    employee1List = new ArrayList<>();

                    employee1List.add(employee1);

                    projectNew = new Project1();

                    projectNew.setId(projectId);
                    projectNew.setName(projectName);

                    tempId = projectId;
                } else {
                    employee1List.add(employee1);

                    projectNew.setId(projectId);
                    projectNew.setName(projectName);

                }
                projectNew.setEmployeeList(employee1List);

                projectList.add(projectNew);
            }

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        ArrayList<Project1> collect = (ArrayList<Project1>) projectList.stream().collect(Collectors.toList());
        log.info("The end");
        return collect;
    }

    @Override
    public boolean exitsById(Integer id) {
        return false;
    }

    @Override
    public Project1 save(Project1 project1) {
        return null;
    }

    @Override
    public void update(Project1 project1) {

    }

    @Override
    public boolean deleteById(Integer id) {
        return false;
    }
}
