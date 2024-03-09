package ru.kuznetsov.service.impl;


import ru.kuznetsov.dao.RoleDAO;
import ru.kuznetsov.dao.inter.RoleRepository;
import ru.kuznetsov.dto.RoleIncomingDto;
import ru.kuznetsov.dto.RoleOutGoingDto;
import ru.kuznetsov.dto.RoleUpdateDto;
import ru.kuznetsov.entity.Role;
import ru.kuznetsov.exception.NotFoundException;
import ru.kuznetsov.mapper.RoleDtoMapper;
import ru.kuznetsov.mapper.impl.RoleDtoMapperImpl;
import ru.kuznetsov.service.RoleService;

import java.util.List;

public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository = RoleDAO.getInstance();
    private static RoleService instance;
    private final RoleDtoMapper roleDtoMapper = RoleDtoMapperImpl.getInstance();


    private RoleServiceImpl() {
    }

    public static synchronized RoleService getInstance() {
        if (instance == null) {
            instance = new RoleServiceImpl();
        }
        return instance;
    }

    @Override
    public RoleOutGoingDto save(RoleIncomingDto roleDto) {
        Role role = roleDtoMapper.map(roleDto);
        role = roleRepository.save(role);
        return roleDtoMapper.map(role);
    }

    @Override
    public void update(RoleUpdateDto roleUpdateDto) throws NotFoundException {
        checkRoleExist(roleUpdateDto.getId());
        Role role = roleDtoMapper.map(roleUpdateDto);
        roleRepository.update(role);
    }

    @Override
    public RoleOutGoingDto findById(Integer roleId) throws NotFoundException {
        Role role = roleRepository.findById(roleId).orElseThrow(() ->
                new NotFoundException("Role not found."));
        return roleDtoMapper.map(role);
    }

    @Override
    public List<RoleOutGoingDto> findAll() {
        List<Role> roleList = roleRepository.findAll();
        return roleDtoMapper.map(roleList);
    }

    @Override
    public boolean delete(Integer roleId) throws NotFoundException {
        checkRoleExist(roleId);
        return roleRepository.deleteById(roleId);
    }

    private void checkRoleExist(Integer roleId) throws NotFoundException {
        if (!roleRepository.exitsById(roleId)) {
            throw new NotFoundException("Role not found.");
        }
    }
}
