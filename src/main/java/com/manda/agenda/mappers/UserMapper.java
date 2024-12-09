package com.manda.agenda.mappers;

import org.mapstruct.Mapper;

import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.manda.agenda.dto.UserDTO;
import com.manda.agenda.models.User1;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserDTO toUserDTO(User1 user1);

    User1 toUser(UserDTO userDTO);
}
