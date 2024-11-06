package com.mbe.ada.model.person.dto;

import java.time.LocalDate;

public record UpdatePersonDTO(String name, String lastname, String email, String cpf, LocalDate birthDate) {}
