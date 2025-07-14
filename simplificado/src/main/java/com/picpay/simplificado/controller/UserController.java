package com.picpay.simplificado.controller;

import com.picpay.simplificado.DTO.UserCreatedDTO;
import com.picpay.simplificado.DTO.UserDTO;
import com.picpay.simplificado.domain.user.User;
import com.picpay.simplificado.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserCreatedDTO> createUser(@RequestBody UserDTO user) {
        var newUSer = userService.createUser(user);
        return new ResponseEntity<>(newUSer, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        var allUsers = this.userService.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

}
