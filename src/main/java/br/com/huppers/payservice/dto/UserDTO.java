package br.com.huppers.payservice.dto;

import br.com.huppers.payservice.domain.user.UserType;

import java.math.BigDecimal;

public record UserDTO(String firstName, String lastName, String document, BigDecimal balance, String email,
                      String password, UserType userType) {


}
