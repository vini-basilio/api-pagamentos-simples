package com.picpay.simplificado.domain.user;
import com.picpay.simplificado.DTO.UserRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
