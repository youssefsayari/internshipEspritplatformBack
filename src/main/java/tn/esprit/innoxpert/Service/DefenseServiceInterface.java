package tn.esprit.innoxpert.Service;

import org.springframework.http.ResponseEntity;
import tn.esprit.innoxpert.DTO.DefenseRequest;
import tn.esprit.innoxpert.DTO.DefenseWithEvaluationsDTO;
import tn.esprit.innoxpert.Entity.Defense;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface DefenseServiceInterface {
    public List<Defense> getAllDefenses();
    public Defense getDfenseById(Long idDefense);
    public Defense addDefense(Long studentId, DefenseRequest defenseRequest);
    public void removeDefenseById(Long idDefense);
    public Defense updateDefense(Long defenseId, DefenseRequest request);
    boolean isDefenseSlotAvailable(String classroom, LocalDate date, LocalTime time);
    List<Defense> getDefensesByTutorId(Long tutorId); // Add this new method
    public List<DefenseWithEvaluationsDTO> getDefensesWithEvaluationsByTutor(Long tutorId) ;
    List<Defense> getDefensesByStudentId(Long studentId); // Add this new method
    public Map<String, List<Defense>> getDefenseStats(List<Defense> defenses) ;






    }
