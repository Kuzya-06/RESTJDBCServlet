package ru.kuznetsov.mapper;

import ru.kuznetsov.dto.RoleIncomingDto;
import ru.kuznetsov.dto.RoleOutGoingDto;
import ru.kuznetsov.dto.RoleUpdateDto;
import ru.kuznetsov.entity.Role;

import java.util.List;

public interface RoleDtoMapper {
    Role map(RoleIncomingDto roleIncomingDto);

    Role map(RoleUpdateDto roleUpdateDto);

    RoleOutGoingDto map(Role role);

    List<RoleOutGoingDto> map(List<Role> roleList);
}
