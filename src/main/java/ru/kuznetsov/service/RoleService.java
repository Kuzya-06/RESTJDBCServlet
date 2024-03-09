package ru.kuznetsov.service;

import ru.kuznetsov.dto.RoleIncomingDto;
import ru.kuznetsov.dto.RoleOutGoingDto;
import ru.kuznetsov.dto.RoleUpdateDto;
import ru.kuznetsov.exception.NotFoundException;

import java.util.List;

public interface RoleService {
    RoleOutGoingDto save(RoleIncomingDto role);

    void update(RoleUpdateDto role) throws NotFoundException;

    RoleOutGoingDto findById(Integer roleId) throws NotFoundException;

    List<RoleOutGoingDto> findAll();

    boolean delete(Integer roleId) throws NotFoundException;
}
