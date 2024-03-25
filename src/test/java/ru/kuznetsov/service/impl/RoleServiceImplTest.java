package ru.kuznetsov.service.impl;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.kuznetsov.dao.RoleDAO;
import ru.kuznetsov.dao.inter.RoleRepository;
import ru.kuznetsov.dto.RoleIncomingDto;
import ru.kuznetsov.dto.RoleOutGoingDto;
import ru.kuznetsov.dto.RoleUpdateDto;
import ru.kuznetsov.entity.Role;
import ru.kuznetsov.exception.NotFoundException;
import ru.kuznetsov.service.RoleService;

import java.lang.reflect.Field;
import java.util.Optional;
@Tag("test")
class RoleServiceImplTest {
    private static RoleService roleService;
    private static RoleRepository mockRoleRepository;
    private static RoleDAO oldInstance;

    private static void setMock(RoleRepository mock) {
        try {
            Field instance = RoleDAO.class.getDeclaredField("instance");
            instance.setAccessible(true);
            oldInstance = (RoleDAO) instance.get(instance);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        mockRoleRepository = Mockito.mock(RoleRepository.class);
        setMock(mockRoleRepository);
        roleService = RoleServiceImpl.getInstance();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field instance = RoleDAO.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(instance, oldInstance);
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(mockRoleRepository);
    }

    @Test
    void save() {
        Integer expectedId = 1;

        RoleIncomingDto dto = new RoleIncomingDto("role #2");
        Role role = new Role(expectedId, "role #10");

        Mockito.doReturn(role).when(mockRoleRepository).save(Mockito.any(Role.class));

        RoleOutGoingDto result = roleService.save(dto);

        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void update() throws NotFoundException {
        Integer expectedId = 1;

        RoleUpdateDto dto = new RoleUpdateDto(expectedId, "role update #1");

        Mockito.doReturn(true).when(mockRoleRepository).exitsById(Mockito.any());

        roleService.update(dto);

        ArgumentCaptor<Role> argumentCaptor = ArgumentCaptor.forClass(Role.class);
        Mockito.verify(mockRoleRepository).update(argumentCaptor.capture());

        Role result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result.getId());
    }

    @Test
    void updateNotFound() {
        RoleUpdateDto dto = new RoleUpdateDto(1, "role update #1");

        Mockito.doReturn(false).when(mockRoleRepository).exitsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> {
                    roleService.update(dto);
                }, "Not found."
        );
        Assertions.assertEquals("Role not found.", exception.getMessage());
    }

    @Test
    void findById() throws NotFoundException {
        Integer expectedId = 1;

        Optional<Role> role = Optional.of(new Role(expectedId, "role found #1"));

        Mockito.doReturn(true).when(mockRoleRepository).exitsById(Mockito.any());
        Mockito.doReturn(role).when(mockRoleRepository).findById(Mockito.anyInt());

        RoleOutGoingDto dto = roleService.findById(expectedId);

        Assertions.assertEquals(expectedId, dto.getId());
    }

    @Test
    void findByIdNotFound() {
        Optional<Role> role = Optional.empty();

        Mockito.doReturn(false).when(mockRoleRepository).exitsById(Mockito.any());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> {
                    roleService.findById(1);
                }, "Not found."
        );
        Assertions.assertEquals("Role not found.", exception.getMessage());
    }

    @Test
    void findAll() {
        roleService.findAll();
        Mockito.verify(mockRoleRepository).findAll();
    }

    @Test
    void delete() throws NotFoundException {
        Integer expectedId = 100;

        Mockito.doReturn(true).when(mockRoleRepository).exitsById(100);

        roleService.delete(expectedId);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(mockRoleRepository).deleteById(argumentCaptor.capture());

        Integer result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedId, result);
    }
}