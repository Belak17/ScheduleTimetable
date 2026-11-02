package com.belak.scheduletimetable.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegister {

    @NotBlank(message = "Le nom est obligatoire")
    private String userId;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    public void setUserId(String userId) {
        this.userId = userId != null ? userId.trim() : null;
    }

    public void setPassword(String password) {
        this.password = password != null ? password.trim() : null;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.trim() : null;
    }
}