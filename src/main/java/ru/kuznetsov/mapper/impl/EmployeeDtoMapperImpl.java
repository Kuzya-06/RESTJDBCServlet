package ru.kuznetsov.mapper.impl;

import ru.kuznetsov.dto.EmployeeIncomingDto;
import ru.kuznetsov.dto.EmployeeOutGoingDto;
import ru.kuznetsov.dto.EmployeeUpdateDto;
import ru.kuznetsov.entity.Employee;
import ru.kuznetsov.mapper.EmployeeDtoMapper;
import ru.kuznetsov.mapper.ProjectDtoMapper;
import ru.kuznetsov.mapper.RoleDtoMapper;

import java.util.List;

public class EmployeeDtoMapperImpl implements EmployeeDtoMapper {
    private static final RoleDtoMapper roleDtoMapper = RoleDtoMapperImpl.getInstance();
    private static final ProjectDtoMapper projectDtoMapper = ProjectDtoMapperImpl.getInstance();


    private static EmployeeDtoMapper instance;

    private EmployeeDtoMapperImpl() {
    }

    public static synchronized EmployeeDtoMapper getInstance() {
        if (instance == null) {
            instance = new EmployeeDtoMapperImpl();
        }
        return instance;
    }

    @Override
    public Employee map(EmployeeIncomingDto empDto) {
        return new Employee(
                null,
                empDto.getName(),
                empDto.getRole(),
                null
        );
    }

    // Для изменения
    @Override
    public Employee map(EmployeeUpdateDto empDto) {
        return new Employee(
                empDto.getId()
                , empDto.getName()
                , roleDtoMapper.map(empDto.getRole())
                , projectDtoMapper.mapUpdateList(empDto.getProjectList())
        );
    }

    @Override
    public EmployeeOutGoingDto map(Employee employee) {
        return new EmployeeOutGoingDto(
                employee.getId(),
                employee.getName(),
                roleDtoMapper.map(employee.getRole()),
                projectDtoMapper.map(employee.getProjectList())
        );
    }

    @Override
    public List<EmployeeOutGoingDto> map(List<Employee> employees) {
        return employees.stream().map(this::map).toList();
    }
}
