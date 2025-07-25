package com.pagamentos.simplificado.repository;

import com.pagamentos.simplificado.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRespository extends JpaRepository<User, Long> {
    Optional<User> findUserById(UUID id);
    Optional<User> findUserByCpf(String cpf);
    Optional<User> findUserByEmail(String email);
}
