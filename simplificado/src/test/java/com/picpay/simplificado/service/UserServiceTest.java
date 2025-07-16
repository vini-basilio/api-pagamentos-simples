package com.picpay.simplificado.service;

import com.picpay.simplificado.DTO.user.UserDTO;
import com.picpay.simplificado.DTO.user.UserRequestDTO;
import com.picpay.simplificado.domain.user.User;
import com.picpay.simplificado.domain.user.UserType;
import com.picpay.simplificado.exception.UserCreationException;
import com.picpay.simplificado.exception.ValidateTransactionException;
import com.picpay.simplificado.repository.UserRespository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceTest {
    @Mock
    private UserRespository  userRespository;
    @Mock
    private PasswordEncoder enconder;

    @Autowired
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void createUserSucess() {
        var userRequestDTO = new UserRequestDTO(
                "João",
                "Silva",
                "12345678901",
                new BigDecimal("1000.00"),
                "joao.silva@example.com",
                "senhaSegura123",
                UserType.COMMON
        );

        when(userRespository.findUserByCpf(userRequestDTO.cpf())).thenReturn(Optional.empty());
        when(userRespository.findUserByEmail(userRequestDTO.email())).thenReturn(Optional.empty());
        when(enconder.encode(userRequestDTO.password())).thenReturn(userRequestDTO.password());

        var user = userService.createUser(userRequestDTO);

        assertEquals(user, new UserDTO(
                null,
                "João",
                "Silva",
                new BigDecimal("1000.00"),
                "joao.silva@example.com",
                UserType.COMMON));
    }


    @Test
    void createUserCpf() {
        var userRequestDTO = new UserRequestDTO(
                "João",
                "Silva",
                "12345678901",
                new BigDecimal("1000.00"),
                "joao.silva@example.com",
                "senhaSegura123",
                UserType.COMMON
        );

        when(userRespository.findUserByCpf(userRequestDTO.cpf())).thenReturn(Optional.of(new User()));

        assertThrows(UserCreationException.class, () -> {
            userService.createUser(userRequestDTO);
        });
    }

    @Test
    void createUserEmail() {
        var userRequestDTO = new UserRequestDTO(
                "João",
                "Silva",
                "12345678901",
                new BigDecimal("1000.00"),
                "joao.silva@example.com",
                "senhaSegura123",
                UserType.COMMON
        );

        when(userRespository.findUserByEmail(userRequestDTO.email())).thenReturn(Optional.of(new User()));

        assertThrows(UserCreationException.class, () -> {
            userService.createUser(userRequestDTO);
        });
    }

    @Test
    void createUserFindIdFail() {
        UUID uuid = UUID.randomUUID();

        when(userRespository.findUserById(uuid)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            userService.findUserById(uuid);
        });
    }

    @Test
    void validateTransactionFailType() {
        User sender = new User();
        sender.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        sender.setFirstName("João");
        sender.setLastName("Silva");
        sender.setCpf("12345678901");
        sender.setEmail("joao.silva@example.com");
        sender.setPassword("senhaSegura123");
        sender.setBalance(new BigDecimal("1000.00"));
        sender.setUserType(UserType.MERCHART);

        assertThrows(ValidateTransactionException.class, () -> {
            userService.validateTransaction(sender, new BigDecimal("1000.00"));
        });
    }

    @Test
    void validateTransactionFailAmount() {
        User sender = new User();
        sender.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        sender.setFirstName("João");
        sender.setLastName("Silva");
        sender.setCpf("12345678901");
        sender.setEmail("joao.silva@example.com");
        sender.setPassword("senhaSegura123");
        sender.setBalance(new BigDecimal("1000.00"));
        sender.setUserType(UserType.MERCHART);

        assertThrows(ValidateTransactionException.class, () -> {
            userService.validateTransaction(sender, new BigDecimal("1000.01"));
        });
    }
}