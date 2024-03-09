package ru.kuznetsov.mapper.impl;

import ru.kuznetsov.dto.RoleIncomingDto;
import ru.kuznetsov.dto.RoleOutGoingDto;
import ru.kuznetsov.dto.RoleUpdateDto;
import ru.kuznetsov.entity.Role;
import ru.kuznetsov.mapper.RoleDtoMapper;

import java.util.List;

public class RoleDtoMapperImpl implements RoleDtoMapper {
    private static RoleDtoMapper instance;

    private RoleDtoMapperImpl() {
    }

    public static synchronized RoleDtoMapper getInstance() {
        if (instance == null) {
            instance = new RoleDtoMapperImpl();
        }
        return instance;
    }

    @Override
    public Role map(RoleIncomingDto roleIncomingDto) {
        return new Role(
                null,
                roleIncomingDto.getName()
        );
    }

    @Override
    public Role map(RoleUpdateDto roleUpdateDto) {
        return new Role(
                roleUpdateDto.getId(),
                roleUpdateDto.getName());
    }

    @Override
    public RoleOutGoingDto map(Role role) {
        return new RoleOutGoingDto(
                role.getId(),
                role.getName()
        );
    }

    @Override
    public List<RoleOutGoingDto> map(List<Role> roleList) {
        return roleList.stream().map(this::map).toList();
    }
}
