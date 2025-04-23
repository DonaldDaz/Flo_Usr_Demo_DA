package com.example.flo_usr_demo;

import com.example.flo_usr_demo.model.User;
import com.example.flo_usr_demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository repo;

    @Test
    void whenDataLoaded_thenFindAllReturnsTwo() {
        List<User> all = repo.findAll();
        assertThat(all).hasSize(3);
    }

    @Test
    void whenSearchByFirstName_thenReturnMatch() {
        List<User> result = repo.findByFirstNameContainingIgnoreCase("alice");
        assertThat(result)
                .extracting(User::getFirstName)
                .containsExactly("Alice");
    }
}
