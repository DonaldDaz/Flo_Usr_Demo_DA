package com.example.flo_usr_demo.mapper;

import com.example.flo_usr_demo.dto.UserCreateDto;
import com.example.flo_usr_demo.dto.UserDto;
import com.example.flo_usr_demo.model.User;

/**
 * Mapper utility for converting between {@link User} entities and their DTO representations.
 */
public class UserMapper {

    /**
     * Converts a {@link UserCreateDto} into a {@link User} entity.
     *
     * @param dto the create/update DTO carrying user input
     * @return a new User entity populated with data from the DTO
     */
    public static User toEntity(UserCreateDto dto) {
        if (dto == null) {
            return null;
        }
        User u = new User();
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setEmail(dto.getEmail());
        u.setAddress(dto.getAddress());
        return u;
    }

    /**
     * Converts a {@link User} entity into a {@link UserDto}.
     *
     * @param u the User entity to convert
     * @return a UserDto with fields copied from the entity
     */
    public static UserDto toDto(User u) {
        if (u == null) {
            return null;
        }
        return new UserDto(
                u.getId(),
                u.getFirstName(),
                u.getLastName(),
                u.getEmail(),
                u.getAddress()
        );
    }
}
