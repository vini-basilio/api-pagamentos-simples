package com.pagamentos.simplificado.domain.user;
import com.pagamentos.simplificado.DTO.user.UserRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String firstName;
    private String lastName;
    @Column (unique = true)
    private String cpf;
    @Column (unique = true)
    private String email;
    private String password;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private UserType userType;

    public User(UserRequestDTO user) {
        this.firstName = user.firstName();
        this.lastName = user.lastName();
        this.cpf = user.document();
        this.balance = user.balance();
        this.userType = user.userType();
        this.password =  user.password();
        this.email = user.email();
    }
}
