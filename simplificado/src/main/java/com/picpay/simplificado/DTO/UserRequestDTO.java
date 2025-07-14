package com.picpay.simplificado.DTO;

import com.picpay.simplificado.domain.user.UserType;
import java.math.BigDecimal;

public record UserRequestDTO(
                      String firstName,
                      String lastName,
                      String document,
                      BigDecimal balance,
                      String email,
                      String password,
                      UserType userType) {
}
