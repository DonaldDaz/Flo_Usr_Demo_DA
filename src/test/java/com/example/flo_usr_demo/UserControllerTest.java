package com.example.flo_usr_demo;

import com.example.flo_usr_demo.dto.UserCreateDto;
import com.example.flo_usr_demo.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for UserController.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    // Spring class to simulate controller calls
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void list_allUsers_returnsSeededData() throws Exception {
        mvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].firstName").exists());
    }

    @Test
    void get_existingUser_returnsUser() throws Exception {
        // We assume there is an Alice in db with id 1
        mvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Alice"));
    }

    @Test
    void get_nonexistentUser_returns404() throws Exception {
        mvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_and_delete_user_flow() throws Exception {
        UserCreateDto create = new UserCreateDto(
                "Test", "User", "test.user@example.com", "Test Address"
        );
        // Create
        String body = mapper.writeValueAsString(create);
        String json = mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto created = mapper.readValue(json, UserDto.class);
        assertThat(created.getEmail()).isEqualTo("test.user@example.com");

        // Delete
        mvc.perform(delete("/api/users/" + created.getId()))
                .andExpect(status().isNoContent());

        // Verify gone
        mvc.perform(get("/api/users/" + created.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_existingUser_updatesCorrectly() throws Exception {
        // Preliminar Creation
        UserCreateDto create = new UserCreateDto(
                "Up", "Date", "update@example.com", "Old Addr"
        );
        String content = mapper.writeValueAsString(create);
        String resp = mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andReturn()
                .getResponse()
                .getContentAsString();
        UserDto created = mapper.readValue(resp, UserDto.class);

        // Update
        UserCreateDto update = new UserCreateDto(
                "Up", "Date", "update2@example.com", "New Addr"
        );
        String updatedJson = mapper.writeValueAsString(update);
        mvc.perform(put("/api/users/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("update2@example.com"))
                .andExpect(jsonPath("$.address").value("New Addr"));
    }

    @Test
    void uploadCsv_validFile_importsData() throws Exception {
        String csv = "Name,Surname,Email,Adress\nCsv,User,csv.user@example.com,Csv Addr\n";
        MockMultipartFile file = new MockMultipartFile(
                "file", "users.csv", "text/csv", csv.getBytes()
        );

        mvc.perform(multipart("/api/users/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("csv.user@example.com"));
    }

    @Test
    void uploadCsv_malformedFile_returnsBadRequest() throws Exception {
        String badCsv = "Name,Surname,Email,Adress\nbad,cols\n";
        MockMultipartFile file = new MockMultipartFile(
                "file", "bad.csv", "text/csv", badCsv.getBytes()
        );

        mvc.perform(multipart("/api/users/upload").file(file))
                .andExpect(status().isBadRequest());
    }
}
