package com.belak.scheduletimetable.service.professor;

import com.belak.scheduletimetable.dto.professor.ProfessorDetailsDto;
import com.belak.scheduletimetable.dto.professor.ProfessorDto;
import com.belak.scheduletimetable.dto.professor.ProfessorImportDto;
import com.belak.scheduletimetable.dto.professor.ProfessorProfileDto;
import com.belak.scheduletimetable.request.UpdateRequest;
import org.springframework.data.domain.Page;

public interface ProfessorInterfaceService {
    public Page<ProfessorDto> getAllProfessor(int page , int size) ;

    public Page<ProfessorDto> getAllProfessorBySpecialite(int page , int size , String department) ;


    public void saveProfessor(ProfessorImportDto professorDto) ;

    public void updateProfessorPassword(UpdateRequest request) ;

    public ProfessorProfileDto findByUserId(String userId) ;

    public void updateEmail(String nouvEmail, String confEmail, String userId) ;


    public void updateInfo(String userId ,String telephone) ;


    public void updatePassword(String oldPassword,
                               String newPassword,
                               String confPassword,
                               String userId) ;

    void updateProfessor(Long id, ProfessorDetailsDto professorDto);

    public ProfessorDetailsDto getProfessorDetailsDto(Long id );
}
