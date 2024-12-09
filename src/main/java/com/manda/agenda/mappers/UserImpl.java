package com.manda.agenda.mappers;

import com.manda.agenda.dto.UserDTO;
import com.manda.agenda.models.User1;

import lombok.Data;

@Data
public class UserImpl implements UserMapper {

    @Override
    public UserDTO toUserDTO(User1 user) {
        // TODO Auto-generated method stub
        return new UserDTO(user.getId(), user.getLastName(), user.getFirstName(), user.getUsername(),
                user.getPassword(),
                user.getRole(), user.getWhoCreated(), user.getDateCreated(),
                user.getWhoModified(),
                user.getDateModified(), user.getFirstConnection(), user.getStatut());
    }

    @Override
    public User1 toUser(UserDTO userDTO) {
        // TODO Auto-generated method stub
        return new User1(userDTO.getId(), userDTO.getLastName(), userDTO.getFirstName(), userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getRole(), userDTO.getWhoCreated(),
                userDTO.getDateCreated(),
                userDTO.getWhoModified(),
                userDTO.getDateModified(), userDTO.getFirstConnection(), userDTO.getStatut());
    }

}
