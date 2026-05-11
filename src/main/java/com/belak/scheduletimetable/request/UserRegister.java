package com.belak.scheduletimetable.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Valid
public class UserRegister {
    @NotBlank(message = "Le nom est obligatoire")
    private  String userId ;
    @NotBlank(message = "Le mot de passe est obligatoire")
    private  String password ;
    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email ;
}
