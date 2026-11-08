package com.belak.scheduletimetable.dto.professor;


import com.belak.scheduletimetable.enumeration.Grade;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.belak.scheduletimetable.dto.user.CreateUserDto;
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class CreateProfessorDto extends CreateUserDto {

    private String specialite;
    private Grade grade ;

    private String nomDepartement ;

}
