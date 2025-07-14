package com.picpay.simplificado.service;


import com.picpay.simplificado.DTO.AuthDTO;
import com.picpay.simplificado.DTO.TransactionDTO;
import com.picpay.simplificado.domain.transaction.Transaction;
import com.picpay.simplificado.domain.user.User;
import com.picpay.simplificado.exception.ValidateTransactionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Service
public class TransactionService {
    @Autowired
    private UserService userService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.authorization}")
    private String authUrl;

    public Transaction createTransaction(TransactionDTO request) throws Exception {
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
                sender.getBalance()
                .add(request.value()
        ));

        log.info("Nova transação autorizada: " + transaction.toString() );

        return transaction;
    }

    public void authorizeTransaction(User sender, BigDecimal value) throws Exception {
        try {
            var response = restTemplate.getForEntity(authUrl, AuthDTO.class);
        } catch (HttpClientErrorException e) {
            String errorBody = e.getResponseBodyAsString();
            throw new ValidateTransactionException("Transação não autorizada pelo provedor", e.getStatusCode().value());

        } catch (HttpServerErrorException e) {
            throw new ValidateTransactionException("Erro no provedor de autorização", e.getStatusCode().value());
        }
    }
}
