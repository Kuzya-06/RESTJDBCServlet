package ru.kuznetsov.dao;

import lombok.extern.slf4j.Slf4j;
import ru.kuznetsov.dao.inter.EmployeeRepository1;
import ru.kuznetsov.dao.inter.EmployeeToProjectRepository;
import ru.kuznetsov.dao.inter.ProjectRepository;
import ru.kuznetsov.dao.inter.RoleRepository;
import ru.kuznetsov.dao.sql.EmployeeSQLQuery;
import ru.kuznetsov.db.ConnectionManager;
import ru.kuznetsov.db.ConnectionManagerImpl;
import ru.kuznetsov.entity.*;
import ru.kuznetsov.exception.RepositoryException;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Slf4j
public class EmployeeDAO1 implements EmployeeRepository1 {

    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
    private final RoleRepository roleDAO = RoleDAO.getInstance();
    private final ProjectRepository projectDAO = ProjectDAO.getInstance();
    private final EmployeeToProjectRepository employeeToProjectDAO = EmployeeToProjectDAO.getInstance();
    private static EmployeeRepository1 instance;

    private EmployeeDAO1() {
    }

    public static synchronized EmployeeRepository1 getInstance() {
        if (instance == null) {
            instance = new EmployeeDAO1();
        }
        return instance;
    }


    @Override
    public Employee1 save(Employee1 employee1) {
        return null;
    }

    @Override
    public void update(Employee1 employee1) {

    }

    @Override
    public boolean deleteById(Integer id) {
        return false;
    }

    @Override
    public Optional<Employee1> findById(Integer id) {

        return Optional.ofNullable(null);
    }

    @Override
    public List<Employee1> findAll() {
        return null;
    }

    @Override
    public boolean exitsById(Integer id) {
        return false;
    }
}
