package ru.kuznetsov.dao;

import lombok.extern.slf4j.Slf4j;
import ru.kuznetsov.dao.inter.ProjectRepository;
import ru.kuznetsov.dao.sql.ProjectSQLQuery;
import ru.kuznetsov.db.ConnectionManager;
import ru.kuznetsov.db.ConnectionManagerImpl;
import ru.kuznetsov.entity.Project;
import ru.kuznetsov.exception.RepositoryException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ProjectDAO implements ProjectRepository {

    private static ProjectRepository instance;
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();

    private ProjectDAO() {
    }

    public static synchronized ProjectRepository getInstance() {
        if (instance == null) {
            instance = new ProjectDAO();
        }
        return instance;
    }

    @Override
    public Project save(Project project) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ProjectSQLQuery.SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, project.getName());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                project = new Project(
                        resultSet.getInt("id"),
                        project.getName(),
                        null
                        );
            }
            project.getEmployeeList();

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return project;
    }

    @Override
    public void update(Project project) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ProjectSQLQuery.UPDATE_SQL);) {

            preparedStatement.setString(1, project.getName());
            preparedStatement.setInt(2, project.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean deleteResult = true;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ProjectSQLQuery.DELETE_SQL)) {

            preparedStatement.setInt(1, id);

            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return deleteResult;
    }

    @Override
    public Optional<Project> findById(Integer id) {
        Project department = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ProjectSQLQuery.FIND_BY_ID_SQL)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                department = createProject(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return Optional.ofNullable(department);
    }

    private static Project createProject(ResultSet resultSet) throws SQLException{
        Project project;
        project = new Project(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                null
        );
        return project;
    }

    @Override
    public List<Project> findAll() {
        log.info(" Begin findAll()");
        List<Project> projectList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ProjectSQLQuery.FIND_ALL_SQL)) {
            log.info(" findAll() preparedStatement - "+preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                projectList.add(createProject(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        log.info(projectList.toString());
        return projectList;
    }

    @Override
    public boolean exitsById(Integer id) {
        boolean isExists = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ProjectSQLQuery.EXIST_BY_ID_SQL)) {

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
