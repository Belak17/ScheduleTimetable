package com.belak.scheduletimetable.exception;

import lombok.Getter;

public class CourseAndCodeNotFoundException extends RuntimeException {
    @Getter
    private final String code;
    @Getter
    private final String userId;

    @Getter
    private final String nom ;

    @Getter
    private final String prenom;

    public CourseAndCodeNotFoundException(String coursNonDisponible, String code,String userId, String nom, String prenom) {
        super(coursNonDisponible);
        this.code = code;
        this.userId = userId;
        this.nom = nom;
        this.prenom = prenom;
    }

}
