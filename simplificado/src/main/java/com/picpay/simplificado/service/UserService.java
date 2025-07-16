package com.picpay.simplificado.service;

import com.picpay.simplificado.DTO.user.UserDTO;
import com.picpay.simplificado.DTO.user.UserRequestDTO;
import com.picpay.simplificado.domain.user.User;
import com.picpay.simplificado.domain.user.UserType;
import com.picpay.simplificado.exception.UserCreationException;
import com.picpay.simplificado.exception.ValidateTransactionException;
import com.picpay.simplificado.repository.UserRespository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRespository userRespository;
    // Apenas para demonstração!
    // Não é uma configuração de segurança real
    @Autowired
    private PasswordEncoder encoder;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception{
        if(sender.getUserType() == UserType.MERCHART)
            throw new ValidateTransactionException("Usuário do tipo lojista não está autorizado a realizar transação",  HttpStatus.FORBIDDEN.value());
        if(sender.getBalance().compareTo(amount) < 0)
            throw new ValidateTransactionException("Saldo insuficiente de: id " + sender.getId(), HttpStatus.FORBIDDEN.value());
    }

    public User findUserById(UUID id){
        return this.userRespository.findUserById(id).orElseThrow(() -> new ExpressionException("Usuário não encontrado"));
    }

    public void saveUser(User user) {
        this.userRespository.save(user);
    }

    public UserDTO createUser(UserRequestDTO user) {
        var document = this.userRespository.findUserByCpf(user.document());
        if(document.isPresent()) throw new UserCreationException("Problemas com o documento cadastrado");

        var email = this.userRespository.findUserByEmail(user.email());
        if(email.isPresent()) throw new UserCreationException("Problemas com o e-mail cadastrado");

        User newUser = new User(user);

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
