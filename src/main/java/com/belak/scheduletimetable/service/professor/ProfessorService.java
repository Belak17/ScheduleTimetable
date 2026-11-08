package com.belak.scheduletimetable.service.professor;

import com.belak.scheduletimetable.component.ProfessorDetailsMapper;
import com.belak.scheduletimetable.component.ProfessorMapper;
import com.belak.scheduletimetable.component.ProfessorProfileMapper;
import com.belak.scheduletimetable.dto.professor.ProfessorDetailsDto;
import com.belak.scheduletimetable.dto.professor.ProfessorDto;
import com.belak.scheduletimetable.dto.professor.ProfessorImportDto;
import com.belak.scheduletimetable.dto.professor.ProfessorProfileDto;
import com.belak.scheduletimetable.exception.ResourceNotFoundException;
import com.belak.scheduletimetable.model.Professor;
import com.belak.scheduletimetable.repository.professor.ProfessorRepository;
import com.belak.scheduletimetable.request.UpdateRequest;
import com.belak.scheduletimetable.service.UtilsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
 @RequiredArgsConstructor
public class ProfessorService implements  ProfessorInterfaceService  {
    private  final ProfessorRepository professorRepository ;
    private  final PasswordEncoder passwordEncoder ;
    private  final UtilsService utilsService ;
    private  final ProfessorMapper professorMapper ;
    private  final ProfessorProfileMapper profileMapper ;
    private  final ProfessorDetailsMapper detailsMapper  ;

    public Page<ProfessorDto> getAllProfessor(int page , int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return professorRepository.findAll(pageable).map(professorMapper::convertToDto);
    }

    public Page<ProfessorDto> getAllProfessorBySpecialite(int page , int size , String specialite)
    {
        Pageable pageable = PageRequest.of(page, size);
        return professorRepository.findByNomDepartementIgnoreCase(specialite,pageable).map(professorMapper::convertToDto);
    }

    public void saveProfessor(ProfessorImportDto professorDto)
    {
        if (professorRepository.findByUserId(professorDto.getUserId()).isPresent())
        {
            throw new ResourceNotFoundException("Professor already exists");
        }
        Professor professor = new Professor(professorDto);
        professorRepository.save(professor);
    }


      public void updateProfessorPassword(UpdateRequest request)
      {
          Professor professor = professorRepository.findByUserId(request.getUserId()).get();
          professor.setPassword(passwordEncoder.encode(request.getPassword()));
          professorRepository.save(professor);
      }
    public ProfessorProfileDto findByUserId(String userId)
    {
        log.info("Acces Profile du Professeur Identifiant {}",userId);
        return profileMapper.convertToProfessorProfileDto(professorRepository
                .findByUserId(userId).get());
    }

    public void updateEmail(String nouvEmail, String confEmail, String userId) {
        if (!nouvEmail.equals(confEmail)) {
            throw new IllegalArgumentException("Les emails ne correspondent pas");
        }

        Professor professor = professorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Professeur introuvable"));

        professor.setEmail(nouvEmail);
        professorRepository.save(professor);
    }

    public void updateInfo(String userId, String telephone) {
        Professor professor = professorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Professeur introuvable"));
        professor.setUserId(userId);

        professor.setTelephone(telephone);

        professorRepository.save(professor);
    }

    public void updatePassword(String oldPassword,
                               String newPassword,
                               String confPassword,
                               String userId) {

        Professor professor = professorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Professeur introuvable"));

        if (!passwordEncoder.matches(oldPassword, professor.getPassword())) {
            log.info("Ancien Mot de passe Incorrect pour Professeur {}",professor.getUserId());
            throw new IllegalArgumentException("Ancien mot de passe incorrect");
        }

        if (!newPassword.equals(confPassword)) {
            log.info("Mots de Passe Non Correspondant Pour Professeur {}",professor.getUserId());
            throw new IllegalArgumentException("Les mots de passe ne correspondent pas");
        }

        professor.setPassword(passwordEncoder.encode(newPassword));
        professorRepository.save(professor);
    }

    @Override
    @Transactional
    public void updateProfessor(Long id, ProfessorDetailsDto professorDto) {

        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Professeur introuvable avec l'identifiant : " + id
                ));

        // Informations personnelles
        professor.setNom(professorDto.getNom());
        professor.setPrenom(professorDto.getPrenom());
        professor.setEmail(professorDto.getEmail());
        professor.setTelephone(professorDto.getTelephone());
        professor.setSexe(professorDto.getSexe());
        professor.setDateNaissance(professorDto.getDateNaissance());
        professor.setNationalite(professorDto.getNationalite());

        // Informations professionnelles
        professor.setGrade(professorDto.getGrade());
        professor.setDepartment(professorDto.getDepartement());

        // État du compte
        professor.setEnabled(professorDto.isEnabled());

        professorRepository.save(professor);
    }

    public List<String> getAllNameDepartement()
    {
        return  professorRepository.findDistinctNomDepartements();
    }

    public ProfessorDetailsDto getProfessorDetailsDto(Long id )
    {
        return detailsMapper.convertToProfessorDetailsDto(
                professorRepository.findByIdUser(id)
        );
    }

}
