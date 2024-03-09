package ru.kuznetsov.service;

import ru.kuznetsov.dto.EmployeeIncomingDto;
import ru.kuznetsov.dto.EmployeeOutGoingDto;
import ru.kuznetsov.dto.EmployeeUpdateDto;
import ru.kuznetsov.exception.NotFoundException;

import java.util.List;

public interface EmployeeService {
    EmployeeOutGoingDto save(EmployeeIncomingDto empDto);

    void update(EmployeeUpdateDto empDto) throws NotFoundException;

    EmployeeOutGoingDto findById(Integer empId) throws NotFoundException;

    List<EmployeeOutGoingDto> findAll();

    void delete(Integer empId) throws NotFoundException;
}
