package com.belak.scheduletimetable.service.professor;

import com.belak.scheduletimetable.dto.CreateProfessorDto;
import com.belak.scheduletimetable.dto.ProfessorDto;
import com.belak.scheduletimetable.dto.ProfessorProfileDto;
import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.request.UpdateRequest;
import org.springframework.data.domain.Page;

public interface ProfessorInterfaceService {
    public Page<ProfessorDto> getAllProfessor(int page , int size) ;

    public Page<ProfessorDto> getAllProfessorByDepartment(int page , int size , Departement departement) ;


    public void saveProfessor(CreateProfessorDto professorDto) ;

    public void updateProfessorPassword(UpdateRequest request) ;

    public ProfessorProfileDto findByUserId(String userId) ;

    public void updateEmail(String nouvEmail, String confEmail, String userId) ;


    public void updateInfo(String userId, String address, String telephone, String code) ;


    public void updatePassword(String oldPassword,
                               String newPassword,
                               String confPassword,
                               String userId) ;
}
