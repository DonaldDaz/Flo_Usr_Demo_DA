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
     */
    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        return svc.get(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    /**
     * Create a new user.
     *
     * @param dto the DTO representing the new user
     * @return 201 with created user DTO
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
     */
    @PostMapping("/upload")
    public ResponseEntity<List<UserDto>> uploadCsv(@RequestParam("file") MultipartFile file) {
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csv = new CSVReader(reader)) {

            List<UserCreateDto> dtos = new ArrayList<>();
            String[] line;
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
}
