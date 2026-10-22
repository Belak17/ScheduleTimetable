package com.belak.scheduletimetable.model;

import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.enumeration.Nationalite;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "user_entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    private Long id;

    //@Column(unique = true, nullable = false)
    //private String cin;

    @Column(nullable = true)
    private String password;

    private String nom;

    private String prenom;

    @Column(unique = true, nullable = false)
    private String userId;


    @Enumerated(EnumType.STRING)
    private Nationalite nationalite;

    private String sexe;

    @Column(unique = true, nullable = false)
    private String email;

    private LocalDate dateNaissance;

    private String isEmailVerified ;

    private String telephone;

//    private String villeNaissance;
//
//    private String villeNaissanceArabe;
//
//    private String adresse;
//
//    private String codePostal;
//
//    private String ville;



    @Enumerated(EnumType.STRING)
    private Departement department;

    private String codeDepartement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(nullable = false)
    private boolean enabled = false;

    @Column(nullable = false)
    private boolean locked = false;


    public enum Role {
        USER,
        ADMIN,
        PROFESSOR,
        ADMINISTRATOR,
        STUDENT
    }


    public User(String userId, String email, String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;
    }


    // =========================
    // Spring Security
    // =========================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}