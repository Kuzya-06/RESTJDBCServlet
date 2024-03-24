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
import ru.kuznetsov.dao.inter.ProjectRepository;
import ru.kuznetsov.dao.inter.RoleRepository;
import ru.kuznetsov.entity.Project;
import ru.kuznetsov.entity.Role;
import ru.kuznetsov.utils.PropertiesUtil;

import java.util.List;
import java.util.Optional;


@Testcontainers
@Tag("DockerRequired")
class ProjectDAOTest {
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
    public static ProjectRepository projectRepository;
    private static JdbcDatabaseDelegate jdbcDatabaseDelegate;

    @BeforeAll
    static void beforeAll() {
        container.start();
        projectRepository = ProjectDAO.getInstance();
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
        String expectedName = "new Department Yo!";
        Project project = new Project(
                null,
                expectedName,
                null
        );
        project = projectRepository.save(project);
        Optional<Project> resultDepartment = projectRepository.findById(project.getId());

        Assertions.assertTrue(resultDepartment.isPresent());
        Assertions.assertEquals(expectedName, resultDepartment.get().getName());

    }

    @Test
    void update() {
        String expectedName = "Update Project name";

        Project project = projectRepository.findById(2).get();
        String oldName = project.getName();
        int expectedSizeUserList = project.getEmployeeList().size();
        project.setName(expectedName);
        projectRepository.update(project);

        Project resultDepartment = projectRepository.findById(2).get();
        int resultSizeUserList = resultDepartment.getEmployeeList().size();

        Assertions.assertNotEquals(expectedName, oldName);
        Assertions.assertEquals(expectedName, resultDepartment.getName());
        Assertions.assertEquals(expectedSizeUserList, resultSizeUserList);
    }

    @Test
    void deleteById() {
        Boolean expectedValue = true;
        int expectedSize = projectRepository.findAll().size();

        Project tempProject = new Project(null, "New Project", List.of());
        tempProject = projectRepository.save(tempProject);

        int resultSizeBefore = projectRepository.findAll().size();
        Assertions.assertNotEquals(expectedSize, resultSizeBefore);

        boolean resultDelete = projectRepository.deleteById(tempProject.getId());
        int resultSizeAfter = projectRepository.findAll().size();

        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, resultSizeAfter);
    }

    @DisplayName("Find by ID=1")
    @Test
    void findById1() {
        Integer expectedId=1;
        String expectedName = "Project A";
        Optional<Project> project = projectRepository.findById(expectedId);
        Assertions.assertEquals(expectedName, project.get().getName());
    }

    @DisplayName("Find by ID=100")
    @Test
    void findById100() {
        Integer expectedId=100;
        Boolean expectedValue=false;
        boolean isRoleExist = projectRepository.exitsById(expectedId);

        Assertions.assertEquals(expectedValue, isRoleExist);
    }

    @Test
    void findAll() {
        int expectedSize = 5;
        int resultSize = projectRepository.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Exist by ID=1")
    @Test
    void exitsById1() {
        Integer roleId=1;
        Boolean expectedValue=true;
        boolean isRoleExist = projectRepository.exitsById(roleId);

        Assertions.assertEquals(expectedValue, isRoleExist);
    }

    @DisplayName("Exist by ID=100")
    @Test
    void exitsById100() {
        Integer roleId=100;
        Boolean expectedValue=false;
        boolean isRoleExist = projectRepository.exitsById(roleId);

        Assertions.assertEquals(expectedValue, isRoleExist);
    }

}