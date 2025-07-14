package com.picpay.simplificado.service;

import com.picpay.simplificado.DTO.UserDTO;
import com.picpay.simplificado.DTO.UserCreationDTO;
import com.picpay.simplificado.domain.user.UserType;
import com.picpay.simplificado.exception.UserCreation;
import com.picpay.simplificado.exception.ValidateTransaction;
import com.picpay.simplificado.repository.UserRespository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRespository userRespository;
    // Apenas para demonstração!
    // Não é uma configuração de segurança real
    @Autowired
    private PasswordEncoder encoder;

    public void validateTransaction(com.picpay.simplificado.domain.user.User sender, BigDecimal amount) throws Exception{
        if(sender.getUserType() == UserType.MERCHART)
            throw new ValidateTransaction("Usuário do tipo lojista não está autorizado a realizar transação");
        if(sender.getBalance().compareTo(amount) < 0)
            throw new ValidateTransaction("Saldo insuficiente");
    }

    public com.picpay.simplificado.domain.user.User findUserById(Long id){
        return this.userRespository.findUserById(id).orElseThrow(() -> new ExpressionException("Usuário não encontrado"));
    }

    public void saveUser(com.picpay.simplificado.domain.user.User user) {
        this.userRespository.save(user);
    }

    public UserDTO createUser(UserCreationDTO user) {
        this.userRespository
                .findUserByCpf(user.document())
                .ifPresent((cpf) -> new UserCreation("Problemas com o documento cadastrado"));

        this.userRespository
                .findUserByEmail(user.document())
                .ifPresent((email) -> new UserCreation("Problemas com o e-mail cadastrado"));

        com.picpay.simplificado.domain.user.User newUser = new com.picpay.simplificado.domain.user.User(user);

        newUser.setPassword(encoder.encode(newUser.getPassword()));

        this.saveUser(newUser);

        return new UserDTO(
                newUser.getId(),
                newUser.getFirstName(),
                newUser.getLastName(),
                newUser.getBalance(),
                newUser.getEmail(),
                newUser.getUserType()
        );
    }

    public List<UserDTO> getAllUsers() {
        return this.userRespository.findAll()
                .stream()
                .map( user -> new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBalance(),
                user.getEmail(),
                user.getUserType()
        )).toList();
    }
}
