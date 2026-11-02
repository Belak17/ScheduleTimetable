package com.belak.scheduletimetable.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfessorRegister {

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    public void setEmail(String email) {
        this.email = email != null ? email.trim() : null;
    }

    public void setPassword(String password) {
        this.password = password != null ? password.trim() : null;
    }
}