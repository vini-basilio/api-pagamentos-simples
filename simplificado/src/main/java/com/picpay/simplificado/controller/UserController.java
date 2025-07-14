package com.picpay.simplificado.controller;

import com.picpay.simplificado.DTO.UserDTO;
import com.picpay.simplificado.DTO.UserCreationDTO;
import com.picpay.simplificado.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreationDTO user) {
        var newUser = userService.createUser(user);
        log.info("Usuário criado: " + newUser.toString() + " " +  LocalDateTime.now());
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        var allUsers = this.userService.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }
}
