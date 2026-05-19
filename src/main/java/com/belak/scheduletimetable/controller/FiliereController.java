package com.belak.scheduletimetable.controller;

import com.belak.scheduletimetable.enumeration.Departement;
import com.belak.scheduletimetable.enumeration.Filiere;
import com.belak.scheduletimetable.service.timetable.grouptimetable.GroupTimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/filieres")
@RequiredArgsConstructor
public class FiliereController {
    private final GroupTimetableService groupTimetableService;
    @GetMapping("/departements/{departement}")
    public String showAllFieldByDepartment(@PathVariable String departement ,
                                           Model model)
    {
        model.addAttribute("departement", departement);
        List<Filiere> licences = groupTimetableService.getLicenceFilieresByDepartement(Departement.valueOf(departement.trim().toUpperCase()));
        model.addAttribute("licences", licences);
        List<Filiere> masters = groupTimetableService.getMasterFilieresByDepartement(Departement.valueOf(departement.trim().toUpperCase()));
        model.addAttribute("masters", masters);
        return  "/admin/show-filiere-by-department" ;
    }

    @GetMapping("/departements/{departement}/{filiere}/{niveau}")
    public String showAllGroupsByDepartmentAndfieldAndlevel(@PathVariable String departement
    , @PathVariable String filiere , @PathVariable int niveau , Model model)
    {
        model.addAttribute("groups",groupTimetableService.getAllGroupByDepartmentAndFieldAndLevel(departement,filiere,niveau));
        return "admin/show-group-by-filiere-by-department";
    }


}
