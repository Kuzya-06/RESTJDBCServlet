package ru.kuznetsov.dao;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.kuznetsov.dao.inter.EmployeeRepository;
import ru.kuznetsov.entity.Employee;
import ru.kuznetsov.entity.Project;
import ru.kuznetsov.entity.Role;
import ru.kuznetsov.utils.PropertiesUtil;

import java.util.List;
import java.util.Optional;

@Testcontainers
@Tag("DockerRequired")
class EmployeeDAOTest {
    private static final String INIT_SQL = "sql/data.sql";
    private static final int containerPort = 5432;
    private static final int localPort = 5433;
    @Container
    public static PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>("postgres:16.0")
                    .withDatabaseName("Aston")
                    .withUsername(PropertiesUtil.getProperties("connection.username"))
                    .withPassword(PropertiesUtil.getProperties("connection.password"))
                    .withExposedPorts(containerPort)
                    .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                            new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(localPort), new ExposedPort(containerPort)))
                    ))
                    .withInitScript(INIT_SQL);
    public static EmployeeRepository employeeRepository;
    private static JdbcDatabaseDelegate jdbcDatabaseDelegate;

    @BeforeAll
    static void beforeAll() {
        container.start();
        employeeRepository = EmployeeDAO.getInstance();
        jdbcDatabaseDelegate = new JdbcDatabaseDelegate(container, "");
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @BeforeEach
    void setUp() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, INIT_SQL);
    }

    @Test
    void save() {
        String expectedName = "new Name";

        Employee user = new Employee(
                null,
                expectedName,
                null,
                null);
        user = employeeRepository.save(user);
        Optional<Employee> resultUser = employeeRepository.findById(user.getId());

        Assertions.assertTrue(resultUser.isPresent());
        Assertions.assertEquals(expectedName, resultUser.get().getName());
    }

    @Test
    void update() {
        String expectedName = "UPDATE Name";

        Employee employeeForUpdate = employeeRepository.findById(12).get();
        employeeForUpdate.setName(expectedName);

        employeeRepository.update(employeeForUpdate);
        Assertions.assertEquals(expectedName, employeeForUpdate.getName());



//        List<Project> departmentList = employeeForUpdate.getProjectList();
//        int projectListSize = employeeForUpdate.getProjectList().size();
//        Role oldRole = employeeForUpdate.getRole();
//
//        Assertions.assertNotEquals(expectedRoleId, employeeForUpdate.getRole().getId());
//        Assertions.assertNotEquals(expectedName, employeeForUpdate.getName());
//
//        employeeForUpdate.setName(expectedName);
//        employeeRepository.update(employeeForUpdate);
//
//        Employee resultUser = employeeRepository.findById(12).get();
//
//        Assertions.assertEquals(expectedName, resultUser.getName());
//        Assertions.assertEquals(projectListSize, resultUser.getProjectList().size());
//        Assertions.assertEquals(oldRole.getId(), resultUser.getRole().getId());
//
//        employeeForUpdate.setProjectList(List.of());
//        employeeForUpdate.setRole(new Role(expectedRoleId, null));
//        employeeRepository.update(employeeForUpdate);
//        resultUser = employeeRepository.findById(3).get();
//
//
//        Assertions.assertEquals(0, resultUser.getProjectList().size());
//        Assertions.assertEquals(expectedRoleId, resultUser.getRole().getId());
//
//        departmentList.add(new Project(3, null, null));
//        departmentList.add(new Project(4, null, null));
//        employeeForUpdate.setProjectList(departmentList);
//        employeeRepository.update(employeeForUpdate);
//        resultUser = employeeRepository.findById(12).get();
//
//        Assertions.assertEquals(3, resultUser.getProjectList().size());
//
//        departmentList.remove(2);
//        employeeForUpdate.setProjectList(departmentList);
//        employeeRepository.update(employeeForUpdate);
//        resultUser = employeeRepository.findById(12).get();
//
//        Assertions.assertEquals(2, resultUser.getProjectList().size());
//
//        employeeRepository.update(employeeForUpdate);
//        resultUser = employeeRepository.findById(12).get();
//
//        Assertions.assertEquals(2, resultUser.getProjectList().size());
    }

    @Test
    void deleteById() {
        Boolean expectedValue = true;
        int expectedSize = employeeRepository.findAll().size();

        Employee tempEmployee = new Employee(
                null,
                "User for delete Name.",
                null,
                null
        );
        tempEmployee = employeeRepository.save(tempEmployee);

        boolean resultDelete = employeeRepository.deleteById(tempEmployee.getId());
        int roleListAfterSize = employeeRepository.findAll().size();

        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, roleListAfterSize);
    }

    @DisplayName("Find by ID")
   @Test
    void findById() {
        Integer expectedId=1;
        Boolean expectedValue=true;
        Optional<Employee> user = employeeRepository.findById(expectedId);
        Assertions.assertEquals(expectedValue, user.isPresent());
        user.ifPresent(value -> Assertions.assertEquals(expectedId, value.getId()));
    }

    @Test
    void findAll() {
        int expectedSize = 13;
        int resultSize = employeeRepository.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Exist by ID")
   @Test
    void exitsById() {
        Integer roleId=1;
        Boolean expectedValue=true;
        boolean isUserExist = employeeRepository.exitsById(roleId);

        Assertions.assertEquals(expectedValue, isUserExist);
    }

}