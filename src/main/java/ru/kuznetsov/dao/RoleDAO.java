package ru.kuznetsov.dao;

import ru.kuznetsov.dao.inter.RoleRepository;
import ru.kuznetsov.dao.sql.RoleSQLQuery;
import ru.kuznetsov.db.ConnectionManager;
import ru.kuznetsov.db.ConnectionManagerImpl;
import ru.kuznetsov.entity.Role;
import ru.kuznetsov.exception.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoleDAO implements RoleRepository {

    private static RoleRepository instance;
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();

    private RoleDAO() {
    }

    public static synchronized RoleRepository getInstance() {
        if (instance == null) {
            instance = new RoleDAO();
        }
        return instance;
    }

    private static Role createRole(ResultSet resultSet) throws SQLException {
        Role role;
        role = new Role(resultSet.getInt("id"),
                resultSet.getString("name"));
        return role;
    }

    @Override
    public Role save(Role role) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(RoleSQLQuery.SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, role.getName());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                role = new Role(
                        resultSet.getInt("id"),
                        role.getName());
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return role;
    }

    @Override
    public void update(Role role) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(RoleSQLQuery.UPDATE_SQL);) {

            preparedStatement.setString(1, role.getName());
            preparedStatement.setInt(2, role.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(RoleSQLQuery.DELETE_SQL);) {

            preparedStatement.setInt(1, id);

            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return deleteResult;
    }

    @Override
    public Optional<Role> findById(Integer id) {
        Role role = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(RoleSQLQuery.FIND_BY_ID_SQL)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                role = createRole(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return Optional.ofNullable(role);
    }

    @Override
    public List<Role> findAll() {
        List<Role> roleList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(RoleSQLQuery.FIND_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                roleList.add(createRole(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return roleList;
    }

    @Override
    public boolean exitsById(Integer id) {
        boolean isExists = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(RoleSQLQuery.EXIST_BY_ID_SQL)) {

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
