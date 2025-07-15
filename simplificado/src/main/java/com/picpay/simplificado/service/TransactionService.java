package com.picpay.simplificado.service;


import com.picpay.simplificado.DTO.AuthDTO;
import com.picpay.simplificado.DTO.transaction.TransactionDTO;
import com.picpay.simplificado.DTO.transaction.TransactionRequestDTO;
import com.picpay.simplificado.DTO.user.UserDTO;
import com.picpay.simplificado.domain.transaction.Transaction;
import com.picpay.simplificado.domain.user.User;
import com.picpay.simplificado.exception.ValidateTransactionException;
import com.picpay.simplificado.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Service
@Transactional
public class TransactionService {
    @Autowired
    private UserService userService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private NotificationService notificationService;
    @Value("${api.authorization}")
    private String authUrl;
    @Autowired
    private TransactionRepository transactionRepository;


    public TransactionDTO createTransaction(TransactionRequestDTO request) throws Exception {
        var sender = this.userService.findUserById(request.senderId());
        var receiver = this.userService.findUserById(request.receiverId());

        userService.validateTransaction(sender, request.value());
        this.authorizeTransaction(sender, request.value());

        Transaction transaction = new Transaction();

        transaction.setAmount(request.value());
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDate.now());

        sender.setBalance(
                sender.getBalance()
                .subtract(request.value()
        ));

        receiver.setBalance(
                receiver.getBalance()
                .add(request.value()
        ));

        processTransaction(transaction, sender, receiver);

        UserDTO senderDTO = new UserDTO(sender.getId(),
                sender.getFirstName(),
                sender.getLastName(),
                sender.getBalance(),
                sender.getEmail(),
                sender.getUserType());

        UserDTO receiverDTO = new UserDTO(receiver.getId(),
                receiver.getFirstName(),
                receiver.getLastName(),
                receiver.getBalance(),
                receiver.getEmail(),
                receiver.getUserType());


        handleTransactionCompleted(transaction, sender, receiver);
        return new TransactionDTO(transaction.getId(), senderDTO, receiverDTO);
    }

    @Transactional
    public void processTransaction(Transaction transaction, User sender, User receiver) {
        transactionRepository.save(transaction);
        userService.saveUser(sender);
        userService.saveUser(receiver);
    }
    @Async
    public void handleTransactionCompleted(Transaction transaction, User sender, User receiver) {
        log.info("Nova transação autorizada: " + transaction.getId());
        notificationService.sendNotification(sender.getId(), "Transação realizada com sucesso");
        notificationService.sendNotification(receiver.getId(), "Transação recebida com sucesso");
    }

    public void authorizeTransaction(User sender, BigDecimal value) throws Exception {
        try {
            var response = restTemplate.getForEntity(authUrl, AuthDTO.class);
        } catch (HttpClientErrorException e) {
            throw new ValidateTransactionException("Transação não autorizada pelo provedor", e.getStatusCode().value());

        } catch (HttpServerErrorException e) {
            throw new ValidateTransactionException("Erro no provedor de autorização", e.getStatusCode().value());
        }
    }
}
