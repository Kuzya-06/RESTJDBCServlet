package ru.kuznetsov.service.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kuznetsov.dao.EmployeeDAO;
import ru.kuznetsov.dao.inter.EmployeeRepository;
import ru.kuznetsov.dto.EmployeeIncomingDto;
import ru.kuznetsov.dto.EmployeeOutGoingDto;
import ru.kuznetsov.dto.EmployeeUpdateDto;
import ru.kuznetsov.entity.Employee;
import ru.kuznetsov.exception.NotFoundException;
import ru.kuznetsov.mapper.EmployeeDtoMapper;
import ru.kuznetsov.mapper.impl.EmployeeDtoMapperImpl;
import ru.kuznetsov.service.EmployeeService;

import java.util.List;
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository empDAO = EmployeeDAO.getInstance();
    private static final EmployeeDtoMapper empDtoMapper = EmployeeDtoMapperImpl.getInstance();
    private static EmployeeService instance;


    private EmployeeServiceImpl() {
    }

    public static synchronized EmployeeService getInstance() {
        if (instance == null) {
            instance = new EmployeeServiceImpl();
        }
        return instance;
    }

    private void checkExistUser(Integer empId) throws NotFoundException {
        if (!empDAO.exitsById(empId)) {
            throw new NotFoundException("Employee not found.");
        }
    }

    @Override
    public EmployeeOutGoingDto save(EmployeeIncomingDto empDto) {
        Employee employee = empDAO.save(empDtoMapper.map(empDto));
        return empDtoMapper.map(empDAO.findById(employee.getId()).orElse(employee));
    }

    @Override
    public void update(EmployeeUpdateDto empDto) throws NotFoundException {
        if (empDto == null || empDto.getId() == null) {
            throw new IllegalArgumentException();
        }
        checkExistUser(empDto.getId());
        log.info("empDto = "+empDto);
        empDAO.update(empDtoMapper.map(empDto));
    }

    @Override
    public EmployeeOutGoingDto findById(Integer userId) throws NotFoundException {
        checkExistUser(userId);
        Employee user = empDAO.findById(userId).orElseThrow();
        return empDtoMapper.map(user);
    }

    @Override
    public List<EmployeeOutGoingDto> findAll() {
        log.info(" begin findAll()");
        List<Employee> all = empDAO.findAll();
        log.info(" end findAll()"+all);
        return empDtoMapper.map(all);
    }

    @Override
    public void delete(Integer userId) throws NotFoundException {
        checkExistUser(userId);
        empDAO.deleteById(userId);
    }
}
