package tn.esprit.innoxpert.Service;

import org.springframework.http.ResponseEntity;
import tn.esprit.innoxpert.DTO.DefenseRequest;
import tn.esprit.innoxpert.Entity.Defense;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DefenseServiceInterface {
    public List<Defense> getAllDefenses();
    public Defense getDfenseById(Long idDefense);
    public Defense addDefense(Long studentId, DefenseRequest defenseRequest);
    public void removeDefenseById(Long idDefense);
    public Defense updateDefense (Defense d );
    boolean isDefenseSlotAvailable(String classroom, LocalDate date, LocalTime time);



}
