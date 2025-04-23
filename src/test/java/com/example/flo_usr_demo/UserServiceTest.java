package com.example.flo_usr_demo;

import com.example.flo_usr_demo.dto.UserCreateDto;
import com.example.flo_usr_demo.dto.UserDto;
import com.example.flo_usr_demo.exception.NotFoundException;
import com.example.flo_usr_demo.mapper.UserMapper;
import com.example.flo_usr_demo.model.User;
import com.example.flo_usr_demo.repository.UserRepository;
import com.example.flo_usr_demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.argThat;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repo;

    private UserCreateDto sampleDto;
    private User sampleEntity;
    private UserDto sampleDtoWithId;

    @BeforeEach
    void setUp() {
        sampleDto = new UserCreateDto("Anna", "Verdi", "anna.verdi@example.com", "Via Milano 1");
        sampleEntity = UserMapper.toEntity(sampleDto);
        sampleEntity.setId(42L);
        sampleDtoWithId = UserMapper.toDto(sampleEntity);
    }

    @Test
    void create_shouldReturnDtoWithId() {
        when(repo.save(any(User.class))).thenReturn(sampleEntity);

        UserDto result = service.create(sampleDto);

        assertThat(result).usingRecursiveComparison()
                .isEqualTo(sampleDtoWithId);
        verify(repo).save(argThat(u ->
                u.getFirstName().equals("Anna") &&
                        u.getLastName().equals("Verdi") &&
                        u.getEmail().equals("anna.verdi@example.com") &&
                        u.getAddress().equals("Via Milano 1")
        ));
    }

    @Test
    void update_existingUser_shouldReturnUpdatedDto() {
        UserCreateDto updateDto = new UserCreateDto("Anna", "Rossi", "anna.rossi@example.com", "Via Roma 2");
        User existing = UserMapper.toEntity(sampleDto);
        existing.setId(42L);

        User updatedEntity = UserMapper.toEntity(updateDto);
        updatedEntity.setId(42L);

        when(repo.findById(42L)).thenReturn(Optional.of(existing));
        when(repo.save(any(User.class))).thenReturn(updatedEntity);

        UserDto result = service.update(42L, updateDto);

        assertThat(result.getId()).isEqualTo(42L);
        assertThat(result.getLastName()).isEqualTo("Rossi");
        assertThat(result.getEmail()).isEqualTo("anna.rossi@example.com");
    }

    @Test
    void update_nonexistentUser_shouldThrowNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, sampleDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found with id: 99");
    }

    @Test
    void get_existingUser_shouldReturnOptionalDto() {
        when(repo.findById(42L)).thenReturn(Optional.of(sampleEntity));

        Optional<UserDto> result = service.get(42L);

        assertThat(result).isPresent()
                .contains(sampleDtoWithId);
    }

    @Test
    void get_nonexistentUser_shouldReturnEmptyOptional() {
        when(repo.findById(123L)).thenReturn(Optional.empty());

        Optional<UserDto> result = service.get(123L);

        assertThat(result).isEmpty();
    }

    @Test
    void listAll_shouldMapAllEntitiesToDtos() {
        User other = new User();
        other.setId(7L);
        other.setFirstName("Bruno");
        other.setLastName("Bianchi");
        other.setEmail("bruno.bianchi@example.com");
        other.setAddress("Piazza Duomo");

        when(repo.findAll()).thenReturn(List.of(sampleEntity, other));

        List<UserDto> result = service.listAll();

        assertThat(result).hasSize(2)
                .extracting(UserDto::getId)
                .containsExactly(42L, 7L);
    }

    @Test
    void search_withBothParams_shouldDelegateToRepo() {
        when(repo.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase("a", "v"))
                .thenReturn(List.of(sampleEntity));

        List<UserDto> result = service.search("a", "v");

        assertThat(result).hasSize(1)
                .first()
                .extracting(UserDto::getEmail)
                .isEqualTo("anna.verdi@example.com");
    }

    @Test
    void search_withFirstNameOnly_shouldDelegateToRepo() {
        when(repo.findByFirstNameContainingIgnoreCase("Anna"))
                .thenReturn(List.of(sampleEntity));

        List<UserDto> result = service.search("Anna", null);

        assertThat(result).hasSize(1);
    }

    @Test
    void search_withLastNameOnly_shouldDelegateToRepo() {
        when(repo.findByLastNameContainingIgnoreCase("Verdi"))
                .thenReturn(List.of(sampleEntity));

        List<UserDto> result = service.search(null, "Verdi");

        assertThat(result).hasSize(1);
    }

    @Test
    void search_withNoParams_shouldReturnAll() {
        when(repo.findAll()).thenReturn(List.of(sampleEntity));

        List<UserDto> result = service.search(null, null);

        assertThat(result).hasSize(1);
    }

    @Test
    void saveAll_shouldSaveAndReturnListOfDtos() {
        User u1 = new User(); u1.setId(1L); u1.setFirstName("X"); u1.setLastName("Y"); u1.setEmail("x@y.com"); u1.setAddress("A");
        User u2 = new User(); u2.setId(2L); u2.setFirstName("P"); u2.setLastName("Q"); u2.setEmail("p@q.com"); u2.setAddress("B");

        List<UserCreateDto> dtos = List.of(
                new UserCreateDto("X","Y","x@y.com","A"),
                new UserCreateDto("P","Q","p@q.com","B")
        );
        when(repo.saveAll(anyList())).thenReturn(List.of(u1, u2));

        List<UserDto> result = service.saveAll(dtos);

        assertThat(result).hasSize(2)
                .extracting(UserDto::getId)
                .containsExactly(1L, 2L);
        verify(repo).saveAll(argThat((List<User> list) -> list.size() == 2));
    }
}
