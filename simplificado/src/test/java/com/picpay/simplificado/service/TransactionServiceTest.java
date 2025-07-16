package com.picpay.simplificado.service;

import com.picpay.simplificado.DTO.transaction.TransactionRequestDTO;
import com.picpay.simplificado.domain.user.User;
import com.picpay.simplificado.domain.user.UserType;
import com.picpay.simplificado.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;

class TransactionServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private NotificationService notificationService;

    @Autowired
    @InjectMocks
    TransactionService transactionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void createTransactionSucess() throws Exception {

        User sender = new User();
        sender.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        sender.setFirstName("João");
        sender.setLastName("Silva");
        sender.setCpf("12345678901");
        sender.setEmail("joao.silva@example.com");
        sender.setPassword("senhaSegura123");
        sender.setBalance(new BigDecimal("1000.00"));
        sender.setUserType(UserType.COMMON);

        User receiver = new User();
        receiver.setId(UUID.fromString("22222222-2222-2222-2222-222222222222"));
        receiver.setFirstName("Maria");
        receiver.setLastName("Oliveira");
        receiver.setCpf("98765432100");
        receiver.setEmail("maria.oliveira@example.com");
        receiver.setPassword("outraSenha456");
        receiver.setBalance(new BigDecimal("2500.50"));
        receiver.setUserType(UserType.COMMON);


        when(userService.findUserById(
                        UUID.fromString("11111111-1111-1111-1111-111111111111")
                )
        ).thenReturn(sender);

        when(userService.findUserById(
                        UUID.fromString("22222222-2222-2222-2222-222222222222")
                )
        ).thenReturn(receiver);

        TransactionRequestDTO transaction = new TransactionRequestDTO(
                new BigDecimal("100.00"),
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                UUID.fromString("22222222-2222-2222-2222-222222222222")
        );

        var response = transactionService.createTransaction(transaction);

        sender.setBalance(sender.getBalance().subtract(new BigDecimal("100.00")));
        receiver.setBalance(receiver.getBalance().add(new BigDecimal("100.00")));

        // testa se o saldo do sender está correto
        assertEquals(new BigDecimal("900.00"), response.sender().balance());
        assertEquals(new BigDecimal("900.00"), sender.getBalance());
        // testa se o saldo do receiver está correto
        assertEquals(new BigDecimal("2600.50"), response.receiver().balance());
        assertEquals(new BigDecimal("2600.50"), receiver.getBalance());

        // testa se o banco foi chamado de forma correta
        verify(transactionRepository, times(1)).save(any());
        verify(userService, times(2)).saveUser(any());
        verify(userService).saveUser(sender);
        verify(userService).saveUser(receiver);
    }
}