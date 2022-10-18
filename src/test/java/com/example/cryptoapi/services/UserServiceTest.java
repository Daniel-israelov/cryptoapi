package com.example.cryptoapi.services;

import com.example.cryptoapi.assemblers.UserDtoAssembler;
import com.example.cryptoapi.entities.UserEntity;
import com.example.cryptoapi.repositories.UserRepository;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith({MockitoExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    @Autowired
    private UserRepository repository;
    @Mock
    private UserDtoAssembler assembler;
    private UserService service;
    private UserEntity entity1;
    private UserEntity entity2;

    @BeforeEach
    void setup() {
        entity1 = createEntity(999L, "niv", "bar", true, 30);
        entity2 = createEntity(888L, "ofir", "harera", false, 24);
        service = new UserService(repository, assembler);
    }

    @Test
    @DisplayName("User should not exist in db")
    @Order(1)
    void shouldNotExist() {
        assertThat(repository.existsByIdentityNumber(entity1.getIdentityNumber())).isFalse();
        assertThat(repository.existsByIdentityNumber(entity2.getIdentityNumber())).isFalse();
    }

    @Test
    @DisplayName("Should create user and if saved to db")
    @Order(2)
    void shouldCreateUserAndCheckIfSaved() {
        service.createUser(entity1);
        service.createUser(entity2);

        checkIfSavedToDb(entity1);
        checkIfSavedToDb(entity2);
    }

    @Test
    @DisplayName("Should find all users")
    void shouldReturnAllUsers() {
        assertEquals(StreamSupport
                .stream(repository.findAll().spliterator(),
                        false).count(), 2);
    }

    private void checkIfSavedToDb(UserEntity entity) {
        assertAll(
                () -> assertThat(repository.findByIdentityNumber(entity.getIdentityNumber())).isNotNull(),
                () -> assertThat(repository.existsByIdentityNumber(entity.getIdentityNumber())).isTrue()
        );
    }

    @Contract("_, _, _, _, _ -> new")
    private @NotNull UserEntity createEntity(Long id, String firstName, String lastName, boolean gender, Integer age) {
        return new UserEntity(id, firstName, lastName, gender, age);
    }

    @AfterEach
    void tearDown() {
        repository.delete(entity1);
        repository.delete(entity2);
    }
}