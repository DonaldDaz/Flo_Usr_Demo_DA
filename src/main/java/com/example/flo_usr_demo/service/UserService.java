package com.example.flo_usr_demo.service;

import com.example.flo_usr_demo.dto.UserCreateDto;
import com.example.flo_usr_demo.dto.UserDto;
import com.example.flo_usr_demo.exception.NotFoundException;
import com.example.flo_usr_demo.mapper.UserMapper;
import com.example.flo_usr_demo.model.User;
import com.example.flo_usr_demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer for managing {@link User} entities and converting between entities and DTOs.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    /**
     * Create a new User from a DTO.
     *
     * @param dto the create/update DTO
     * @return the created User as a DTO
     */
    public UserDto create(UserCreateDto dto) {
        User entity = UserMapper.toEntity(dto);
        User saved = repo.save(entity);
        return UserMapper.toDto(saved);
    }

    /**
     * Update an existing User with values from a DTO.
     *
     * @param id  the ID of the User to update
     * @param dto the DTO carrying updated values
     * @return the updated User as a DTO
     * @throws NotFoundException if no User with the given ID is found
     */
    public UserDto update(Long id, UserCreateDto dto) {
        User existing = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setAddress(dto.getAddress());
        User saved = repo.save(existing);
        return UserMapper.toDto(saved);
    }

    /**
     * Delete a User by its ID.
     *
     * @param id the ID of the User to delete
     */
    public void delete(Long id) {
        repo.deleteById(id);
    }

    /**
     * Retrieve a User by its ID.
     * If the User is not found, an empty Optional is returned.
     * If the User is found, map method maps it with the method UserMapper.toDto and returns an Optional containing the User DTO.
     *
     * @param id the ID of the User
     * @return an Optional containing the User DTO if found
     */
    public Optional<UserDto> get(Long id) {
        return repo.findById(id).map(UserMapper::toDto);
    }

    /**
     * List all Users.
     * Stream method converts list to a Java Stream, map maps the objects with UserMapper.toDto and Collect converts the stream into a List of User DTOs.
     *
     * @return a List of User DTOs
     */
    public List<UserDto> listAll() {
        return repo.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Search Users by first and/or last name (case-insensitive, partial match).
     *
     * @param firstName optional first name fragment
     * @param lastName  optional last name fragment
     * @return a List of matching User DTOs
     */
    public List<UserDto> search(String firstName, String lastName) {
        List<User> results;
        if (firstName != null && lastName != null) {
            results = repo.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName, lastName);
        } else if (firstName != null) {
            results = repo.findByFirstNameContainingIgnoreCase(firstName);
        } else if (lastName != null) {
            results = repo.findByLastNameContainingIgnoreCase(lastName);
        } else {
            results = repo.findAll();
        }
        return results.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Bulk save Users from a list of DTOs (useful for CSV import).
     *
     * @param dtos the list of UserCreateDto
     * @return a List of saved User DTOs
     */
    @Transactional
    public List<UserDto> saveAll(List<UserCreateDto> dtos) {
        List<User> entities = dtos.stream()
                .map(UserMapper::toEntity)
                .collect(Collectors.toList());
        List<User> saved = repo.saveAll(entities);
        return saved.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all users whose email ends with the given domain.
     *
     * @param domain the domain to filter by (e.g., "@gmail.com")
     * @return list of matching UserDto
     */
    public List<UserDto> getUsersByEmailDomain(String domain) {
        return repo.findByEmailDomain(domain).stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
}
