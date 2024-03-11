package ru.kuznetsov.mapper.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kuznetsov.dto.*;
import ru.kuznetsov.entity.Employee;
import ru.kuznetsov.entity.Project;
import ru.kuznetsov.entity.Role;
import ru.kuznetsov.mapper.EmployeeDtoMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDtoMapperImplTest {
    private EmployeeDtoMapper userDtoMapper;

    @BeforeEach
    void setUp() {
        userDtoMapper = EmployeeDtoMapperImpl.getInstance();
    }

    @DisplayName("User map(UserIncomingDto")
    @Test
    void mapIncoming() {
        EmployeeIncomingDto dto = new EmployeeIncomingDto(
                "f1",
                new Role(1, "role1")
        );
        Employee result = userDtoMapper.map(dto);

        Assertions.assertNull(result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
        Assertions.assertEquals(dto.getRole().getId(), result.getRole().getId());
    }

    @DisplayName("Employee map(EmployeeUpdateDto")
    @Test
    void testMapUpdate() {
        EmployeeUpdateDto dto = new EmployeeUpdateDto(
                100,
                "f1",
                new RoleUpdateDto(1, "Role update"),
                List.of(new ProjectUpdateDto())
        );
        Employee result = userDtoMapper.map(dto);

        Assertions.assertEquals(dto.getId(), result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());

        Assertions.assertEquals(dto.getRole().getId(), result.getRole().getId());
        Assertions.assertEquals(dto.getProjectList().size(), result.getProjectList().size());
    }

    @DisplayName("EmployeeOutGoingDto map(Employee)")
    @Test
    void testMapOutgoing() {
        Employee user = new Employee(
                100,
                "f1",
                new Role(1, "Role #1"),

                List.of(new Project(1, "d1", List.of()))
        );
        EmployeeOutGoingDto result = userDtoMapper.map(user);

        Assertions.assertEquals(user.getId(), result.getId());
        Assertions.assertEquals(user.getName(), result.getName());
        Assertions.assertEquals(user.getRole().getId(), result.getRole().getId());
        Assertions.assertEquals(user.getProjectList().size(), result.getProjectList().size());
    }

    @DisplayName("List<EmployeeOutGoingDto> map(List<Employee>")
    @Test
    void testMapList() {
        List<Employee> empList = List.of(
                new Employee(
                        100,
                        "f1",
                        new Role(1, "Role #1"),
                        List.of(new Project(1, "d1", List.of()))
                ),
                new Employee(
                        101,
                        "f3",
                        new Role(1, "Role #1"),
                        List.of(new Project(2, "d2", List.of()))
                )
        );
        int result = userDtoMapper.map(empList).size();
        Assertions.assertEquals(empList.size(), result);
    }
}