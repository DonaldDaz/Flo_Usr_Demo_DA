package com.example.flo_usr_demo.controller;

import com.example.flo_usr_demo.dto.UserCreateDto;
import com.example.flo_usr_demo.dto.UserDto;
import com.example.flo_usr_demo.exception.NotFoundException;
import com.example.flo_usr_demo.service.UserService;
import com.opencsv.CSVReader;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for managing users through DTOs.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService svc;

    /**
     * Search or list all users.
     *
     * @param firstName optional first name fragment
     * @param lastName  optional last name fragment
     * @return list of matching users as DTOs
     * @example curl -X GET "http://localhost:8080/api/users?firstName=Alice&lastName=User"
     */
    @GetMapping
    public List<UserDto> list(
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "lastName", required = false) String lastName
    ) {
        return svc.search(firstName, lastName);
    }

    /**
     * Retrieve a user by ID.
     *
     * @param id the user ID
     * @return user DTO
     * @throws NotFoundException if user is not found
     * @example curl -X GET "http://localhost:8080/api/users/1"
     */
    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        return svc.get(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    /**
     * Create a new user. Uses Valid to validate the DTO, based on its annotations.
     *
     * @param dto the DTO representing the new user
     * @return 201 with created user DTO
     * @example
     * curl -X POST "http://localhost:8080/api/users"
     * Request Body:
     * {
     *     "firstName": "John",
     *     "lastName": "Doe",
     *     "email": "john.doe@example.com",
     *     "address": "123 Street"
     * }
     */
    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserCreateDto dto) {
        UserDto created = svc.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update an existing user.
     *
     * @param id  the ID of the user to update
     * @param dto the DTO carrying updated values
     * @return updated user DTO
     * @throws NotFoundException if user is not found
     * @example
     * curl -X PUT "http://localhost:8080/api/users/1"
     * Request Body:
     * {
     *     "firstName": "Jane",
     *     "lastName": "Doe",
     *     "email": "jane.doe@example.com",
     *     "address": "456 Avenue"
     * }
     */
    @PutMapping("/{id}")
    public UserDto update(
            @PathVariable Long id,
            @Valid @RequestBody UserCreateDto dto
    ) {
        return svc.update(id, dto);
    }

    /**
     * Delete a user.
     *
     * @param id the ID of the user to delete
     * @example curl -X DELETE "http://localhost:8080/api/users/1"
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        svc.delete(id);
    }

    /**
     * Bulk import users from a CSV file.
     * Expects columns: firstName,lastName,email,address
     *
     * @param file the uploaded CSV file
     * @return list of imported user DTOs
     * @example curl -X POST "http://localhost:8080/api/users/upload" -F "file=@users.csv"
     */
    @PostMapping("/upload")
    public ResponseEntity<List<UserDto>> uploadCsv(@RequestParam("file") MultipartFile file) {
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csv = new CSVReader(reader)) {

            List<UserCreateDto> dtos = new ArrayList<>();
            String[] line;

            //avoid headers
            csv.readNext();

            // csv.readNext() returns null when there are no more lines to read
            while ((line = csv.readNext()) != null) {
                UserCreateDto dto = new UserCreateDto(
                        line[0], // firstName
                        line[1], // lastName
                        line[2], // email
                        line[3]  // address
                );
                dtos.add(dto);
            }
            List<UserDto> saved = svc.saveAll(dtos);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    /**
     * Get all users with emails ending with the specified domain.
     *
     * @param domain the email domain (e.g., "@example.com")
     * @return list of users as DTOs
     * @example curl -X GET "http://localhost:8080/api/users/by-domain?domain=@example.com"
     */
    @GetMapping("/search/by-domain")
    public List<UserDto> getUsersByDomain(@RequestParam String domain) {
        return svc.getUsersByEmailDomain(domain);
    }
}
