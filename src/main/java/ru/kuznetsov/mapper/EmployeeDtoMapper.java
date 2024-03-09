package ru.kuznetsov.mapper;

import ru.kuznetsov.dto.EmployeeIncomingDto;
import ru.kuznetsov.dto.EmployeeOutGoingDto;
import ru.kuznetsov.dto.EmployeeUpdateDto;
import ru.kuznetsov.entity.Employee;

import java.util.List;

public interface EmployeeDtoMapper {
    Employee map(EmployeeIncomingDto empIncomingDto);

    Employee map(EmployeeUpdateDto empIncomingDto);

    EmployeeOutGoingDto map(Employee employee);

    List<EmployeeOutGoingDto> map(List<Employee> employees);

}
