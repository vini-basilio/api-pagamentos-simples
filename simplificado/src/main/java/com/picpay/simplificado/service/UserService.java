package com.picpay.simplificado.service;

import com.picpay.simplificado.DTO.UserCreatedDTO;
import com.picpay.simplificado.DTO.UserDTO;
import com.picpay.simplificado.domain.user.User;
import com.picpay.simplificado.domain.user.UserType;
import com.picpay.simplificado.exception.ValidateTransaction;
import com.picpay.simplificado.repository.UserRespository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRespository userRespository;
    public void validateTransaction(User sender, BigDecimal amount) throws Exception{
        if(sender.getUserType() == UserType.MERCHART)
            throw new ValidateTransaction("Usuário do tipo lojista não está autorizado a realizar transação");
        if(sender.getBalance().compareTo(amount) < 0)
            throw new ValidateTransaction("Saldo insuficiente");
    }
    public User findUserById(Long id){
        return this.userRespository.findUserById(id).orElseThrow(() -> new ExpressionException("Usuário não encontrado"));
    }
    public void saveUser(User user) {
        this.userRespository.save(user);
    }
    public UserCreatedDTO createUser(UserDTO user) {
        User newUser = new User(user);
        this.saveUser(newUser);
        return new UserCreatedDTO(
                newUser.getFirstName(),
                newUser.getLastName(),
                newUser.getBalance(),
                newUser.getId()
        );
    }

    public List<User> getAllUsers() {
        return this.userRespository.findAll();
    }
}
