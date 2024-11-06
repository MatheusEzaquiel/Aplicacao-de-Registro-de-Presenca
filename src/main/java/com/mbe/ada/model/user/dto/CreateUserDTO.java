package com.mbe.ada.model.user.dto;

import java.time.LocalDate;

public record CreateUserDTO(String name, String lastname, String email, String cpf, LocalDate birthDate, String password) {

}
